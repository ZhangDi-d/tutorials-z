package org.example.rmi;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dizhang
 * @date 2021-05-12
 */
@Data
@AllArgsConstructor
public class Booking implements Serializable {
    private String name;
}
