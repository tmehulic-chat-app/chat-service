package com.tmehulic.chat.handler;

import com.tmehulic.chat.helper.ChatPatternHelper;
import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.service.chat.ChatServiceImpl;
import com.tmehulic.chat.utils.InetAddressUtils;
import com.tmehulic.chat.utils.MessageConverter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ChatServiceImpl chatService;
    private final MessageConverter messageConverter;

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        return getSessionInfo(session.getHandshakeInfo())
                .flatMap(
                        sessionInfo ->
                                chatService
                                        .getRoom(sessionInfo.roomId())
                                        .map(room -> new SessionContext(sessionInfo, room)))
                .flatMap(
                        ctx -> {
                            log.info(
                                    "WebSocket connection established for room: {}",
                                    ctx.room().name());

                            Flux<Message> history = chatService.getHistory(ctx.room());
                            Flux<Message> messages =
                                    chatService.getMessages(ctx.room()).replay(10).autoConnect();

                            chatService.joined(ctx.sessionInfo().user(), ctx.room());

                            Mono<Void> send =
                                    session.send(
                                            Flux.concat(history, messages)
                                                    .map(
                                                            message ->
                                                                    session.textMessage(
                                                                            messageConverter
                                                                                    .asPayload(
                                                                                            message))));

                            Mono<Void> receive =
                                    session.receive()
                                            .doOnNext(
                                                    msg ->
                                                            chatService.addMessage(
                                                                    messageConverter.asMessage(
                                                                            msg.getPayloadAsText()),
                                                                    ctx.room()))
                                            .doOnComplete(
                                                    () ->
                                                            chatService.left(
                                                                    ctx.sessionInfo().user(),
                                                                    ctx.room()))
                                            .then();

                            return Mono.when(send, receive);
                        })
                .onErrorContinue(
                        (error, obj) ->
                                log.error("Error while processing : {}", error.getMessage()));
    }

    private Mono<SessionInfo> getSessionInfo(HandshakeInfo handshakeInfo) {
        return Mono.fromCallable(
                () -> {
                    URI uri = handshakeInfo.getUri();
                    String path = uri.getPath();
                    UUID roomId =
                            ChatPatternHelper.getChatRoomId(path)
                                    .orElseThrow(
                                            () ->
                                                    new IllegalArgumentException(
                                                            "Invalid room path: " + path));

                    String user = InetAddressUtils.getIpAddress(handshakeInfo.getRemoteAddress());
                    return new SessionInfo(roomId, user);
                });
    }
}
