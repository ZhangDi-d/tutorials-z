package org.example.rmi.server;

import org.example.rmi.Booking;

import java.util.Random;

import static java.util.UUID.randomUUID;

/**
 * @author dizhang
 * @date 2021-05-12
 */
public class CabBookingServiceImpl implements CabBookingService {

    @Override
    public Booking bookRide(String pickUpLocation) throws Exception {
        Random random = new Random();

        if (random.nextBoolean()) throw new RuntimeException("Cab unavailable");
        return new Booking(randomUUID().toString());
    }
}
