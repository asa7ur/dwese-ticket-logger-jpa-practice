package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao;

import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
@Repository
@Transactional
public class ProvinceDAOImpl implements ProvinceDAO {
    private static final Logger logger =
            LoggerFactory.getLogger(ProvinceDAOImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Province> listAllProvinces() {
        logger.info("Listing all provinces from the database.");
        String query = "SELECT p FROM Province p JOIN FETCH p.region";
        List<Province> provinces = entityManager.createQuery(query,
                Province.class).getResultList();
        logger.info("Retrieved {} provinces from the database.",
                provinces.size());
        return provinces;
    }

    @Override
    public void insertProvince(Province province) {
        logger.info("Inserting province with code: {} and name: {}",
                province.getCode(), province.getName());
        entityManager.persist(province);
        logger.info("Inserted province with ID: {}", province.getId());
    }

    @Override
    public void updateProvince(Province province) {
        logger.info("Updating province with id: {}", province.getId());
        entityManager.merge(province);
        logger.info("Updated province with id: {}", province.getId());
    }

    @Override
    public void deleteProvince(Long id) {
        logger.info("Deleting province with id: {}", id);
        Province province = entityManager.find(Province.class, id);
        if (province != null) {
            entityManager.remove(province);
            logger.info("Deleted province with id: {}", id);
        } else {
            logger.warn("Province with id: {} not found.", id);
        }
    }

    @Override
    public Province getProvinceById(Long id) {
        logger.info("Retrieving province by id: {}", id);
        Province province = entityManager.find(Province.class, id);
        if (province != null) {
            logger.info("Province retrieved: {} - {}", province.getCode(),
                    province.getName());
        } else {
            logger.warn("No province found with id: {}", id);
        }
        return province;
    }

    @Override
    public boolean existsProvinceByCode(String code) {
        logger.info("Checking if province with code: {} exists", code);
        String query = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists: {}", code, exists);
        return exists;
    }

    @Override
    public boolean existsProvinceByCodeAndNotId(String code, Long id) {
        logger.info("Checking if province with code: {} exists excluding id: {}", code, id);
        String query = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code AND p.id != :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Province with code: {} exists excluding id {}: {}", code,
                id, exists);
        return exists;
    }
}