package de.webprojekt.rest;

class NotFoundException extends RuntimeException {

    NotFoundException(Long id) {
        super("Could not find todos/ " + id);
    }
    NotFoundException(String id) {
        super("Could not find users/ " + id);
    }
}