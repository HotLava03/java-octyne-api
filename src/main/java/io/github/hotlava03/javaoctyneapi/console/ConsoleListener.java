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
     * @param message The received message.
     */
    void onMessage(String message);
}
