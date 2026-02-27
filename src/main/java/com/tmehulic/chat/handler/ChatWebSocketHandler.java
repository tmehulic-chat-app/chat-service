package com.tmehulic.chat.handler;

import com.tmehulic.chat.helper.ChatPatternHelper;
import com.tmehulic.chat.model.ChatRoom;
import com.tmehulic.chat.model.Message;
import com.tmehulic.chat.service.chat.ChatServiceImpl;
import com.tmehulic.chat.utils.InetAddressUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ChatServiceImpl chatService;
    private final ObjectMapper objectMapper;

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        ChatRoom room = chatService.getRoom(getRoomId(session.getHandshakeInfo()));

        Flux<Message> history = chatService.getHistory(room);
        Flux<Message> messages = chatService.getMessages(room).replay(10).autoConnect();

        log.info("WebSocket connection established for room: {}", room.name());

        String user = InetAddressUtils.getIpAddress(session.getHandshakeInfo().getRemoteAddress());

        chatService.joined(user, room);

        Mono<Void> send =
                session.send(
                        Flux.concat(history, messages)
                                .map(message -> session.textMessage(asPayload(message))));

        Mono<Void> receive =
                session.receive()
                        .doOnNext(
                                msg ->
                                        chatService.addMessage(
                                                asChatMessage(msg.getPayloadAsText()), room))
                        .doOnComplete(() -> chatService.left(user, room))
                        .then();

        return Mono.when(send, receive)
                .onErrorContinue(
                        (error, obj) ->
                                log.error("Error while processing : {}", error.getMessage()));
    }

    private UUID getRoomId(HandshakeInfo handshakeInfo) {
        URI uri = handshakeInfo.getUri();
        String path = uri.getPath();
        return ChatPatternHelper.getChatRoomId(path)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room path: " + path));
    }

    private Message asChatMessage(String payload) {
        return objectMapper.readValue(payload, Message.class);
    }

    private String asPayload(Message message) {
        return objectMapper.writeValueAsString(message);
    }
}
