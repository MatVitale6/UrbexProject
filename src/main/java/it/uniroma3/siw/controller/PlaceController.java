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

import it.uniroma3.siw.controller.validator.PlaceValidator;
import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.repository.PlaceRepository;
import it.uniroma3.siw.service.PlaceService;
import jakarta.validation.Valid;

@Controller
public class PlaceController {

    @Autowired
    private PlaceValidator placeValidator;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private PlaceRepository placeRepository;


    /* metodo getter per ottenere la form per la creazione di un nuovo place */
    @GetMapping(value="/admin/formNewPlace")
    public String formNewPlace(Model model) {
        model.addAttribute("place", new Place());

        return "admin/formNewPlace";
    }

    /* metodo post per la effettiva creazione di un nuovo place, chiamato quando viene cliccato
     * il button */
    @PostMapping("/admin/place")
    public String createPlace(@Valid @ModelAttribute("place") Place place, BindingResult bindingResult, Model model,
                             @RequestParam("file") MultipartFile[] file) throws IOException {
        this.placeValidator.validate(place, bindingResult);
        if(!bindingResult.hasErrors()) {
            this.placeService.newPlace(place, file, model);
            model.addAttribute("place", place);

            return "place.html";
        }                       
        else {
            /*
            *TODO: aggiungere messaggio di errore */
            return "admin/formNewPlace";
        }
    }

    @GetMapping(value ="/deletePlace/{placeID}")
    public String deletePlace(@PathVariable("placeID") Long placeID, Model model) {
        this.placeService.deletePlace(placeID);

        return "redirect:place.html";
    }

    /* Metodi get e post per l'aggiornamento delle informazioni sui place */
    @GetMapping(value="/admin/formUpdatePlace/{placeID}")
    public String formUpdatePlace(@PathVariable("placeID") Long placeID, Model model) {
        model.addAttribute("place", placeRepository.findById(placeID).get());

        return "admin/formUpdatePlace.html";
    }

    @PostMapping(value="/admin/formUpdatePlace/{placeID}")
    public String updatePlace(@PathVariable("placeID") Long placeID, @Valid @ModelAttribute("place") Place place, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            this.placeService.updatePlaceDetails(placeID, place);
        }

        return "redirect: /admin/formUpdatePlace" + placeID;
    }

    /* Pagine per l'utente admin per la gestione e visualizzazione dei luoghi */
    @GetMapping(value="admin/indexPlace")
    public String indexPlace() {
        return "admin/indexPlace.html";
    }

    @GetMapping(value="/admin/managePlaces")
    public String managePlaces(Model model) {
        model.addAttribute("place", this.placeRepository.findAll());

        return "admin/managePlaces.html";
    }

    /* Metodo getter per ottenere la pagina html con tutti i places inseriti */
    @GetMapping("/place")
    public String getPlaces(Model model) {
        model.addAttribute("places", this.placeRepository.findAll());

        return "places.html";
    }



}