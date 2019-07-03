package cn.com.onlinetool.jt809.decoder;


import cn.com.onlinetool.jt809.constants.JT809MessageConstants;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import cn.com.onlinetool.jt809.util.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author choice
 * @description: 消息解码
 * @date 2018-12-27 14:39
 *
 */
@Slf4j
@Service
public class Byte2MessageDecoder{
    private static Map<String, byte[]> cache = new HashMap<String, byte[]>();

    public void decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String channelKey = ctx.channel().remoteAddress().toString();

        //判断是否有可读的字节
        if (msg.readableBytes() <= 0) {
            return;
        }

        //读取缓冲区数据
        byte[] readDatas = new byte[msg.readableBytes()];
        msg.readBytes(readDatas);
        log.info("接收到数据包, packetLen : {}, packet : {}",readDatas.length, ByteArrayUtil.bytes2HexStr(readDatas));

        //拼接缓存数据
        byte[] cacheDatas = cache.get(channelKey);
        if(null != cacheDatas){
            readDatas = ByteArrayUtil.append(cacheDatas,readDatas);
            cache.remove(channelKey);
            log.info("拼接后的数据包：{}",ByteArrayUtil.bytes2HexStr(readDatas));
        }

        //校验数据头
        if(!PacketUtil.checkHeadFlag(readDatas)){
            //防止极端情况，请求头校验不通过的情况 丢掉本包数据 同时 丢掉上一包缓存的剩余数据
            //防止上一包剩余数据不包含数据起始符 会导致拼接后的数据包一直校验不通过
            cache.remove(channelKey);
            log.warn("数据包标识符验证失败 : {}",ByteArrayUtil.bytes2HexStr(readDatas));
            return;
        }

        //数据转义
        String dataStr = ByteArrayUtil.bytes2FullHexStr(readDatas);
        dataStr = dataStr.replaceAll("0x5a0x01","0x5b");
        dataStr = dataStr.replaceAll("0x5a0x02","0x5a");
        dataStr = dataStr.replaceAll("0x5e0x01","0x5d");
        dataStr = dataStr.replaceAll("0x5e0x02","0x5e");
        readDatas = ByteArrayUtil.fullHexStr2Bytes(dataStr);
        log.info("转义后的数据包：{}",ByteArrayUtil.bytes2HexStr(readDatas));

        //如果数据小于一整包数据的最小长度
        if(readDatas.length < JT809MessageConstants.MSG_MIN_LEN){
            log.warn("数据长度小于整包数据最小长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(readDatas));
            cache.put(channelKey,readDatas);
            return;
        }

        //判断是否有完整数据包，没有直接缓存
        int packetLen = PacketUtil.getPacketLen(readDatas);
        if(readDatas.length < packetLen){
            log.warn("数据长度小于整包数据长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(readDatas));
            cache.put(channelKey,readDatas);
            return;
        }

        //解析数据
        this.parseAndPushData(ctx,channelKey,readDatas, 0);

        msg.release();
    }


    /**
     * 解析并返回数据
     * @param channelKey
     * @param readDatas
     * @param index
     */
    private void parseAndPushData(ChannelHandlerContext ctx,String channelKey,byte[] readDatas,int index){
        //校验数据头 防止递归调用失败
        if(!PacketUtil.checkHeadFlag(readDatas)){
            //防止极端情况，请求头校验不通过的情况 丢掉本包数据 同时 丢掉上一包缓存的剩余数据
            //防止上一包剩余数据不包含数据起始符 会导致拼接后的数据包一直校验不通过
            cache.remove(channelKey);
            log.warn("数据包标识符验证失败 : {}",ByteArrayUtil.bytes2HexStr(readDatas));
            return;
        }

        //整包长度
        int packetLen = PacketUtil.getPacketLen(readDatas);

        //一个完整包
        byte[] fullPacket = ByteArrayUtil.subBytes(readDatas,index,packetLen);
        log.info("拆包后的单包数据 --> fullPacket : {}",ByteArrayUtil.bytes2HexStr(fullPacket));
        //验证数据包有效性
        if(!PacketUtil.checkPacket(fullPacket)){
            log.info("数据校验失败 --> fullPacket : {}",ByteArrayUtil.bytes2HexStr(fullPacket));
            return;
        }
        ctx.fireChannelRead(PacketUtil.bytes2Message(fullPacket));
        index += packetLen;

        //剩余长度
        int remainingLen = readDatas.length - index;

        //没有数据，结束
        if(remainingLen < 1){
            return;
        }

        //剩余数据长度小于一包数据的最小长度，缓存数据
        if(remainingLen < JT809MessageConstants.MSG_MIN_LEN) {
            log.warn("剩余数据长度小于整包数据最小长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(ByteArrayUtil.subBytes(readDatas,index,remainingLen)));
            cache.put(channelKey,ByteArrayUtil.subBytes(readDatas,index,remainingLen));
            return;
        }

        //下一包数据的总长度
        packetLen = PacketUtil.getPacketLen(ByteArrayUtil.subBytes(readDatas,index,readDatas.length - index));
        //剩余数据长度小于整包数据长度
        if(remainingLen < packetLen){
            log.warn("剩余数据长度小于整包数据长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(ByteArrayUtil.subBytes(readDatas,index,remainingLen)));
            cache.put(channelKey,ByteArrayUtil.subBytes(readDatas,index,remainingLen));
            return;
        }

        //还有完整数据包 递归调用
        this.parseAndPushData(ctx,channelKey,readDatas,index);


    }
}
