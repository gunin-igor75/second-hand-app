package ru.skypro.homework.exception_handling;

public class PhotoNotFoundException extends RuntimeException {

    public PhotoNotFoundException() {
    }

    public PhotoNotFoundException(String message) {
        super(message);
    }
}