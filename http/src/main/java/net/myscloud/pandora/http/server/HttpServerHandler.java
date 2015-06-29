/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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

import java.util.List;
import java.util.Map;

import javassist.CtMethod;
import javassist.bytecode.MethodInfo;
import net.myscloud.pandora.common.annotation.Path;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

	private HttpRequest request;
	/** Buffer that stores the response content */
	private final StringBuilder buf = new StringBuilder();

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
			HttpRequest request = this.request = (HttpRequest) msg;

			if (HttpHeaderUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}

			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
					request.uri());
			CtMethod method = HttpServer.PATHMAP.get(queryStringDecoder.path());
			if (method == null) {
				sendError(ctx, HttpResponseStatus.NOT_FOUND);
				return;
			}
			try {
				Path path = (Path)method.getAnnotation(Path.class);
				if (!path.method().getMethod().equals(request.method())) {
					sendError(ctx, HttpResponseStatus.BAD_REQUEST);
					return;
				}
				Map<String, List<String>> params = queryStringDecoder
						.parameters();
				MethodInfo methodInfo = method.getMethodInfo();
				System.out.println();
//				List<Object> paras = new ArrayList<Object>();
//				for (Parameter parameter : parameters) {
//					System.out.println(parameter.getType());
//					if (parameter.getType().isArray()) {
//						List<String> temp = params.get(parameter.getAnnotation(
//								Param.class).value());
//						String[] tempArray = new String[temp.size()];
//						paras.add(temp.toArray(tempArray));
//					} else {
//						paras.add(params.get(
//								parameter.getAnnotation(Param.class).value())
//								.get(0));
//					}
//				}
//				Object object = null;
//				if (params.size() > 0) {
//					object = method.getReturnType().cast(
//							method.invoke(HttpServer.BEANMAP.get(method
//									.getDeclaringClass().getName()),
//									paras.get(0).toString(), paras.get(1)));
//				} else {
//					object = method.getReturnType().cast(
//							method.invoke(HttpServer.BEANMAP.get(method
//									.getDeclaringClass().getName())));
//				}
//				if (method.getAnnotation(Json.class) != null) {
//					buf.append(GsonUtil.getGson().toJson(object));
//				} else {
//					buf.append(object);
//				}
			} catch (Exception e) {
				e.printStackTrace();
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
