/**
 * @Title: HttpServer.java
 * @Package net.myscloud.pandora.http
 * @Description: Copyright: Copyright (c) 2015
 * Company:杭州点望科技有限公司
 */
package net.myscloud.pandora.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.myscloud.pandora.common.reflect.util.PackageUtil;
import net.myscloud.pandora.http.boot.PandoraBootstrap;


/**
 * @ClassName: HttpServer
 * @Description:
 */
public final class HttpServer {

    public static ConcurrentHashMap<String, CtMethod> PATHMAP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Object> BEANMAP = new ConcurrentHashMap<>();

    public void start(final int port) {
//        init();
        try (EventLoopGroup bossGroup = new NioEventLoopGroup(1);
             EventLoopGroup workerGroup = new NioEventLoopGroup(100)) {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer());
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        try {
            ClassPool pool = ClassPool.getDefault();
            String packageName = "net.myscloud";
            List<String> classNames = PackageUtil.getClassName(packageName);
            if (classNames == null || classNames.size() == 0) {
                return;
            }
//			for (String className : classNames) {
//		        CtClass type = pool.get(className);
//				if (!type.hasAnnotation(Controller.class)) {
//					continue;
//				}
//
//				BEANMAP.put(type.getName(),type);
//
//				for (CtMethod method : type.getMethods()) {
//					Path path=(Path)method.getAnnotation(Path.class);
//					if (path!=null) {
//						PATHMAP.put(path.value(), method);
//					}
//				}
//			}
        } catch (SecurityException
                | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        HttpServer httpServer = new HttpServer();
//        httpServer.start(8081);
        PandoraBootstrap boot = new PandoraBootstrap();
        boot.bind(80).setBossQuantity(1).setWorkerQuantity(100).start();
    }
}
