package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.dao.RegionDAO;
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
@RequestMapping("/regions")
public class RegionController {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionDAO regionDAO;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listRegions(Model model) {
        logger.info("Solicitando la lista de todas las regiones...");
        List<Region> listRegions = null;

        try {
            listRegions = regionDAO.listAllRegions();
            logger.info("Se han cargado {} regiones.", listRegions.size());
        } catch (SQLException e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.list.error", null, Locale.getDefault());
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("listRegions", listRegions);
        model.addAttribute("activePage", "regions");
        return "region";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva región.");
        model.addAttribute("region", new Region());
        return "region-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la región con ID {}", id);
        Region region = null;

        try {
            region = regionDAO.getRegionById(id);
            if (region == null) {
                logger.warn("No se encontró la región con ID {}", id);
                String errorMessage = messageSource.getMessage("msg.region-controller.update.error", null, Locale.getDefault());
                model.addAttribute("errorMessage", errorMessage);
            }

        } catch (SQLException e) {
            logger.error("Error al obtener la región con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.error", null, Locale.getDefault());
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("region", region);
        return "region-form";
    }

    @PostMapping("/insert")
    public String insertRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Insertando nueva región con código {}", region.getCode());

        try {
            if (result.hasErrors()) {
                return "region-form";
            }

            if (regionDAO.existsRegionByCode(region.getCode())) {
                logger.warn("El código de la región {} ya existe.", region.getCode());
                String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/regions/new";
            }

            regionDAO.insertRegion(region);
            logger.info("Región {} insertada con éxito.", region.getCode());

            String successMessage = messageSource.getMessage("msg.region-controller.insert.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (SQLException e) {
            logger.error("Error al insertar la región {}: {}", region.getCode(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/regions";
    }

    @PostMapping("/update")
    public String updateRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Actualizando región con ID {}", region.getId());

        try {
            if (result.hasErrors()) {
                return "region-form";
            }

            if (regionDAO.existsRegionByCodeAndNotId(region.getCode(), region.getId())) {
                logger.warn("El código {} ya existe para otra región.", region.getCode());
                String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/regions/edit?id=" + region.getId();
            }

            regionDAO.updateRegion(region);
            logger.info("Región con ID {} actualizada con éxito.", region.getId());

            String successMessage = messageSource.getMessage("msg.region-controller.update.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (SQLException e) {
            logger.error("Error al actualizar la región con ID {}: {}", region.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/regions";
    }

    @PostMapping("/delete")
    public String deleteRegion(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {
        logger.info("Eliminando región con ID {}", id);

        try {
            regionDAO.deleteRegion(id);
            logger.info("Región con ID {} eliminada con éxito.", id);

            String successMessage = messageSource.getMessage("msg.region-controller.delete.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (SQLException e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/regions";
    }
}
