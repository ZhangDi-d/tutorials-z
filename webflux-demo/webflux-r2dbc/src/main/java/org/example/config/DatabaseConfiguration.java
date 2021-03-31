package org.example.config;

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import com.github.jasync.sql.db.mysql.util.URLParser;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @author dizhang
 * @date 2021-03-26
 * <p>
 * 注意 ： https://docs.spring.io/spring-data/r2dbc/docs/1.2.6/reference/html/#new-features  spring 版本 5.3.5 +
 */
@Configuration
@EnableTransactionManagement // 开启事务的支持
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {  //extends AbstractR2dbcConfiguration

    @Value("${spring.r2dbc.url}")
    public String url;


    //=====================================好几种构建ConnectionFactory 的方式，选一种有用的===============================================//
//    @Bean
//    public ConnectionFactory connectionFactory0(R2dbcProperties properties) throws URISyntaxException {
//        var uri = new URI(properties.getUrl());
//        var host = uri.getHost();
//        var port = uri.getPort();
//
//        //var database = uri.getPath().substring(1);
//        var database = uri.toString().substring(uri.toString().lastIndexOf("/") + 1);
//        var configuration = new com.github.jasync.sql.db.Configuration(
//                properties.getUsername(), host, port, properties.getPassword(), database);
//
//
//        return new JasyncConnectionFactory(new MySQLConnectionFactory(configuration));
//    }


    //-------------------------使用 jasync-r2dbc-mysql 依赖--------------------------//
//    @Bean
//    public ConnectionFactory connectionFactory1(R2dbcProperties properties) throws URISyntaxException {
//        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
//                .option(ConnectionFactoryOptions.DRIVER, "pool")
//                .option(ConnectionFactoryOptions.PROTOCOL, "mysql")
//                .option(ConnectionFactoryOptions.HOST, "localhost")
//                .option(ConnectionFactoryOptions.PORT, 3306)
//                .option(ConnectionFactoryOptions.USER, "root")
//                .option(ConnectionFactoryOptions.PASSWORD, "123456")
//                .option(ConnectionFactoryOptions.DATABASE, "webflux-r2dbc")
//                .build();
//
//        return ConnectionFactories.get(options);
//    }

    //https://github.com/joshlong/reactive-mysql-with-jasync-and-r2dbc/blob/master/src/main/java/com/example/rx/BootifulReactiveMySqlApplication.java
    @Override
    public ConnectionFactory connectionFactory() {
        String url = "mysql://root:123456@127.0.0.1:3306/webflux-r2dbc";  //mysql://orders:orders@127.0.0.1:3306/orders
        return new JasyncConnectionFactory(new MySQLConnectionFactory(URLParser.INSTANCE.parseOrDie(url, StandardCharsets.UTF_8)));
    }
//
//    @Bean
//    public ConnectionFactory connectionFactory2(R2dbcProperties properties) throws URISyntaxException {
//        return ConnectionFactories.get(url);
//    }


    @Bean
    public ReactiveTransactionManager transactionManager(R2dbcProperties properties) throws URISyntaxException {
        return new R2dbcTransactionManager(this.connectionFactory());  //this.connectionFactory1(properties) 有效的
    }

}
