package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class FrontendHomeController {

    // URL of the backend service (assumed to be on a different port)
    @Value("${backend.url}")
    private String backendUrl;

    @GetMapping("/frontend")
    public String home() {
        return "frontend"; // Return a simple form where the user can enter a URL
    }
    
    @GetMapping("/result")
    public String results() {
        return "result"; // Return a simple form where the user can enter a URL
    }

    @PostMapping("/fetchFile")
    public String fetchFile(@RequestParam("url") String url, Model model) {
        if (!isWhitelistedUrl(url)) {
            model.addAttribute("error", "URL not allowed.");
            return "result";
        }
    	
    	try {
            // Send the user input (URL) to the backend service
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            
            // Add the backend response to the model to display it on the frontend
            model.addAttribute("fileContent", response);
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching the file: " + e.getMessage());
        }
        return "result"; // The result page where the fetched file is displayed
    }
    
    private boolean isWhitelistedUrl(String url) {
        List<String> whitelist = List.of("http://localhost:8081/csrfexample/", "http://localhost:8081/csrfexample/sensitive");

        for (String allowedUrl : whitelist) {
            if (url.equals(allowedUrl)) {
                return true;
            }
        }
        return false;
    }

}
