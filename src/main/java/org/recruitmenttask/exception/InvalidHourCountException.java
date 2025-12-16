package org.recruitmenttask.exception;

public class InvalidHourCountException extends RuntimeException {
    public InvalidHourCountException(int hours) {
        super("Invalid Hour Count. Expected between 1 and 6, but provided " + hours);
    }
}
