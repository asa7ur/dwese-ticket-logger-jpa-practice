package org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.controllers;

import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.repositories.RegionRepository;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.entities.Region;
import jakarta.validation.Valid;
import org.iesalixar.daw2.GarikAsatryan.dwese_ticket_logger_jpa_practice.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/regions")
public class RegionController {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    public int currentPage = 1;
    public String sort = "idAsc";
    public String search = "";

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping()
    public String listRegions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort, Model model) {
        logger.info("Solicitando la lista de todas las regiones..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Region> regions;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            regions = regionRepository.findByNameContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) regionRepository.countByNameContainingIgnoreCase(search) / 5);
        } else {
            regions = regionRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) regionRepository.count() / 5);
        }
        logger.info("Se han cargado {} regiones.", regions.toList().size());
        model.addAttribute("listRegions", regions.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
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
        Optional<Region> regionOpt = regionRepository.findById(id);
        if (regionOpt.isPresent()) {
            model.addAttribute("region", regionOpt.get());
        } else{
            logger.warn("No se encontró la región con ID {}", id);
        }
        return "region-form";
    }

    @PostMapping("/insert")
    public String insertRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            @RequestParam("imageFile")
            MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Locale locale) {
        logger.info("Insertando nueva región con código {}", region.getCode());

        if (result.hasErrors()) {
            return "region-form";
        }

        if (regionRepository.existsRegionByCode(region.getCode())) {
            logger.warn("El código de la región {} ya existe.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/new";
        }

        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                region.setImage(fileName);
            }
        }
        regionRepository.save(region);
        logger.info("Región {} insertada con éxito.", region.getCode());
        return "redirect:/regions";
    }

    @PostMapping("/update")
    public String updateRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Locale locale) {
        logger.info("Actualizando región con ID {}", region.getId());
        if (result.hasErrors()) {
            return "region-form";
        }

        if (regionRepository.existsRegionByCodeAndIdNot(region.getCode(), region.getId())) {
            logger.warn("El código de la región {} ya existe para otra región.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/edit?id=" + region.getId();
        }

        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                region.setImage(fileName);
            }
        }

        regionRepository.save(region);
        logger.info("Región con ID {} actualizada con éxito.", region.getId());
        return "redirect:/regions";
    }


    @PostMapping("/delete")
    public String deleteRegion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando región con ID {}", id);
        regionRepository.deleteById(id);
        logger.info("Región con ID {} eliminada con éxito.", id);
        return "redirect:/regions";
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "nameAsc" -> Sort.by("name").ascending();
            case "nameDesc" -> Sort.by("name").descending();
            case "codeAsc" -> Sort.by("code").ascending();
            case "codeDesc" -> Sort.by("code").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
