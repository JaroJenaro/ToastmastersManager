package de.iav.frontend.exception;

public class MappingRuntimeException extends RuntimeException{
    public MappingRuntimeException(String mappedObject) {
        super("Mapping nicht erfolgreich" + mappedObject);
    }

}
