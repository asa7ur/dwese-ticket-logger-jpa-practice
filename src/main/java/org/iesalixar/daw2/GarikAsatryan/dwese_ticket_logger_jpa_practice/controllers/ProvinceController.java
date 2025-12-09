package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao.ProvinceDAO;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao.RegionDAO;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("provinces")
public class ProvinceController {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceDAO provinceDAO;
    @Autowired
    private RegionDAO regionDAO;
    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = null;
        try {
            listProvinces = provinceDAO.listAllProvinces();
            logger.info("Se han cargado {} provincias.", listProvinces.size());
        } catch (SQLException e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.province-controller.list.error", null, Locale.getDefault());
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("listProvinces", listProvinces);
        model.addAttribute("activePage", "provinces");
        return "province";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");

        Province province = new Province();
        province.setRegionId(null);
        model.addAttribute("province", province);

        try {
            List<Region> regions = regionDAO.listAllRegions();
            model.addAttribute("regions", regions);
        } catch (SQLException e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.list.error", null, Locale.getDefault());
            model.addAttribute("errorMessage", errorMessage);
        }

        return "province-form";
    }

    @GetMapping("edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la región con ID {}", id);
        Province province = null;

        try {
            province = provinceDAO.getProvinceById(id);
            if (province == null) {
                logger.warn("No se encontró la provincia con ID {}", id);
                String errorMessage = messageSource.getMessage("msg.province-controller.update.error", null, Locale.getDefault());
                model.addAttribute("errorMessage", errorMessage);
            }

            List<Region> regions = regionDAO.listAllRegions();
            model.addAttribute("regions", regions);

        } catch (SQLException e) {
            logger.error("Error al obtener la provincia con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.province-controller.update.error", null, Locale.getDefault());
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("province", province);

        return "province-form";
    }

    @PostMapping("/insert")
    public String insertProvince(
            @Valid @ModelAttribute("province") Province province,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        try {
            if (result.hasErrors()) {
                return "region-form";
            }

            if (provinceDAO.existsProvinceByCode(province.getCode())) {
                logger.warn("El código de la provincia {} ya existe.", province.getCode());
                String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/provinces/new";
            }
            provinceDAO.insertProvince(province);
            logger.info("provincia {} insertada con éxito.", province.getCode());

            String successMessage = messageSource.getMessage("msg.province-controller.insert.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (SQLException e) {
            logger.error("Error al insertar la provincia {}: {}", province.getCode(), e.getMessage());
            String successMessage = messageSource.getMessage("msg.province-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        }
        return "redirect:/provinces";
    }

    @PostMapping("/update")
    public String updateProvince(
            @Valid @ModelAttribute("province") Province province,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {
        logger.info("Actualizando provincia con ID {}", province.getId());

        try {
            if (result.hasErrors()) {
                return "province-form";
            }

            if (provinceDAO.existsProvinceByCodeAndNotId(province.getCode(), province.getId())) {
                logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
                String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/provinces/edit?id=" + province.getId();
            }

            provinceDAO.updateProvince(province);
            logger.info("Provincia con ID {} actualizada con éxito.", province.getId());

            String successMessage = messageSource.getMessage("msg.province-controller.update.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (SQLException e) {
            logger.error("Error al actualizar la provincia con ID {}", province.getId());
            String errorMessage = messageSource.getMessage("msg.province-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/provinces";
    }

    @PostMapping("/delete")
    public String deleteProvince(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {
        logger.info("Eliminando provincia con ID {}", id);

        try {
            provinceDAO.deleteProvince(id);
            logger.info("Provincia co ID {} eliminada con éxito.", id);

            String successMessage = messageSource.getMessage("msg.province-controller.delete.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (SQLException e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.province-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/provinces";
    }
}
