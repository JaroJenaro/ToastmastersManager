package de.iav.frontend.exception;

public class DeletingRuntimeException extends RuntimeException{
    public DeletingRuntimeException(String deletingObject) {
        super("Löschen nicht erfolgreich" + deletingObject);
    }

}
