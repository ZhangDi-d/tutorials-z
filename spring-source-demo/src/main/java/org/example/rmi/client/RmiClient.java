package org.example.rmi.client;

import org.example.rmi.Booking;
import org.example.rmi.server.CabBookingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

/**
 * @author dizhang
 * date 2021-05-12
 */
public class RmiClient {

    @Bean
    @SuppressWarnings("deprecation")
    RmiProxyFactoryBean service() {
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl("rmi://localhost:1099/CabBookingService");
        rmiProxyFactory.setServiceInterface(CabBookingService.class);
        return rmiProxyFactory;
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(RmiClient.class);
        applicationContext.refresh();
        System.out.println("client start...");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));

        CabBookingService cabBookingService = (CabBookingService) applicationContext.getBean("service", CabBookingService.class);
        Booking bookingOutcome = cabBookingService.bookRide("13 Seagate Blvd, Key Largo, FL 33037");
        System.out.println(bookingOutcome);
    }

}