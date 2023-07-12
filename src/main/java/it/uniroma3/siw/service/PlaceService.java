package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PhotoRepository;
import it.uniroma3.siw.repository.PlaceRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CredentialsService credentialsService;

    @Transactional
    public void createNewPlace(Place place) {
        this.placeRepository.save(place);
    }

    @Transactional
    public void updatePlace(Place place) {
        placeRepository.save(place);
    }

    @Transactional
    public Place findPlaceByID(Long id) {
        return this.placeRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Place> findPlaceByAddress(String address) {
        return this.findPlaceByAddress(address);
    }

    @Transactional
    public Iterable<Place> findAllPlace() {
        return this.placeRepository.findAll();
    }

    @Transactional
    public void newPlace(@Valid Place place, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if(!placeRepository.existsByAddressAndName(place.getAddress(), place.getName())) {
            Photo img = new Photo();
            img.setFilename(file.getResource().getFilename());
            img.setData(file.getBytes());
            photoRepository.save(img);
            place.setThumbnail(img);
            this.placeRepository.save(place);
            model.addAttribute("hasReview", this.existsReviewByAuthorAndPlace(this.getCredentials().getUser().getId(), place.getId()));
            model.addAttribute("place", place);
        }
        else {
            model.addAttribute("messaggioErrore", "Questo urbex esiste già");
        }
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

    @Transactional
    public boolean existsReviewByAuthorAndPlace(Long userID, Long placeID) {
        User user = userRepository.findById(userID).orElse(null);
        Place place = placeRepository.findById(placeID).orElse(null);

        return this.reviewRepository.existsByAuthorAndPlace(user, place);
    }

    @Transactional
    public Place deletePlace(Long placeID) {
        Place place = this.placeRepository.findById(placeID).orElse(null);

        /* Ricorda di eliminare tutte le classi che si collegano a place qui */
        for(Photo ph : place.getPhotos()) {
            photoRepository.delete(ph);
        }
        for(Review rvw : place.getReviews()) {
            rvw.getAuthor().getReviews().remove(rvw);
            userRepository.save(rvw.getAuthor());
            reviewRepository.delete(rvw);
        }
        placeRepository.delete(place);

        return place;
    }
    /*
    @Transactional
    public Place rejectPlace(Long placeID) {
        Place place = this.placeRepository.findById(placeID).orElse(null);

        if(place.isApproved() == false) {

            place.getRecommendedBy().getPlacesToApprove().remove(place);
            this.placeRepository.delete(place);
        }
        return place;
    }
    */

    @Transactional
    public void updatePlaceImage(Long placeID, MultipartFile[] file) throws IOException {
        Place place = this.placeRepository.findById(placeID).orElse(null);

        for(MultipartFile f : file) {
            Photo photo = new Photo();
            photo.setFilename(f.getResource().getFilename());
            photo.setData(f.getBytes());
            this.photoRepository.save(photo);
            place.getPhotos().add(photo);
        }
        this.placeRepository.save(place);
    }

    @Transactional
    public Photo deletePhotoFromPlace(Long placeID, Long photoID) {
        Place oldPlace = this.placeRepository.findById(photoID).orElse(null);
        Photo oldPhoto = this.photoRepository.findById(photoID).orElse(null);

        if(oldPlace.getPhotos().size() > 1) {
            oldPlace.getPhotos().remove(oldPhoto);
            this.photoRepository.delete(oldPhoto);
            this.placeRepository.save(oldPlace);
        }
        return oldPhoto;
    }

    @Transactional
    public Place updatePlaceDetails(Long placeID, @Valid Place newPlace) {
        Place oldPlace = this.placeRepository.findById(placeID).orElse(null);
        if(oldPlace != null) {
            oldPlace.setName(newPlace.getName());
            oldPlace.setDescription(newPlace.getDescription());
            placeRepository.save(oldPlace);
        }
        return oldPlace;
    }
}
