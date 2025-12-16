package org.recruitmenttask.exception;

public class ServiceIsUnavailableException extends RuntimeException {
    public ServiceIsUnavailableException() {super("External service is unavailable");}
}
