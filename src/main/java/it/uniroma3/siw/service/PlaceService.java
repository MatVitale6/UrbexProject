package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.model.Review;
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
    public void newPlace(@Valid Place place, @RequestParam("file") MultipartFile[] file, Model model) throws IOException {
        place.setPhotos(new HashSet<Photo>());

        for(MultipartFile f : file) {
            System.out.println(f.toString());
        }
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
    public Place deletePlace(Long placeID) {
        Place place = this.placeRepository.findById(placeID).orElse(null);

        /* Ricorda di eliminare tutte le classi che si collegano a place qui */
        for(Photo ph : place.getPhotos()) {
            photoRepository.delete(ph);
        }
        for(Review rvw : place.getReviews()) {
            rvw.getAuthor().getWrittens().remove(rvw);
            userRepository.save(rvw.getAuthor());
            reviewRepository.delete(rvw);
        }
        this.placeRepository.delete(place);

        return place;
    }

    @Transactional
    public Place rejectPlace(Long placeID) {
        Place place = this.placeRepository.findById(placeID).orElse(null);

        if(place.isApproved() == false) {

            place.getRecommendedBy().getPlacesToApprove().remove(place);
            this.placeRepository.delete(place);
        }
        return place;
    }

    @Transactional
    public Place updatePlaceDetails(Long placeID, Place newPlace) {
        Place oldPlace = this.placeRepository.findById(placeID).orElse(null);
        
        if(oldPlace != null) {
            /* Magari aggiusta qua gaspa che bisogna accorpare delle classi */
            oldPlace.setName(newPlace.getName());
            oldPlace.setAddress(newPlace.getAddress());
            oldPlace.setAccessibility(newPlace.getAccessibility());
            oldPlace.setRegion(newPlace.getRegion());
            
            oldPlace.setAltitude(newPlace.getAltitude());
            oldPlace.setLatitude(newPlace.getLatitude());
            oldPlace.setLongitude(newPlace.getLongitude());

            oldPlace.setDescription(newPlace.getDescription());
        }
        return oldPlace;
    }

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
}
