package org.iesalixar.daw2.javiermorenosalas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {

    private int id;
    private String name;

    public Supermarket(String name) {
        this.name = name;
    }
}
