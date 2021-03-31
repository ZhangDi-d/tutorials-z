package org.example.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author dizhang
 * @date 2021-03-31
 */
//@Component
//public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
//
//    @Override
//    public Date convert(LocalDateTime source) {
//        ZoneId zoneId = ZoneId.systemDefault();
//        // Combines this date-time with a time-zone to create a ZonedDateTime.
//        ZonedDateTime zdt = source.atZone(zoneId);
//        return Date.from(zdt.toInstant());
//    }
//}
