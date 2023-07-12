package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Photo;


public interface PhotoRepository extends CrudRepository<Photo, Long> {

}
