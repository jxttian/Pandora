package net.myscloud.pandora.http.boot;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.myscloud.pandora.core.boot.Bootstrap;
import net.myscloud.pandora.http.server.HttpServerInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by user on 2015/7/2.
 */
public final class PandoraBootstrap implements Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger();

    private int port = 80;

    private int bossQuantity = 1;

    private int workerQuantity = 100;

    @Override
    public void start() {
        LOGGER.info("PandoraBootstrap Start with port:{},bossQuantity:{},workerQuantity:{}", port, bossQuantity, workerQuantity);
        try (EventLoopGroup bossGroup = new NioEventLoopGroup(bossQuantity);
             EventLoopGroup workerGroup = new NioEventLoopGroup(workerQuantity)) {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer());
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void status() {

    }

    @Override
    public PandoraBootstrap init() {

        return this;
    }

    @Override
    public PandoraBootstrap bind(int port) {
        this.port = port;
        return this;
    }

    public PandoraBootstrap setBossQuantity(int bossQuantity) {
        this.bossQuantity = bossQuantity;
        return this;
    }

    public PandoraBootstrap setWorkerQuantity(int workerQuantity) {
        this.workerQuantity = workerQuantity;
        return this;
    }
}
