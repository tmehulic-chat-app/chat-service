package com.tmehulic.chat.handler;

import com.tmehulic.chat.dto.ChatRoom;
import com.tmehulic.chat.helper.ChatPatternHelper;
import com.tmehulic.chat.service.ChatService;

import lombok.AllArgsConstructor;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@AllArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ChatService chatService;

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        ChatRoom room = chatService.getRoom(getRoomName(session.getHandshakeInfo()));
        Flux<String> history = chatService.getHistory(room);
        Flux<String> messages = chatService.getMessages(room).replay(10).autoConnect();

        Mono<Void> send = session.send(Flux.concat(history, messages).map(session::textMessage));

        Mono<Void> receive =
                session.receive()
                        .doOnNext(msg -> chatService.addMessage(msg.getPayloadAsText(), room))
                        .then();

        return Mono.when(send, receive);
    }

    private String getRoomName(HandshakeInfo handshakeInfo) {
        URI uri = handshakeInfo.getUri();
        String path = uri.getPath();
        return ChatPatternHelper.getChatRoomName(path)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room path: " + path));
    }
}
