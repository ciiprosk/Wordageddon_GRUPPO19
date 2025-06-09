package it.unisa.diem.exceptions;

public class DBException extends Exception {

    public DBException(String message, Throwable cause) {

        super(message);

        cause.printStackTrace();

    }

    public DBException(String message) {
        super(message);
    }
}
