package cn.com.onlinetool.jt809.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author choice
 * @date 2019-03-13 14:40
 *
 */
public class JT809TcpClient {
    public void runClient() throws Exception{
        //如果现在客户端不同，那么也可以不实用多线程模式来处理
        //在netty中考虑到代码的统一性，也允许你在客户端设置线程池
        EventLoopGroup group = new NioEventLoopGroup();//创建一个线程池
        try {
            Bootstrap client = new Bootstrap();//创建客户毒案处理程序
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true) //允许接收大块的返回数据
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(100));

                            //自定义分隔符
//                            ByteBuf delimiter = Unpooled.copiedBuffer("11111".getBytes());
//                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                            //使用系统的分隔符
//                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new Message2ByteEncoder());
                            socketChannel.pipeline().addLast(new Byte2MessageDecoder());
                            socketChannel.pipeline().addLast(new JT809TcpClientHandler());
                        }
                    });
            ChannelFuture channelFuture = client.connect("127.0.0.1", 11111).sync();//等待连接处理
            channelFuture.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("服务器连接已经完成，可以确保进行消息准确传输");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();  //等待关闭连接

        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new JT809TcpClient().runClient();
    }
}
