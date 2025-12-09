package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class RegionDAOImpl implements RegionDAO {
    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Region> listAllRegions() {
        logger.info("Listing all regions from the database.");
        String query = "SELECT r FROM Region r";
        List<Region> regions = entityManager.createQuery(query, Region.class).getResultList();
        logger.info("Retrieved {} regions from the database.", regions.size());
        return regions;
    }

    @Override
    public void insertRegion(Region region) {
        logger.info("Inserting region with code: {} and name: {}",
                region.getCode(), region.getName());
        entityManager.persist(region);
        logger.info("Inserted region with ID: {}", region.getId());
    }

    @Override
    public void updateRegion(Region region) {
        logger.info("Updating region with id: {}", region.getId());
        entityManager.merge(region);
        logger.info("Updated region with id: {}", region.getId());
    }

    @Override
    public void deleteRegion(Long id) {
        logger.info("Deleting region with id: {}", id);
        Region region = entityManager.find(Region.class, id);
        if (region != null) {
            entityManager.remove(region);
            logger.info("Deleted region with id: {}", id);
        } else {
            logger.warn("Region with id: {} not found.", id);
        }
    }

    @Override
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        Region region = entityManager.find(Region.class, id);
        if (region != null) {
            logger.info("Region retrieved: {} - {}", region.getCode(),
                    region.getName());
        } else {
            logger.warn("No region found with id: {}", id);
        }
        return region;
    }

    @Override
    public boolean existsRegionByCode(String code) {
        logger.info("Checking if region with code: {} exists", code);
        String query = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists: {}", code, exists);
        return exists;
    }

    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id) {
        logger.info("Checking if region with code: {} exists excluding id: {}",
                code, id);
        String query = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code AND r.id != :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id,
                exists);
        return exists;
    }
}
