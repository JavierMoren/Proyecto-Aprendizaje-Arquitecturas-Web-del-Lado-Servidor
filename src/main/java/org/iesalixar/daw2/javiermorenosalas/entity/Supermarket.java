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

    /**
     * Constructor que permite crear una instancia de Supermarket sin el ID.
     *
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name;
    }
}
