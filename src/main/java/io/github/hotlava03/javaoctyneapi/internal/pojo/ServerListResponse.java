package io.github.hotlava03.javaoctyneapi.internal.pojo;

import io.github.hotlava03.javaoctyneapi.entities.Server;

import java.util.List;

public class ServerListResponse {
    private List<Server> servers;

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }
}
