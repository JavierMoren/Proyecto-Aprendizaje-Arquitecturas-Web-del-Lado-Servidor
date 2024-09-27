package org.iesalixar.daw2.javiermorenosalas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Province` representa una entidad que modela una provincia dentro de la base de datos.
 * Contiene cuatro campos: `id`, `code`, `name` y `region`.
 *
 * - `id` es el identificador único de la provincia.
 * - `code` es un código asociado a la provincia.
 * - `name` es el nombre de la provincia.
 * - `region` es una referencia a la región a la que pertenece la provincia.
 *
 * Las anotaciones de Lombok se utilizan para evitar la necesidad de escribir manualmente los
 * métodos getters, setters, constructores, entre otros, reduciendo así la cantidad de código repetitivo.
 */
@Data  // Genera automáticamente getters, setters, `equals()`, `hashCode()` y `toString()`.
@NoArgsConstructor  // Genera un constructor sin parámetros.
@AllArgsConstructor  // Genera un constructor que toma todos los parámetros (id, code, name, region).
public class Province {

    // Campo que almacena el identificador único de la provincia. Suele ser clave primaria autogenerada.
    private int id;

    // Campo que almacena el código de la provincia. Ejemplo: "GR" para Granada.
    private String code;

    // Campo que almacena el nombre completo de la provincia, como "Granada".
    private String name;

    // Campo que almacena el objeto Region al que pertenece esta provincia.
    private Region region;

    /**
     * Constructor que permite crear una instancia de la clase `Province` sin el campo `id`.
     * Útil para crear una provincia antes de insertar sus datos en la base de datos,
     * donde el `id` es normalmente autogenerado.
     *
     * @param code Código de la provincia.
     * @param name Nombre de la provincia.
     */
    public Province(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
