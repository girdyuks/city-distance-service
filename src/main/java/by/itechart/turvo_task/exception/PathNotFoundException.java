package by.itechart.turvo_task.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException(final String message) {
        super(message);
    }
}
