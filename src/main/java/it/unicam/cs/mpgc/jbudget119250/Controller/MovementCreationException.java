package it.unicam.cs.mpgc.jbudget119250.Controller;

/**
 * This exception is thrown to indicate a failure in the creation of a movement entity.
 * It is used when the {@code MovementFactory} encounters an issue during the
 * instantiation or configuration of a movement object.
 */
public class MovementCreationException extends RuntimeException {
    public MovementCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
