package com.tmehulic.chat.handler;

import com.tmehulic.chat.service.chat.ChatRoom;

record SessionContext(SessionInfo sessionInfo, ChatRoom room) {}
