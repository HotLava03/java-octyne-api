package io.github.hotlava03.javaoctyneapi.entities;

/**
 * Represents a server in Octyne.
 */
public class Server {
    private final String name;
    private final boolean online;

    /**
     * Create a new Server instance, offline by default.
     *
     * @param name The server name.
     */
    public Server(String name) {
        this(name, false);
    }

    /**
     * Create a new Server instance.
     *
     * @param name   The server name.
     * @param online Whether the server is online or not.
     */
    public Server(String name, boolean online) {
        this.name = name;
        this.online = online;
    }

    /**
     * Get the server name.
     *
     * @return The server name
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether the server is online or not.
     *
     * @return Whether the server is online or not.
     */
    public boolean isOnline() {
        return online;
    }
}
