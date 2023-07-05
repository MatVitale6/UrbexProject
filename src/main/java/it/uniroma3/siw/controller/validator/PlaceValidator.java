package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Place;
import it.uniroma3.siw.repository.PlaceRepository;

@Component
public class PlaceValidator implements Validator {
    
    @Autowired
    private PlaceRepository placeRepository; 


    @Override
	public void validate(Object o, Errors errors) {
		Place place = (Place)o;
		if (place.getAddress()!=null && place.getName()!=null 
				&& placeRepository.existsByAddressAndName(place.getAddress(), place.getName())) {
			errors.reject("place.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Place.class.equals(aClass);
	}
}
