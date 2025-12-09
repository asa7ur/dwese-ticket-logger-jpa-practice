package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.repositories;


import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Page<Province> findAll(Pageable pageable);

    Page<Province> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    @Query("SELECT COUNT(r) > 0 FROM Province r WHERE r.code = :code AND r.id != :id")
    boolean existsProvinceByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Province r WHERE r.code = :code")
    boolean existsProvinceByCode(@Param("code") String code);
}
