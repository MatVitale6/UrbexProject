package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewValidator implements Validator {
    @Autowired private ReviewRepository reviewRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Review review = (Review) target;
        if(review.getAuthor() != null && review.getTitle() != null && reviewRepository.existsByAuthorAndPlace(review.getAuthor(), review.getPlace())) {
            errors.reject("review.duplicate");
        }
    }
}
