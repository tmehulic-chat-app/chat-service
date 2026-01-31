package com.tmehulic.chat.helper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatPatternHelper {
    private ChatPatternHelper() {}

    private static final Pattern CHAT_ROOM_PATTERN = Pattern.compile("/chat/(.+)");

    public static Optional<String> getChatRoomName(String path) {
        Matcher matcher = CHAT_ROOM_PATTERN.matcher(path);
        if (matcher.matches()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }
}
