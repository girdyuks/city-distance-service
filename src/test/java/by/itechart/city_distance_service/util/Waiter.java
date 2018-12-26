package by.itechart.city_distance_service.util;

import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

public class Waiter {
    private static final Long DEFAULT_TIMEOUT = 5000L;
    private static final Long WAIT_DELAY = 100L;
    private static final String WAIT_TIME_EXPIRED = "Wait time expired";

    private Waiter() {
    }

    public static void wait(BooleanSupplier testFunction) throws TimeoutException, InterruptedException {
        wait(testFunction, DEFAULT_TIMEOUT);
    }

    public static void wait(BooleanSupplier testFunction, Long timeout) throws TimeoutException, InterruptedException {
        for(Long countdown = timeout; countdown > 0L; countdown = countdown - WAIT_DELAY) {
            Thread.sleep(WAIT_DELAY);
            if (testFunction.getAsBoolean()) {
                return;
            }
        }

        throw new TimeoutException(WAIT_TIME_EXPIRED);
    }
}
