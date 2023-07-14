package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.PhotographerValidator;
import it.uniroma3.siw.model.Photographer;
import it.uniroma3.siw.repository.PhotographerRepository;
import it.uniroma3.siw.service.PhotographerService;
import jakarta.validation.Valid;

@Controller
public class PhotographerController {
    
    @Autowired
    private PhotographerRepository photographerRepository;
    @Autowired
    private PhotographerService photographerService;
    @Autowired
    private PhotographerValidator photographerValidator;

    @GetMapping(value = "/admin/formNewPhotographer")
    public String formNewPhotographer(Model model) {
        model.addAttribute("photographer", new Photographer());
        return "admin/formNewPhotographer.html";
    }

    @GetMapping(value = "/admin/indexPhotographer")
    public String indexPhotographer() {
        return "admin/indexPhotographer.html";
    }

    @PostMapping("/admin/photographer")
    public String newPhotographer(@Valid @ModelAttribute("photographer") Photographer photographer, Model model, @RequestParam("file") MultipartFile photo, BindingResult bindingResult) throws IOException {
        photographerValidator.validate(photographer, bindingResult);

        if(!bindingResult.hasErrors()) {
            return this.photographerService.newPhotographer(photographer, photo, model);
        }
        else {
            model.addAttribute("messaggioErrore", "Questo fotografo esiste già");
            return "admin/formNewPhotographer.html";
        }
    }

    @GetMapping("/photographer/{photographerID}")
    public String getPhotographer(@PathVariable("photographerID") Long photographerID, Model model) {
        model.addAttribute("photographer", this.photographerRepository.findById(photographerID).get());
        return "photographer.html";
    }

    @GetMapping("/photographer")
    public String getPhotographers(Model model) {
        model.addAttribute("photographers", this.photographerRepository.findAll());
        return "photographers.html";
    }
}
