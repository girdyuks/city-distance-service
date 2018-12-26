package by.itechart.city_distance_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException(final String message) {
        super(message);
    }
}
