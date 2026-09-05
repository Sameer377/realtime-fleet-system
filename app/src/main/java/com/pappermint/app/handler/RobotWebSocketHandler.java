package com.pappermint.app.handler;

import com.pappermint.app.dto.RobotTelemetryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class RobotWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status) {

        sessions.remove(session);
    }

    public void broadcast(RobotTelemetryDTO state) {

        try {
            String json = objectMapper.writeValueAsString(state);

            for (WebSocketSession session : sessions) {

                if (session.isOpen()) {
                    session.sendMessage(
                            new TextMessage(json)
                    );
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}