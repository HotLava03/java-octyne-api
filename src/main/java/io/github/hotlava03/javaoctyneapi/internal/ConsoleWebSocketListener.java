package io.github.hotlava03.javaoctyneapi.internal;

import io.github.hotlava03.javaoctyneapi.console.ConsoleListener;
import io.github.hotlava03.javaoctyneapi.entities.Server;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;

public class ConsoleWebSocketListener extends WebSocketListener {
    private final String token, url, server;
    private final OkHttpClient client;
    private final ConsoleListener listener;

    public ConsoleWebSocketListener(String token, String url, String server, OkHttpClient client, ConsoleListener listener) {
        this.token = token;
        this.url = url;
        this.server = server;
        this.client = client;
        this.listener = listener;
    }

    public void run() {
        Request request = new Request.Builder()
                .url("ws://" + url + "/server/" + server + "/console")
                .addHeader("Authorization", token)
                .build();
        client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        listener.onMessage(text, server);
    }
}
