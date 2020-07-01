package de.webprojekt.rest;

class NotFoundException extends RuntimeException {

    NotFoundException(Long id) {
        super("Could not find users/notes " + id);
    }
    NotFoundException(String id) {
        super("Could not find users/notes " + id);
    }
}