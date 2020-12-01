package io.github.hotlava03.javaoctyneapi;

import io.github.hotlava03.javaoctyneapi.console.ConsoleListener;

public class OctyneClientBuilder {
    private String token, username, password;
    private final String url;
    private boolean https;

    private ConsoleListener listener;

    public OctyneClientBuilder(String url) {
        this.url = url;
    }

    public OctyneClientBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public OctyneClientBuilder setHttps() {
        return setHttps(true);
    }

    public OctyneClientBuilder setHttps(boolean https) {
        this.https = https;
        return this;
    }

    public OctyneClientBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public OctyneClientBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public OctyneClientBuilder setConsoleListener(ConsoleListener listener) {
        this.listener = listener;
        return this;
    }

    public OctyneClient build() {
        if (this.token != null) return new OctyneClientImpl(token, url, https ? "https" : "http", listener);
        else {
            if (this.username == null || this.password == null) {
                throw new IllegalArgumentException("Either a token must be given or a username and " +
                        "a password in order to build an OctyneClient instance.");
            }

            return new OctyneClientImpl(username, password, url, https ? "https" : "http", listener);
        }
    }
}
