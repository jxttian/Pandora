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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = ch.pipeline();

        /**
         * http-request解码器
         * http服务器端对request解码
         */
        pipeline.addLast("decoder", new HttpRequestDecoder());
        /**
         * http-response解码器
         * http服务器端对response编码
         */
        pipeline.addLast("encoder", new HttpResponseEncoder());

        /**
         * 压缩
         * Compresses an HttpMessage and an HttpContent in gzip or deflate encoding
         * while respecting the "Accept-Encoding" header.
         * If there is no matching encoding, no compression is done.
         */
        pipeline.addLast("deflater", new HttpContentCompressor());

        pipeline.addLast(new HttpObjectAggregator(65536));

        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new HttpServerHandler());
    }
}
