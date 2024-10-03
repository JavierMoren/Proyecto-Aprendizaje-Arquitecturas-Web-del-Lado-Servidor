package org.iesalixar.daw2.javiermorenosalas.dao;

import org.iesalixar.daw2.javiermorenosalas.entity.Supermarket;

import java.sql.SQLException;
import java.util.List;


public interface SupermarketDAO {


    List<Supermarket> listAllSupermarkets() throws SQLException;
    void insertSupermarket(Supermarket supermarket) throws SQLException;
    void updateSupermarket(Supermarket supermarket) throws SQLException;
    void deleteSupermarket(int id) throws SQLException;
    Supermarket getSupermarketById(int id) throws SQLException;


}

