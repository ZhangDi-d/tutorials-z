package org.example.rmi.server;

import org.example.rmi.Booking;

/**
 * @author dizhang
 * @date 2021-05-12
 */
public interface CabBookingService{
    Booking bookRide(String pickUpLocation) throws Exception;
}
