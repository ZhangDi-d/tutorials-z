package org.example.rmi.httpInvokerClient;

import org.example.rmi.Booking;
import org.example.rmi.client.RmiClient;
import org.example.rmi.server.CabBookingService;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

/**
 * @author dizhang
 * @date 2021-05-12
 */
public class HttpInvokerClient {
    @Bean
    @SuppressWarnings("deprecation")
    HttpInvokerProxyFactoryBean service() {
        HttpInvokerProxyFactoryBean rmiProxyFactory = new HttpInvokerProxyFactoryBean();
        rmiProxyFactory.setServiceUrl("http://localhost:8080/CabBookingService/remoting/exporter");
        rmiProxyFactory.setServiceInterface(CabBookingService.class);
        return rmiProxyFactory;
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(HttpInvokerClient.class);
        applicationContext.refresh();
        System.out.println("Http Invoker client start...");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));

        CabBookingService cabBookingService = applicationContext.getBean("service", CabBookingService.class);
        Booking bookingOutcome = cabBookingService.bookRide("Http Invoker Client...");
        System.out.println(bookingOutcome);
    }
}
