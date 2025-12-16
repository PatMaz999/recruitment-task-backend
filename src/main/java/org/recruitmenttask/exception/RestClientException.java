package org.recruitmenttask.exception;

public class RestClientException extends RuntimeException {
    public RestClientException() {super("An error occurred while trying to fetch data from external service.");}
}
