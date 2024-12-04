package mk.finki.ukim.mk.dasproject.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeControler {

    @GetMapping
    public String home() {

        return "home";
    }
}
