package it.unisa.diem.exceptions;

/**
 * Eccezione lanciata in caso di errori durante operazioni di cancellazione.
 */
public class DeleteException extends RuntimeException {

  /**
   * Costruisce una nuova DeleteException con un messaggio specifico.
   *
   * @param message Il messaggio descrittivo dell'eccezione.
   */
  public DeleteException(String message) {
    super(message);
  }
}
