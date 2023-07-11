package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Place;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Photo;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long> {


    public List<Photo> findAllByPlaceIsNull();

    public List<Photo> findAllByPlaceIsNotNull();
}
