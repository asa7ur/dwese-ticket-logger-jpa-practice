package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao;


import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;

import java.sql.SQLException;
import java.util.List;

public interface ProvinceDAO {
    List<Province> listAllProvinces() throws SQLException;

    void insertProvince(Province province) throws SQLException;

    void updateProvince(Province province) throws SQLException;

    void deleteProvince(Long id) throws SQLException;

    Province getProvinceById(Long id) throws SQLException;

    boolean existsProvinceByCode(String code) throws SQLException;

    boolean existsProvinceByCodeAndNotId(String code, Long id) throws SQLException;
}
