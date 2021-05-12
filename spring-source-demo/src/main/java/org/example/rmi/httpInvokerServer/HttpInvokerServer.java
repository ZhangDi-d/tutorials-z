package org.example.rmi.httpInvokerServer;

import org.example.rmi.server.CabBookingService;
import org.example.rmi.server.CabBookingServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

/**
 * @author dizhang
 * @date 2021-05-12
 *
 *
 * ***** http invoker 需要在web 容器里气启动   *** main不行
 */
public class HttpInvokerServer {

    @Bean
    CabBookingService bookingService() {
        return new CabBookingServiceImpl();
    }

    @Bean
    @SuppressWarnings("deprecation")
    HttpInvokerServiceExporter exporter(CabBookingService implementation) {

        // Expose a service via RMI. Remote obect URL is:
        // rmi://<HOST>:<PORT>/<SERVICE_NAME>
        // 1099 is the default port

        Class<CabBookingService> serviceInterface = CabBookingService.class;
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setServiceInterface(serviceInterface);
        exporter.setService(implementation);
        return exporter;
    }

    public static void main(String[] args) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(HttpInvokerServer.class);
        applicationContext.refresh();
        System.out.println("http invoker server start...");

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));


        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
