package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.repositories;


import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.SQLException;
import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    @Query("SELECT COUNT(r) > 0 FROM Province r WHERE r.code = :code AND r.id != :id")
    boolean existsProvinceByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Province r WHERE r.code = :code")
    boolean existsProvinceByCode(@Param("code") String code);
}
