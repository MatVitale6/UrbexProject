package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.PlaceRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlaceService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class PlaceController {

    @Autowired
    private PlaceValidator placeValidator;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CredentialsService credentialsService;


    /* metodo getter per ottenere la form per la creazione di un nuovo place in attesa di approvazione */
    @GetMapping("/formNewPlace/{userID}")
    public String formNewPlace(@PathVariable("userID") Long userID, Model model) {
        model.addAttribute("place", new Place());
        model.addAttribute("credentials", this.credentialsService.getCredentials(userID));

        return "formNewPlace.html";
    }

    /* metodo post per la effettiva creazione di un nuovo place, chiamato quando viene cliccato
     * il button per l'approvazione di un place */
    @PostMapping("/place/{userID}")
    public String createPlace(@Valid @ModelAttribute("place") Place place,@PathVariable("userID") Long userID, BindingResult bindingResult, Model model,
                             @RequestParam("file") MultipartFile[] file) throws IOException {
        this.placeValidator.validate(place, bindingResult);
        if(!bindingResult.hasErrors()) {
            this.placeService.newPlace(place, file, model);
            model.addAttribute("place", place);
            model.addAttribute("credentials", this.credentialsService.getCredentials(userID));
            place.setApproved(true);

            return "place.html";
        }                       
        else {
            /*
            *TODO: aggiungere messaggio di errore */
            return "/formNewPlace.html";
        }
    }

    @PostMapping("/admin/updatePlaceImage/{placeId}")
    public String updateMovieImage(@PathVariable("placeId") Long placeID, @RequestParam("file") MultipartFile[] file) throws IOException {
        this.placeService.updatePlaceImage(placeID, file);

        return "place.html";
    }

    /* Metodo get per l'eliminazione di un place */
    @GetMapping(value ="/deletePlace/{placeID}")
    public String deletePlace(@PathVariable("placeID") Long placeID, Model model) {
        this.placeService.deletePlace(placeID);

        return "redirect:place.html";
    }
    /*
    @PostMapping(value="/rejectPlace/{placeID}")
    public String rejectPlace(@PathVariable("placeID") Long placeID, Model model) {
        this.placeService.rejectPlace(placeID);

        return "redirect:place.html";
    }
    */

    @GetMapping(value="/deletePhoto/{placeID}/{photoID}")
    public String deletePhoto(@PathVariable("placeID") Long placeID, @PathVariable("photoID") Long photoID, Model model) {
        this.placeService.deletePhotoFromPlace(placeID, photoID);

        return "redirect:/admin/formUpdatePlace/"+placeID;
    }

    /* Metodi get per l'aggiornamento delle informazioni sui place */
    @GetMapping(value="/admin/formUpdatePlace/{placeID}")
    public String formUpdatePlace(@PathVariable("placeID") Long placeID, Model model) {
        Place place;
        try {
            place = this.placeService.findPlaceByID(placeID);
            model.addAttribute("place", place);

            return "return admin/formUpdatePlace.html";
        }
        catch (Exception e) {
            return "/resourceNotFound.html";
        }
    }

    /* Metodo post per l'aggiornamento delle informazioni riguardo ad un place */
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
    /*
    @GetMapping(value="/admin/notApprovedPlace")
    public String getNotApprovedPlace(Model model) {
        model.addAttribute("places", this.placeRepository.findAllByApproved(false));

        return "notApprovedPlaces.html";
    }

    /* Metodo getter per ottenere la pagina html con tutti i places presenti nel sito */
    /* 
    @GetMapping("/approvedPlace")
    public String getApprovedPlaces(Model model) {
        model.addAttribute("places", this.placeRepository.findAllByApproved(true));

        return "places.html";
    }
    */

    /*Metodo per ottenere la pagina con tutti i places*/
    @GetMapping("/places")
    public String showPlaces(Model model) {
        model.addAttribute("places", this.placeService.findAllPlace());
        return "/places.html";
    }


    /* Metodo getter per ottenere la pagina del place in dettaglio */
    @GetMapping("/place/{placeID}")
    public String getPlace(@PathVariable("placeID") Long placeID, Model model) {
        Place place;
        try {
            place = placeService.findPlaceByID(placeID);
        }
        catch(Exception e) {
            return "resourceNotFound.html";
        }

        model.addAttribute("place", place);
        model.addAttribute("review", new Review());

        if(this.getCredentials() != null) {
            model.addAttribute("hasReview", this.reviewService.existsReviewByAuthorAndPlace(this.getCredentials().getUser().getId(), placeID));
        }
        return "place.html";
    }

    /* Metodi per la ricerca di un place mediante: indirizzo o regione */
    @GetMapping("/formSearchPlaces")
    public String formSearchPlace() {
        return "formSearchPlace";
    }

    @PostMapping("/searchPlacesByRegion") 
    public String searchPlacesByRegion(Model model, @RequestParam String region) {
        model.addAttribute("places", this.placeRepository.findByRegion(region));

        return "foundMovies.html";
    }

    @PostMapping("/searchPlacesByAddress") 
    public String searchPlacesByAddress(Model model, @RequestParam String address) {
        model.addAttribute("places", this.placeRepository.findByAddress(address));

        return "foundMovies.html";
    }

    


	private Credentials getCredentials() {
		if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			return credentials;
		}
		return null;
	}




}