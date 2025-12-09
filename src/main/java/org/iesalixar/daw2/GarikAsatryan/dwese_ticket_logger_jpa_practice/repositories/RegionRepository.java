package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.repositories;


import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Page<Region> findAll(Pageable pageable);

    Page<Region> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code")
    boolean existsRegionByCode(@Param("code") String code);

    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code AND r.id != :id")
    boolean existsRegionByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

}
