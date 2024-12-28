package mk.finki.ukim.mk.dasproject.web.controller;

import mk.finki.ukim.mk.dasproject.service.implementation.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final FileService fileService;

    public DashboardController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String dashboard(Model model) {

        List<String> fileNames = fileService.getFileNames();
        model.addAttribute("fileNames", fileNames);
        return "dashboard";
    }
}
