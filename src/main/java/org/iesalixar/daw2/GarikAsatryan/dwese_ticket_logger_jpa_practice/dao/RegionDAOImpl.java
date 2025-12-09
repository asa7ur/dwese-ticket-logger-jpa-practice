package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao;

import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionDAOImpl implements RegionDAO {
    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public RegionDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Region> listAllRegions() {
        logger.info("Listing all regions from the database.");
        String sql = "SELECT * FROM regions";
        List<Region> regions = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(Region.class));
        logger.info("Retrieved {} regions from the database.", regions.size());
        return regions;
    }

    @Override
    public void insertRegion(Region region) {
        logger.info("Inserting region with code: {} and name: {}", region.getCode(), region.getName());
        String sql = "INSERT INTO regions (code, name) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql,
                region.getCode(),
                region.getName());
        logger.info("Inserted region. Rows affected: {}", rowsAffected);
    }

    @Override
    public void updateRegion(Region region) {
        logger.info("Updating region with id: {}", region.getId());
        String sql = "UPDATE regions SET code = ?, name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                region.getCode(),
                region.getName(),
                region.getId());
        logger.info("Updated region. Rows affected: {}", rowsAffected);
    }

    @Override
    public void deleteRegion(Long id) {
        logger.info("Deleting region with id: {}", id);
        String sql = "DELETE FROM regions WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted region. Rows affected: {}", rowsAffected);
    }

    @Override
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        String sql = "SELECT * FROM regions WHERE id = ?";
        try {
            Region region = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Region.class),
                    id);
            if (region != null) {
                logger.info("Region retrieved: {} - {}", region.getCode(), region.getName());
            }
            return region;
        } catch (Exception e) {
            logger.warn("No region found with id: {}", id);
            return null;
        }
    }

    @Override
    public boolean existsRegionByCode(String code) {
        logger.info("Checking if region with code: {} exists", code);
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";
        Integer count = jdbcTemplate.queryForObject(sql,
                Integer.class,
                code.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists: {}", code, exists);
        return exists;
    }

    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id) {
        logger.info("Checking if region with code: {} exists excluding id: {}", code, id);
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql,
                Integer.class,
                code.toUpperCase(),
                id);
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id, exists);
        return exists;
    }
}
