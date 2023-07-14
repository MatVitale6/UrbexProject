package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Photographer;
import it.uniroma3.siw.repository.PhotographerRepository;


@Component
public class PhotographerValidator implements Validator {
    
    @Autowired
    private PhotographerRepository photographerRepository; 

    @Override
	public void validate(Object o, Errors errors) {
		Photographer photographer = (Photographer)o;
		if (photographer.getName()!=null && photographer.getSurname()!=null 
				&& photographerRepository.existsByNameAndSurname(photographer.getName(), photographer.getSurname())) {
			errors.reject("photographer.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Photographer.class.equals(aClass);
	}
}
