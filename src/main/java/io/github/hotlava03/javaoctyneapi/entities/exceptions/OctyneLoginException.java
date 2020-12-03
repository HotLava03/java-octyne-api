package io.github.hotlava03.javaoctyneapi.entities.exceptions;

/**
 * Thrown when a login attempt to
 * Octyne fails.
 */
public class OctyneLoginException extends IllegalStateException {
    private final String username;

    public OctyneLoginException(String username) {
        this.username = username;
    }

    /**
     * Get the username involved in this login
     * failure.
     *
     * @return The username involved.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the exception's message.
     *
     * @return The exception's message.
     */
    @Override
    public String getMessage() {
        return "Login failed for " + username + ".";
    }
}
