package net.myscloud.pandora.http.server;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.myscloud.pandora.common.util.GsonUtil;
import net.myscloud.pandora.mvc.bind.UrlBind;
import net.myscloud.pandora.mvc.bind.annotation.response.Json;
import net.myscloud.pandora.mvc.bind.method.MethodDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private HttpRequest request;
    /**
     * Buffer that stores the response content
     */
    private final StringBuilder buf = new StringBuilder();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status + "\r\n",
                CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpRequest msg) {
        if (msg instanceof HttpRequest) {
            buf.setLength(0);
            HttpRequest request = this.request = msg;

            if (HttpHeaderUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
                    request.uri());
            MethodDetail methodDetail = UrlBind.getUrlMap().get(queryStringDecoder.path());
            if (methodDetail == null) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
            try {
                if (!methodDetail.getRequestMethod().getMethod().equals(request.method())) {
                    sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                    return;
                }
                Map<String, List<String>> params = queryStringDecoder
                        .parameters();

                Map<String, Class> paramsMap = methodDetail.getParamsMap();

                if (params.size() != paramsMap.size()) {
                    sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                    return;
                }

                Object[] paras = new Object[params.size()];

                Method method = methodDetail.getMethod();

                int cursor = 0;
                for (Map.Entry<String, Class> entry : paramsMap.entrySet()) {
                    List<String> temp = params.get(entry.getKey());
                    if (entry.getValue().isArray()) {
                        String[] tempArray = new String[temp.size()];
                        paras[cursor] = temp.toArray(tempArray);
                    } else {
                        paras[cursor] = temp.get(0);
                    }
                    cursor++;
                }

                Object result = method.getReturnType().cast(method.invoke(HttpServer.factory.getInstance(methodDetail.getClassName()), paras));

                if (method.getAnnotation(Json.class) != null) {
                    buf.append(GsonUtil.getGson().toJson(result));

                } else {
                    buf.append(result);
                }

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            appendDecoderResult(buf, request);
        }

        if (msg instanceof HttpContent) {
            if (msg instanceof LastHttpContent) {
                LastHttpContent trailer = (LastHttpContent) msg;
                if (!writeResponse(trailer, ctx)) {
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(
                            ChannelFutureListener.CLOSE);
                }
            }
        }
    }

    private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.decoderResult();
        if (result.isSuccess()) {
            return;
        }
        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }

    private boolean writeResponse(HttpObject currentObj,
                                  ChannelHandlerContext ctx) {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                currentObj.decoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            // Add keep alive header as per:
            // -
            // http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        return keepAlive;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
