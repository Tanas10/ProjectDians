package mk.finki.ukim.mk.dasproject.web.controller.apicontrolers;


import mk.finki.ukim.mk.dasproject.service.implementation.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/files")
public class FileControllerAPI {

    @Autowired
    private FileService fileService;

    @GetMapping
    public List<String> getFileNames() {
        return fileService.getFileNames();
    }

}
