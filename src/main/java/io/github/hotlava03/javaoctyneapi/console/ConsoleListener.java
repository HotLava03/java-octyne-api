package io.github.hotlava03.javaoctyneapi.console;

/**
 * Listen to a server's console through
 * this interface.
 */
public interface ConsoleListener {
    /**
     * Listen to a message. Executes everytime
     * the websocket receives a console message.
     *
     * @param message The received console message.
     * @param serverName The name of the server the message was sent on.
     */
    void onMessage(String message, String serverName);
}
