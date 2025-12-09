package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao;

import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProvinceDAOImpl implements ProvinceDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public ProvinceDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Province> listAllProvinces() {
        logger.info("Listing all provinces from the database.");
        String sql = "SELECT p.*, r.name AS regionName FROM provinces p JOIN regions r ON p.region_id = r.id";
        List<Province> provinces = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(Province.class));
        logger.info("Retrieved {} provinces from the database.", provinces.size());
        return provinces;
    }

    @Override
    public void insertProvince(Province province) {
        logger.info("Inserting province with code: {} and name: {}", province.getCode(), province.getName());
        String sql = "INSERT INTO provinces (code, name, region_id) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
                province.getCode(),
                province.getName(),
                province.getRegionId()
        );
        logger.info("Inserted province. Rows affected: {}", rowsAffected);
    }

    @Override
    public void updateProvince(Province province) {
        logger.info("Updating province with id: {}", province.getId());
        String sql = "UPDATE provinces SET code = ?, name = ?, region_id = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                province.getCode(),
                province.getName(),
                province.getRegionId(),
                province.getId()
        );
        logger.info("Updated province. Rows affected: {}", rows);
    }

    @Override
    public void deleteProvince(Long id) {
        logger.info("Deleting province with id: {}", id);
        String sql = "DELETE FROM provinces WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        logger.info("Deleted province. Rows affected: {}", rows);
    }

    @Override
    public Province getProvinceById(Long id) {
        logger.info("Retrieving province by id: {}", id);
        String sql = "SELECT p.*, r.name AS regionName " +
                "FROM provinces p " +
                "JOIN regions r ON p.region_id = r.id " +
                "WHERE p.id = ?";
        try {
            Province province = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Province.class),
                    id
            );
            if (province != null) {
                logger.info("Province retrieved: {} - {}", province.getCode(), province.getName());
            }
            return province;
        } catch (Exception e) {
            logger.warn("No province found with id: {}", id);
            return null;
        }
    }

    @Override
    public boolean existsProvinceByCode(String code) {
        logger.info("Checking if province with code {} exists", code);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Province with code {} exists: {}", code, exists);
        return exists;
    }

    @Override
    public boolean existsProvinceByCodeAndNotId(String code, Long id) {
        logger.info("Checking if province with code {} exists excluding id {}", code, id);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Province with code {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }
}
