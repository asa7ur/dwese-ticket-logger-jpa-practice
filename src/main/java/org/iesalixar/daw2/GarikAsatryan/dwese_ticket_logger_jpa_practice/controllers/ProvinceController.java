package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.controllers;

import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.repositories.ProvinceRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.repositories.RegionRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/provinces")

public class ProvinceController {
    private static final Logger logger =
            LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceRepository provinceRepository;


    @Autowired
    private RegionRepository regionRepository;

    @GetMapping
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvincias = null;
        listProvincias = provinceRepository.findAll();
        logger.info("Se han cargado {} provincias.", listProvincias.size());
        model.addAttribute("listProvincias", listProvincias);
        return "/province";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");
        model.addAttribute("provincia", new Province());
        model.addAttribute("listRegiones", regionRepository.findAll());
        return "province-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para provincia {}", id);
        Optional<Province> provincia = provinceRepository.findById(id);
        if (provincia.isPresent()) {
            model.addAttribute("province", provincia.get());
            model.addAttribute("listRegiones", regionRepository.findAll());
        } else{
            logger.warn("No se encontró la región con ID {}", id);
        }
        return "province-form";
    }

    @PostMapping("/insert")
    public String insertProvincia(@ModelAttribute("provincia") Province provincia, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva provincia con código {}", provincia.getCode());
        if (provinceRepository.existsProvinceByCode(provincia.getCode())) {
            logger.warn("El código de la provincia {} ya existe.",
                    provincia.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe.");
            return "redirect:/provinces/new";
        }
        provinceRepository.save(provincia);
        logger.info("Provincia {} insertada con éxito.", provincia.getCode());
        return "redirect:/provinces";
    }

    @PostMapping("/update")
    public String updateProvincia(@ModelAttribute("provincia") Province provincia, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando provincia con ID {}", provincia.getId());
        if (provinceRepository.existsProvinceByCodeAndIdNot(provincia.getCode(), provincia.getId())) {
            logger.warn("El código de la provincia {} ya existe para otra provincia.", provincia.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe para otra provincia.");
            return "redirect:/provinces/edit?id=" + provincia.getId();
        }
        provinceRepository.save(provincia);
        logger.info("Provincia con ID {} actualizada con éxito.", provincia.getId());
        return "redirect:/provinces";
    }

    @PostMapping("/delete")
    public String deleteProvincia(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando Provincia con ID {}", id);
        provinceRepository.deleteById(id);
        logger.info("Provincia con ID {} eliminada con éxito.", id);
        return "redirect:/provinces";
    }
}