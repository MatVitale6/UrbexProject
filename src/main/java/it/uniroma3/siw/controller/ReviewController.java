package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlaceService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private PlaceService placeService;

	@Autowired
	private CredentialsService credentialsService;



	@GetMapping("/formNewReview/{placeID}/{userID}")
	public String formNewReview(@PathVariable("placeID") Long placeID, @PathVariable("userID") Long userID, Model model) {
		model.addAttribute("review", new Review());
		try {
			model.addAttribute("place", this.placeService.findPlaceByID(placeID));
		}
		catch (Exception e) {
			return "resourceNotFound.html";
		}
		model.addAttribute("credentials", this.credentialsService.getCredentials(userID));

		return "formNewReview.html";
	}

	@PostMapping("/addReview/{placeID}/{userID}")
	public String addReview(@Valid @ModelAttribute("review") Review review,BindingResult bindingResult, @PathVariable("placeID") Long placeID,
								@PathVariable("userID") Long userID, Model model) {
		//this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.reviewService.addReview(review, placeID, userID, model);
			model.addAttribute("review", review);

			return "redirect:/place/"+placeID;
		}
		else {
			return "formNewReview.html";
		}
	}

	@GetMapping("deleteReview/{reviewID}/{placeID}")
	public String deleteReview(@PathVariable("reviewID") Long reviewID, @PathVariable("placeID") Long placeID, Model model) {
		reviewService.deleteReview(reviewID);

		return "redirect:/place/"+placeID;
	}
}