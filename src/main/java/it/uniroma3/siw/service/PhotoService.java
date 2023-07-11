package it.uniroma3.siw.service;

import it.uniroma3.siw.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

}
