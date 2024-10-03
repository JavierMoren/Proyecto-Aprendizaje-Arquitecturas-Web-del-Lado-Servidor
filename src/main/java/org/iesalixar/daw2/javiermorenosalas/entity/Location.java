package org.iesalixar.daw2.javiermorenosalas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private int id;
    private String address;
    private String city;
    private Supermarket supermarket;
    private Province province;

    /**
     * Constructor que permite crear una nueva instancia de Location sin el ID.
     *
     * @param address     Direccion de la ubicacion.
     * @param city        Ciudad de la ubicacion.
     * @param supermarket Supermercado asociado a esta ubicacion.
     * @param province    Provincia asociada a esta ubicacion.
     */
    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address;
        this.city = city;
        this.supermarket = supermarket;
        this.province = province;
    }
}