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
        List<Province> listProvinces = null;
        listProvinces = provinceRepository.findAll();
        logger.info("Se han cargado {} provincias.", listProvinces.size());
        model.addAttribute("listProvinces", listProvinces);
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
        Optional<Province> province = provinceRepository.findById(id);
        if (province.isPresent()) {
            model.addAttribute("province", province.get());
            model.addAttribute("listRegiones", regionRepository.findAll());
        } else{
            logger.warn("No se encontró la región con ID {}", id);
        }
        return "province-form";
    }

    @PostMapping("/insert")
    public String insertProvince(@ModelAttribute("province") Province province, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        if (provinceRepository.existsProvinceByCode(province.getCode())) {
            logger.warn("El código de la provincia {} ya existe.",
                    province.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe.");
            return "redirect:/provinces/new";
        }
        provinceRepository.save(province);
        logger.info("Provincia {} insertada con éxito.", province.getCode());
        return "redirect:/provinces";
    }

    @PostMapping("/update")
    public String updateProvince(@ModelAttribute("province") Province province, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando provincia con ID {}", province.getId());
        if (provinceRepository.existsProvinceByCodeAndIdNot(province.getCode(), province.getId())) {
            logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
            redirectAttributes.addFlashAttribute("errorMessage", "El código de la provincia ya existe para otra provincia.");
            return "redirect:/provinces/edit?id=" + province.getId();
        }
        provinceRepository.save(province);
        logger.info("Provincia con ID {} actualizada con éxito.", province.getId());
        return "redirect:/provinces";
    }

    @PostMapping("/delete")
    public String deleteProvince(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando Provincia con ID {}", id);
        provinceRepository.deleteById(id);
        logger.info("Provincia con ID {} eliminada con éxito.", id);
        return "redirect:/provinces";
    }
}