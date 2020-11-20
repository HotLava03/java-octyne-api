package io.github.hotlava03.javaoctyneapi;

import com.google.gson.Gson;
import io.github.hotlava03.javaoctyneapi.console.ConsoleListener;
import io.github.hotlava03.javaoctyneapi.entities.Server;
import io.github.hotlava03.javaoctyneapi.internal.ConsoleWebSocketListener;
import io.github.hotlava03.javaoctyneapi.internal.pojo.ServerList;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

class OctyneClientImpl implements OctyneClient {
    private String token;
    private final String url, protocol;
    private final OkHttpClient client;
    private final Logger logger;
    private List<Server> servers;

    OctyneClientImpl(String token, String url, String protocol) {
        this.token = token;
        this.url = url;
        this.protocol = protocol;
        this.client = new OkHttpClient();
        this.logger = Logger.getLogger(OctyneClient.class.getName());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                logger.throwing(OctyneClient.class.getName(), "<init>", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Gson gson = new Gson();
                ResponseBody body = response.body();
                if (body == null)
                    throw new IllegalStateException("Octyne has responded with an empty body while getting servers.");
                ServerList serverList = gson.fromJson(body.toString(), ServerList.class);
                servers = new Vector<>();
                servers.addAll(serverList.getServers());
            }
        });
    }

    @Override
    public List<Server> getServers() {
        return servers;
    }

    @Override
    public void logout() {
        Request request = new Request.Builder()
                .url(protocol + url + "/logout")
                .addHeader("Authorization", token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                logger.throwing(OctyneClient.class.getName(), "logout", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                token = null;
            }
        });
    }

    @Override
    public void openConsole(String serverName, ConsoleListener listener) {
        ConsoleWebSocketListener socket = new ConsoleWebSocketListener(
                token,
                url,
                serverName,
                client,
                listener
        );
        socket.run();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String newToken) {
        token = newToken;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
