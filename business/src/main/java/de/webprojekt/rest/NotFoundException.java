package de.webprojekt.rest;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Long id) {
        super("Could not find todos/ " + id);
    }
    public NotFoundException(String id) {
        super("Could not find users/ " + id);
    }
}