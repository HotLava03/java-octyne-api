package io.github.hotlava03.javaoctyneapi;

import io.github.hotlava03.javaoctyneapi.console.ConsoleListener;
import io.github.hotlava03.javaoctyneapi.entities.Server;
import io.github.hotlava03.javaoctyneapi.entities.exceptions.OctyneLoginException;

import java.util.List;

/**
 * Represents a session in Octyne.<br>
 * An Octyne client contains one token,
 * which means each session must be
 * an instance of this. To create a
 * client, use {@link OctyneClientBuilder}.
 *
 * @see OctyneClientBuilder
 */
public interface OctyneClient {
    /**
     * Get all the servers in this Octyne server.
     *
     * @return A list of {@link Server} containing all servers.
     */
    List<Server> getServers();

    /**
     * Log out of Octyne, destroying the current
     * token.
     */
    void logout();

    /**
     * Login into Octyne. This is called automatically
     * when creating a new {@link OctyneClient} instance.
     *
     * @param username The username.
     * @param password The password.
     */
    void login(String username, String password) throws OctyneLoginException;

    /**
     * Listen to console messages in a specific
     * server.
     *
     * @param serverName The server to listen to's name
     * @param listener   The console listener which will
     *                   listen to every console message.
     */
    void openConsole(String serverName, ConsoleListener listener);

    /**
     * Get the current session's token.
     *
     * @return The current token.
     */
    String getToken();

    /**
     * Change the token.
     *
     * @param newToken The new token to change to.
     */
    void setToken(String newToken);

    /**
     * Get Octyne's URL.
     *
     * @return Octyne's URL.
     */
    String getUrl();
}
