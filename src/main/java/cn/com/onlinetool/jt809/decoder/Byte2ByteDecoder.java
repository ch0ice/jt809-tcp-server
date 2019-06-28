package cn.com.onlinetool.jt809.decoder;


import cn.com.onlinetool.jt809.bean.BasePacket;
import cn.com.onlinetool.jt809.constants.JT809Constants;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import cn.com.onlinetool.jt809.util.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author choice
 * @description: 消息解码
 * @date 2018-12-27 14:39
 *
 */
public class Byte2ByteDecoder extends ByteToMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(Byte2ByteDecoder.class);
    private static Map<String, byte[]> cache = new ConcurrentHashMap<String, byte[]>();

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String channelKey = ctx.channel().remoteAddress().toString();

        //判断是否有可读的字节
        if (msg.readableBytes() <= 0) {
            return;
        }

        //读取缓冲区数据
        byte[] readDatas = new byte[msg.readableBytes()];
        msg.readBytes(readDatas);
        logger.info("接收到数据包, packetLen : {}, packet : {}",readDatas.length, ByteArrayUtil.bytes2HexStr(readDatas));


        //拼接缓存数据
        byte[] cacheDatas = cache.get(channelKey);
        if(null != cacheDatas){
            readDatas = ByteArrayUtil.append(cacheDatas,readDatas);
            cache.remove(channelKey);
            logger.info("拼接后的数据包：{}",ByteArrayUtil.bytes2HexStr(readDatas));
        }

        //如果数据小于一整包数据的最小长度
        if(readDatas.length < JT809Constants.MSG_MIN_LEN){
            logger.warn("数据长度小于整包数据最小长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(readDatas));
            cache.put(channelKey,readDatas);
            return;
        }

        //整包长度
        int packetLen = PacketUtil.getPacketLen(readDatas);
        //判断是否有完整数据包，没有直接缓存
        if(readDatas.length < packetLen){
            logger.warn("数据长度小于整包数据长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(readDatas));
            cache.put(channelKey,readDatas);
            return;
        }

        //解析数据
        this.parseAndPushData(channelKey,readDatas, 0,out);


    }


    /**
     * 解析并返回数据
     * @param channelKey
     * @param readDatas
     * @param index
     * @param out
     */
    private void parseAndPushData(String channelKey,byte[] readDatas,int index,List<Object> out){
        if(PacketUtil.checkHeadFlag(readDatas)){
            BasePacket basePacket = new BasePacket();
            basePacket.setHeadFlag(readDatas[index]);
            int offset = index + 1;
            basePacket.setMsgHead(ByteArrayUtil.subBytes(readDatas,offset,JT809Constants.MSG_HEAD_LEN));
            offset += JT809Constants.MSG_HEAD_LEN;
            basePacket.setMsgBody(ByteArrayUtil.subBytes(readDatas,offset,PacketUtil.getMsgBodyLen(readDatas)));
            offset += PacketUtil.getMsgBodyLen(readDatas);
            basePacket.setCrcCode(ByteArrayUtil.subBytes(readDatas,offset,JT809Constants.MSG_CRC_LEN));
            offset += JT809Constants.MSG_CRC_LEN;

            //整包长度
            int packetLen = PacketUtil.getPacketLen(readDatas);
            //验证数据包有效性
            if(PacketUtil.checkPacket(basePacket)){
                //一个完整包
                byte[] fullPacket = ByteArrayUtil.subBytes(readDatas,index,packetLen);
                logger.info("拆包后的单包数据 --> baseData : {}",ByteArrayUtil.bytes2HexStr(fullPacket));
                out.add(basePacket);
                index += packetLen;
                //终端成功响应
//                byte[] result = new byte[9];
//                ctx.writeAndFlush(result);
            }else{
                //终端错误响应
//                byte[] result = new byte[9];
//                ctx.writeAndFlush(result);
            }

            //剩余长度
            int remainingLen = readDatas.length - index;

            //没有数据，结束
            if(remainingLen < 1){
                return;
            }

            //剩余数据长度小于一包数据的最小长度，缓存数据
            if(remainingLen < JT809Constants.MSG_MIN_LEN) {
                logger.warn("剩余数据长度小于整包数据最小长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(ByteArrayUtil.subBytes(readDatas,index,remainingLen)));
                cache.put(channelKey,ByteArrayUtil.subBytes(readDatas,index,remainingLen));
                return;
            }

            //下一包数据的总长度
            packetLen = PacketUtil.getPacketLen(ByteArrayUtil.subBytes(readDatas,offset,readDatas.length));
            //剩余数据长度小于整包数据长度
            if(remainingLen < packetLen){
                logger.warn("剩余数据长度小于整包数据长度，缓存数据：{}",ByteArrayUtil.bytes2HexStr(ByteArrayUtil.subBytes(readDatas,index,remainingLen)));
                cache.put(channelKey,ByteArrayUtil.subBytes(readDatas,index,remainingLen));
                return;
            }

            //还有完整数据包 递归调用
            this.parseAndPushData(channelKey,readDatas,index,out);

        } else if(null != cache.get(channelKey)){
            logger.warn("数据包标识符验证失败 : {}",ByteArrayUtil.bytes2HexStr(readDatas));
            //防止极端情况，请求头校验不通过的情况 丢掉本包数据 同时 丢掉上一包缓存的剩余数据
            //防止上一包剩余数据不包含数据起始符 会导致拼接后的数据包一直校验不通过
            cache.remove(channelKey);
        }
    }
}
