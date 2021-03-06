/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.collector.receiver.grpc.service;

import com.google.protobuf.GeneratedMessageV3;
import com.navercorp.pinpoint.grpc.AgentHeaderFactory;
import com.navercorp.pinpoint.grpc.server.ServerContext;
import com.navercorp.pinpoint.grpc.server.TransportMetadata;
import com.navercorp.pinpoint.io.request.DefaultServerRequest;
import com.navercorp.pinpoint.io.request.Message;
import com.navercorp.pinpoint.io.request.ServerRequest;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author Woonduk Kang(emeroad)
 */
public class ServerRequestFactory {


    public ServerRequestFactory() {
    }

    public ServerRequest<GeneratedMessageV3> newServerRequest(Message<?> message) throws StatusException {
        final Context current = Context.current();
        final AgentHeaderFactory.Header header = ServerContext.AGENT_INFO_KEY.get(current);
        if (header == null) {
            throw Status.INTERNAL.withDescription("Not found request header").asException();
        }

        final TransportMetadata transportMetadata = ServerContext.TRANSPORT_METADATA_KEY.get(current);
        if (transportMetadata == null) {
            throw Status.INTERNAL.withDescription("Not found transportMetadata").asException();
        }

        InetSocketAddress inetSocketAddress = transportMetadata.getRemoteAddress();
        ServerRequest request = new DefaultServerRequest(message, inetSocketAddress.getHostString(), inetSocketAddress.getPort());
        return request;
    }
}
