package com.cybercloud.sprbotfreedom.platform.netty;

import com.cybercloud.sprbotfreedom.platform.netty.handler.NettyHandler;
import com.cybercloud.sprbotfreedom.platform.util.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *  netty服务端
 * @author liuyutang
 * @date 2023/8/2
 */
@Slf4j
@Component
public class NettyServer implements CommandLineRunner {

    @Value("${system.netty.enabled:false}")
    private Boolean nettyEnabled;
    @Value("${system.netty.port}")
    private Integer nettyPort;


    @Override
    public void run(String... args) throws Exception {
        if(nettyEnabled){
            ThreadUtil.threadPoolExecutor.execute(()->{
                log.info(">>> Start netty server listening on port :{}" , nettyPort);
                NioEventLoopGroup boss = new NioEventLoopGroup();
                NioEventLoopGroup worker = new NioEventLoopGroup();

                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(boss, worker)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG,1024)
                        // 开启长连接
                        .option(ChannelOption.SO_KEEPALIVE,true)
                        // 自适应缓存调配
                        .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                        // 日志代理
                        .handler(new LoggingHandler())
                        .childHandler(new ChannelInitializer(){
                            @Override
                            protected void initChannel(Channel channel) throws Exception {
                                channel.pipeline().addLast("encoder",new ObjectEncoder())
                                        .addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                        .addLast(new NettyHandler());
                            }
                        });
                try {
                    ChannelFuture sync = bootstrap.bind(nettyPort).sync();
                    sync.channel().closeFuture().sync();
                }catch (Exception e) {
                    log.error(">>> Start netty server listening on port error :{}" , e.getMessage());
                }finally {
                    boss.shutdownGracefully();
                    worker.shutdownGracefully();
                }
            });
        }
    }
}
