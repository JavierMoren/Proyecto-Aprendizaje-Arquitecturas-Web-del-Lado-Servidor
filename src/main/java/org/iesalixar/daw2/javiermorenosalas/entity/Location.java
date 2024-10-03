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

    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address;
        this.city = city;
        this.supermarket = supermarket;
        this.province = province;
    }
}
