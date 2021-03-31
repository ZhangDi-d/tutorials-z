package org.example.config;//package org.org.example.config;

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory;
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory;
import com.github.jasync.sql.db.mysql.util.URLParser;
import io.r2dbc.spi.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @author dizhang
 * @date 2021-03-26
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {
    @NotNull
    @Override
    public ConnectionFactory connectionFactory() {
        String url = "mysql://root:123456@127.0.0.1:3306/webflux-r2dbc";
        return new JasyncConnectionFactory(new MySQLConnectionFactory(URLParser.INSTANCE.parseOrDie(url, StandardCharsets.UTF_8)));
    }

    @Bean
    public ReactiveTransactionManager transactionManager() throws URISyntaxException {
        return new R2dbcTransactionManager(this.connectionFactory());
    }

}
