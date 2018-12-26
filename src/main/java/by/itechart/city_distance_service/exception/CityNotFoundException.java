package by.itechart.city_distance_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(final String message) {
        super(message);
    }
}
