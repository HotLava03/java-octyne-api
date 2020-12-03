package io.github.hotlava03.javaoctyneapi;

import com.google.gson.Gson;
import io.github.hotlava03.javaoctyneapi.console.ConsoleListener;
import io.github.hotlava03.javaoctyneapi.entities.Server;
import io.github.hotlava03.javaoctyneapi.entities.exceptions.OctyneLoginException;
import io.github.hotlava03.javaoctyneapi.internal.ConsoleWebSocketListener;
import io.github.hotlava03.javaoctyneapi.internal.pojo.LoginResponse;
import io.github.hotlava03.javaoctyneapi.internal.pojo.ServerListResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

class OctyneClientImpl implements OctyneClient {
    private String token;
    private final String url, protocol;
    private final OkHttpClient client;
    private final Logger logger;
    private List<Server> servers;

    OctyneClientImpl(String username, String password, String url, String protocol) throws OctyneLoginException {
        this(null, username, password, url, protocol, null);
    }

    OctyneClientImpl(String username, String password, String url, String protocol, ConsoleListener listener) throws OctyneLoginException {
        this(null, username, password, url, protocol, listener);
    }

    OctyneClientImpl(String token, String url, String protocol) throws OctyneLoginException {
        this(token, url, protocol, (ConsoleListener) null);
    }

    OctyneClientImpl(String token, String url, String protocol, ConsoleListener listener) throws OctyneLoginException {
        this(token, null, null, url, protocol, listener);
    }

    OctyneClientImpl(String token, String username, String password, String url, String protocol, ConsoleListener listener) throws OctyneLoginException {
        this.token = token;
        this.url = url;
        this.protocol = protocol;
        this.client = new OkHttpClient();
        this.logger = Logger.getLogger(OctyneClient.class.getName());

        if (this.token == null) {
            login(username, password);
        } else {
            retrieveServers();
        }

        // If ConsoleListener isn't null, it means we automatically open the console for all servers.
        if (listener != null) {
            for (Server server : servers) openConsole(server.getName(), listener);
        }
    }

    @Override
    public List<Server> getServers() {
        return servers;
    }

    @Override
    public void login(final String username, String password) {
        Request request = new Request.Builder()
                .url(protocol + url + "/login")
                .addHeader("Username", username)
                .addHeader("Password", password)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.throwing(OctyneClient.class.getName(), "login", e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                Gson gson = new Gson();
                ResponseBody body = response.body();
                if (body == null)
                    throw new OctyneLoginException(username);
                LoginResponse login = gson.fromJson(body.toString(), LoginResponse.class);
                token = login.getToken();
            }
        });
    }

    @Override
    public void logout() {
        Request request = new Request.Builder()
                .url(protocol + url + "/logout")
                .addHeader("Authorization", token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.throwing(OctyneClient.class.getName(), "logout", e);
            }

            @Override
            public void onResponse(Call call, Response response) {
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

    private void retrieveServers() {
        Request request = new Request.Builder()
                .url(protocol + url + "/servers")
                .addHeader("Authorization", token)
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.throwing(OctyneClient.class.getName(), "<init>", e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                Gson gson = new Gson();
                ResponseBody body = response.body();
                if (body == null)
                    throw new IllegalStateException("Octyne has responded with an empty body while getting servers.");
                ServerListResponse serverList = gson.fromJson(body.toString(), ServerListResponse.class);
                servers = new CopyOnWriteArrayList<>();
                servers.addAll(serverList.getServers());
            }
        });
    }
}
