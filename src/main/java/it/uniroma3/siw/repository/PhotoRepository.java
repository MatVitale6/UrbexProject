package it.uniroma3.siw.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
	
	public List<Photo> findByAuthor(String author);

}
