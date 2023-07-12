package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.model.User;
import org.springframework.data.repository.CrudRepository;


import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    public boolean existsByAuthorAndPlace(User author, Place place);

}
