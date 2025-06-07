package it.unisa.diem.exceptions;

public class DBException extends RuntimeException {

    public DBException(String message, Throwable cause) {

        super(message);

    }
}
