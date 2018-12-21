package by.itechart.turvo_task.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(final String message) {
        super(message);
    }
}
