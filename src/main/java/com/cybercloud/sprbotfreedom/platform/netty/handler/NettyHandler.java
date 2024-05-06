package com.cybercloud.sprbotfreedom.platform.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * CIS 客户端响应处理类
 * @author liuyutang
 * @date 2023/8/2
 */
@Slf4j
public class NettyHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("channel read listener: {}",msg);
    }
}
