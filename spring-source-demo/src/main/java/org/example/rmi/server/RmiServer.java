package org.example.rmi.server;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

/**
 * @author dizhang
 * @date 2021-05-12
 */
public class RmiServer {

    @Bean
    CabBookingService bookingService() {
        return new CabBookingServiceImpl();
    }

    @Bean
    @SuppressWarnings("deprecation")
    RmiServiceExporter exporter(CabBookingService implementation) {

        // Expose a service via RMI. Remote obect URL is:
        // rmi://<HOST>:<PORT>/<SERVICE_NAME>
        // 1099 is the default port

        Class<CabBookingService> serviceInterface = CabBookingService.class;
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceInterface(serviceInterface);
        exporter.setService(implementation);
        exporter.setServiceName(serviceInterface.getSimpleName());
        exporter.setRegistryPort(1099);
        return exporter;
    }

    public static void main(String[] args) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(RmiServer.class);
        applicationContext.refresh();
        System.out.println("server start...");

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
    }

}