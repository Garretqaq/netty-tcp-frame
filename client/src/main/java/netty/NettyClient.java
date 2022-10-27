package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * 基于netty的客户端
 * @author sgz
 * @since 1.0.0 2022/10/16
 */
@Slf4j
public class NettyClient {
    private String serverIP;

    private int port;

    private Channel channel;

    private EventLoopGroup work;

    /**
     * 构造连接主机信息
     * @param serverIP ip
     * @param port 端口
     */

    public void connect(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
    }

    /**
     * 初始化相应工作线程及连接
     * @throws InterruptedException 异常
     */
    public void init() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        this.work = new NioEventLoopGroup(1);
        bootstrap.group(work);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast("encode", new ProtocolDecoder());
                ch.pipeline().addLast("decode",new ProtocolEncoder());
            }
        });
        bootstrap.channel(NioSocketChannel.class);

        ChannelFuture channelFuture = bootstrap.connect(serverIP,port).sync();

        channelFuture.addListener((ChannelFutureListener) arg0 -> {
            if (channelFuture.isSuccess()) {
                log.info("连接服务器成功----主机:{},端口:{}", serverIP, port);
            } else {
                log.error("连接服务器失败----主机:{},端口:{}", serverIP, port);
                channelFuture.cause().printStackTrace();
                work.shutdownGracefully(); //关闭线程组
            }
        });
        this.channel = channelFuture.channel();
    }

    /**
     * 获取相应连接进行读写操作
     * @return 连接成功的channel
     */
    public Channel getChannel(){
        return this.channel;
    }

    /**
     * 关闭连接
     */
    public void close(){
        // 关闭连接
        this.channel.close();
        //关闭线程组
        this.work.shutdownGracefully();
    }
}
