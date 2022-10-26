package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于netty的服务类
 * @author sgz
 * @since 1.0.0 2022/10/16
 */
@Slf4j
public class NettyServer {
    private final ServerBootstrap bootstrap;

    private EventLoopGroup parentGroup;

    private EventLoopGroup childGroup;


    public NettyServer(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * 初始化相应参数
     * @param nThread 需要执行的线程
     */
    public void init(Integer nThread){
        parentGroup = new NioEventLoopGroup(nThread);
        childGroup = new NioEventLoopGroup();
        bootstrap.group(parentGroup, childGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast( new IdleStateHandler(6, 0, 0));
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    /**
     * 绑定相应端口并等待事件触发
     * @param port 端口
     */
    public void bind(int port){
        ChannelFuture future = null;

        try {
            future = bootstrap.bind(port).sync();
            log.info("服务器启动成功 监听端口(" + port + ")");
            // 线程会一直阻塞知道服务器关闭
            future.channel().closeFuture().sync();
            log.info("服务器关闭");

        } catch (InterruptedException e) {
            log.warn("Netty绑定异常", e);
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
