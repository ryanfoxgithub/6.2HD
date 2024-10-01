package com.example.controller;

import com.example.model.User;
import com.example.model.Customer;
import com.example.model.Purchase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    	private final EntityManagerFactory entityManagerFactory;
    	private final List<Customer> customers;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.customers = new ArrayList<>(); // In-memory list to store customers
    }
	
    @RequestMapping("/")
    public String vulnerableWebAppHome() {
        return "home"; 
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("customers", getCustomers());
        return "home"; // View name that renders home page
    }

        @GetMapping("/new-customer")
    public String newCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "new_customer"; // View name for new customer form
    }

    @PostMapping("/save-customer")
    public String saveCustomer(@ModelAttribute Customer customer) {
        // Using EntityManager to persist customer in DB (mocked for tests)
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
        entityManager.close();

        customers.add(customer); // Add to in-memory list (for testing)
        return "redirect:/"; // Redirect to home page
    }

    public List<Customer> getCustomers() {
        return customers; // Returns in-memory list of customers
    }
    
    @GetMapping("/xss")
    public String xssForm() {
        return "xss";  // This returns xss.jsp
    }
    
    @GetMapping("/admin")
    public String adminForm() {
        return "admin";  // This returns xss.jsp
    }
    
    @GetMapping("/guest")
    public String guestForm() {
        return "guest";  // This returns xss.jsp
    }
    
    @GetMapping("/user")
    public String userForm() {
        return "user";  // This returns xss.jsp
    }
    
    @GetMapping("/loginhome")
    public String loginHome(HttpSession session, Model model) {
        // Retrieve the CSRF token from the session if it exists
        String csrfToken = (String) session.getAttribute("csrfToken");

        // If there's no token in the session, generate a new one
        if (csrfToken == null) {
            csrfToken = CSRFGen.generateCsrfToken(session);
            session.setAttribute("csrfToken", csrfToken);
        }

        model.addAttribute("csrfToken", csrfToken); // Send the token to the form
        return "loginhome";
    }
    
    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        // Retrieve the CSRF token from the session if it exists
        String csrfToken = (String) session.getAttribute("csrfToken");

        // If there's no token in the session, generate a new one
        if (csrfToken == null) {
            csrfToken = CSRFGen.generateCsrfToken(session);
            session.setAttribute("csrfToken", csrfToken);
        }

        model.addAttribute("csrfToken", csrfToken); // Send the token to the form
        return "login";
    }

    @PostMapping("/xss")
    public String xssVulnerable(@RequestParam("input") String input, Model model) {
        model.addAttribute("output", input); // XSS vulnerability: The input is directly shown without sanitization
        return "xss";
    }
    
    @GetMapping("/upload")
    public String uploadForm() {
    	return "upload";  // Returns the upload.jsp
    }
    
    // Handle file upload
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        String fileName = file.getOriginalFilename();
        String fileContent = "";
        
        // Validate file type (e.g., only allow .txt files)
        if (!fileName.toLowerCase().endsWith(".txt")) {
            model.addAttribute("message", "Invalid file type. Only .txt files are allowed.");
            return "upload";
        }
        
        try {
            // Read file content into a string
            fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add file information to the model
        model.addAttribute("fileName", fileName);
        model.addAttribute("fileContent", fileContent);
        return "upload"; // return to the upload page to display the file
    }
    
    @GetMapping("/updateProfile")
    public String showLoginPage(HttpSession session, Model model) {
        // Retrieve the CSRF token from the session if it exists
        String csrfToken = (String) session.getAttribute("csrfToken");

        // If there's no token in the session, generate a new one
        if (csrfToken == null) {
            csrfToken = CSRFGen.generateCsrfToken(session);
            session.setAttribute("csrfToken", csrfToken);
        }

        model.addAttribute("csrfToken", csrfToken); // Send the token to the form
        
        return "updateProfile";
    }

    @PostMapping("/updateProfile")
    public String loginUsingHQL(
        HttpSession session,
        HttpServletRequest request,
        @RequestParam("username") String username, 
        @RequestParam("csrfToken") String csrfToken, 
        Model model
    ) {
    	
    	String sessionCsrfToken = (String) session.getAttribute("csrfToken");
        // Validate the CSRF token
        if (sessionCsrfToken == null || !sessionCsrfToken.equals(csrfToken)) {
            model.addAttribute("error", "Invalid CSRF token");
            // Do not regenerate the token immediately; instead, re-use the old one
            model.addAttribute("csrfToken", session.getAttribute("csrfToken"));
            return "updateProfile";
        }

        // Validate the Referrer header
        if (!CSRFGen.validateReferrerHeader(request)) {
            model.addAttribute("error", "Invalid referrer header");
            model.addAttribute("csrfToken", session.getAttribute("csrfToken"));
            return "updateProfile";
        }
        
        // Re-use the same CSRF token; don't regenerate unless necessary
        model.addAttribute("csrfToken", session.getAttribute("csrfToken"));
        model.addAttribute("message", username);
        return "updateProfile";
    }

    
    @GetMapping("/CSRF")
    public String CSRF() {
        return "CSRF"; // Return a CSRF page
    }
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Controller-level exception handler
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        // Log the exception
        logger.error("IllegalArgumentException caught in HomeController: ", ex);
        // Display error message in the view
        model.addAttribute("error", "An error occurred: " + ex.getMessage());
        return "error"; // Redirect to an error page
    }

    @GetMapping("/sql")
    public String sqlResult() {
        return "sql"; // Return a login page
    }

    @PostMapping("/sql")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        String message;

        EntityManager entityManager = null;

        try {
            // Get an EntityManager from the factory
            entityManager = entityManagerFactory.createEntityManager();

            // Begin a transaction (not strictly needed for a read query)
            entityManager.getTransaction().begin();

            // Create a query using HQL (JPQL in JPA terms)
            String hql = "FROM User u WHERE u.username = :username AND u.password = :password";
            TypedQuery<User> query = entityManager.createQuery(hql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            // Fetch the result
            User user;
            try {
                user = query.getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                // This block handles when the username/password doesn't match any records
                message = "Login failed! Incorrect username or password.";
                model.addAttribute("message", message);
                return "sql"; // Return login page with failure message
            }

            if (user != null) {
                message = "Login successful!";
            } else {
                message = "Login failed!";
            }

            // Commit transaction (for read-only queries, this can be skipped)
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            // Handle exceptions such as database errors or any other issue
            message = "An error occurred during login.";
            e.printStackTrace(); // Log the error for debugging
        } finally {
            // Close the entity manager to release resources
            if (entityManager != null) {
                entityManager.close();
            }
        }

        model.addAttribute("message", message);
        return "sql"; // The view name for displaying login results
    }
    
    @GetMapping("/aggregate")
    public String aggregateResult() {
        return "aggregate"; // Return an aggregate page
    }

    @PostMapping("/aggregate")
    public String handleAggregation(@RequestParam(value = "first_name", required = false) String firstName,
                                    @RequestParam(value = "last_name", required = false) String lastName,
                                    @RequestParam(value = "email", required = false) String email,
                                    Model model) {
        String message;
        EntityManager entityManager = null;

        try {
            // Ensure all required fields are filled
            if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                email == null || email.isEmpty()) {
                model.addAttribute("message", "Please provide your first name, last name, and email to see product history.");
                return "aggregate";
            }

            // Get an EntityManager from the factory
            entityManager = entityManagerFactory.createEntityManager();

            // Begin a transaction
            entityManager.getTransaction().begin();

            // Create an HQL query to check for a valid customer based on the input fields
            String hql = "FROM Customer c WHERE c.firstName = :firstName AND c.lastName = :lastName AND c.email = :email";
            TypedQuery<Customer> query = entityManager.createQuery(hql, Customer.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            query.setParameter("email", email);

            // Fetch the customer result
            Customer customer;
            try {
                customer = query.getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                // This block handles when no customer is found for the given details
                message = "No matching records found for the provided details.";
                model.addAttribute("message", message);
                return "aggregate"; // Return aggregate page with failure message
            }

            // If customer exists, fetch their purchase history using HQL
            if (customer != null) {
                String purchaseHQL = "SELECT p.item FROM Purchase p WHERE p.customer.id = :customerId";
                TypedQuery<String> purchaseQuery = entityManager.createQuery(purchaseHQL, String.class);
                purchaseQuery.setParameter("customerId", customer.getId());

                // Fetch the purchase history as a list
                List<String> purchaseHistory = purchaseQuery.getResultList();
                model.addAttribute("purchaseHistory", purchaseHistory);
                message = "Purchase history fetched successfully.";
            } else {
                message = "No matching records found for the provided details.";
            }

            // Commit transaction
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            // Handle any exceptions such as database errors
            message = "An error occurred while fetching product history.";
            e.printStackTrace(); // Log the error for debugging
        } finally {
            // Close the entity manager to release resources
            if (entityManager != null) {
                entityManager.close();
            }
        }

        model.addAttribute("message", message);
        return "aggregate"; // The view name for displaying aggregated information
    }
    
    @GetMapping("/example")
    public String example() throws IllegalArgumentException {
        throw new IllegalArgumentException("This is an example of a controller-specific error.");
    }
    
    @PostMapping("/checkDeleteAccount")
    public ModelAndView checkDeleteAccount(@RequestParam("question") String question, HttpSession session, Model model) {
        // Get the currently logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();  // This gets the current user's username
        
        // Retrieve the CSRF token from the session if it exists
        String csrfToken = (String) session.getAttribute("csrfToken");

        // If there's no token in the session, generate a new one
        if (csrfToken == null) {
        	return new ModelAndView("/error");
        }
        
        else if (question.equalsIgnoreCase("yes")) {
            ModelAndView modelAndView = new ModelAndView("/deletedAccount");
            modelAndView.addObject("username", loggedInUsername); // Pass the username to the "deletedAccount" view
            return modelAndView;
        }
        return new ModelAndView("/loginhome");
    }

    @GetMapping("/deletedAccount")
    public ModelAndView deletedAccount(@RequestParam(value = "username", required = false) String username, HttpSession session, Model model) {
        ModelAndView modelAndView = new ModelAndView("deletedAccount");
        modelAndView.addObject("username", username);  // Add the username to the view
        
        // Retrieve the CSRF token from the session if it exists
        String csrfToken = (String) session.getAttribute("csrfToken");

        // If there's no token in the session, generate a new one
        if (csrfToken == null) {
        	return new ModelAndView("/error");
        }

        model.addAttribute("csrfToken", csrfToken); // Send the token to the form
        
        return modelAndView;
    }
}
