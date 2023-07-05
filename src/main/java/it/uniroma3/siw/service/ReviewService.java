package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PlaceRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Transactional
    public Review addReview(@ModelAttribute("review") Review review, @PathVariable("placeID") Long placeID,
                            @PathVariable("userID") Long userID, Model model) {
        User author = userRepository.findById(userID).orElse(null);
        Place place = placeRepository.findById(placeID).orElse(null);

        if (!this.reviewRepository.existsByAuthorAndPlace(author, place)) {
            review.setAuthor(author);
            review.setPlace(place);
            author.getWrittens().add(review);   //se vuoi scrivi il metodo "addReview" in User

            review = reviewRepository.save(review);
            placeRepository.save(place);
            userRepository.save(author);

            return review;
        } else {
            return null;
        }
    }

    @Transactional
        public Review deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        review.getPlace().getReviews().remove(review);
        placeRepository.save(review.getPlace());
        review.getAuthor().getWrittens().remove(review);
        userRepository.save(review.getAuthor());
        reviewRepository.delete(review);

        return review;
    }

    public boolean existsReviewByAuthorAndPlace(Long userId, Long placeID) {
        User user = userRepository.findById(userId).orElse(null);
        Place place = placeRepository.findById(placeID).orElse(null);

        return this.reviewRepository.existsByAuthorAndPlace(user, place);
    }

}
