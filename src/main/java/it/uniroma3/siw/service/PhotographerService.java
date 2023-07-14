package it.uniroma3.siw.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Photo;
import it.uniroma3.siw.model.Photographer;
import it.uniroma3.siw.repository.PhotoRepository;
import it.uniroma3.siw.repository.PhotographerRepository;
import jakarta.transaction.Transactional;

@Service
public class PhotographerService {
    
    @Autowired
    private PhotographerRepository photographerRepository;
    
    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public String newPhotographer(Photographer photographer, @RequestParam("file") MultipartFile photo, Model model) throws IOException {
        if(!photographerRepository.existsByNameAndSurname(photographer.getName(), photographer.getSurname())) {
            Photo ph = new Photo();
            ph.setFilename(photo.getResource().getFilename());
            ph.setData((photo.getBytes()));
            photoRepository.save(ph);
            photographer.setPhotographerPhoto(ph);
            this.photographerRepository.save(photographer);
            model.addAttribute("photographer", photographer);

            return "photographer.html";
        }
        else {
            model.addAttribute("messaggioErrore", "Questo fotografo esiste già");
            return "admin/formNewPhotographer.html";
        }
    }
}
