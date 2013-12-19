package com.levelsbeyond.execserver.resources.atmosphere;

import java.io.IOException;

import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.util.SimpleBroadcaster;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebSocketHandlerService(path = "/chat", broadcaster = SimpleBroadcaster.class)
public class WebSocketChat extends WebSocketHandlerAdapter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onOpen(WebSocket webSocket) throws IOException {
        // Create a communication channel called 'chat' to share messages received.
        webSocket.resource().setBroadcaster(
              BroadcasterFactory.getDefault().lookup("/chat", true));
    }

    public void onTextMessage(WebSocket webSocket, String message) throws IOException {
//        AtmosphereResource r = webSocket.resource();
//        Broadcaster b = r.getBroadcaster();
//        b.broadcast(mapper.writeValueAsString(mapper.readValue(message, Data.class)));
    }
}