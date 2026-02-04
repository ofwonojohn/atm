package com.atm.atm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for root/home page operations.
 */
@Controller
public class HomeController {

    /**
     * Redirect root path to login page.
     *
     * @return redirect to login
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }
}
