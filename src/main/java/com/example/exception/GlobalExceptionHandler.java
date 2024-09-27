package com.example.exception;

import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    // Custom method to log messages into a file
    private void logToFile(String message) {
        FileHandler fileHandler = null;
        try {
        	 fileHandler = new FileHandler("logs/logfile.txt", 0, 1, true); // Append to existing file
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
            logger.log(Level.SEVERE, message); // Log the message
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while logging to file: ", e);
        } finally {
            if (fileHandler != null) {
                fileHandler.close(); // Close the FileHandler to prevent resource leak
            }
        }
    }

    // Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAllExceptions(Exception ex) {
        String message = "Internal server error occurred: " + ex.getMessage();
        logger.log(Level.SEVERE, message, ex); // Log to console
        logToFile(message); // Log to the file
    }

    // Handle 404 Not Found errors
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handle404(NoHandlerFoundException ex) {
        String message = "Requested resource not found: " + ex.getMessage();
        logger.log(Level.WARNING, message, ex); // Log to console
        logToFile(message); // Log to the file
    }

    // Handle specific exceptions like IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequest(IllegalArgumentException ex) {
        String message = "Bad request error occurred: " + ex.getMessage();
        logger.log(Level.WARNING, message, ex); // Log to console
        logToFile(message); // Log to the file
    }
}
