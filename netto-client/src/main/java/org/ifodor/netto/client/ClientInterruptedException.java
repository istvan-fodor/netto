package org.ifodor.netto.client;

public class ClientInterruptedException extends RuntimeException {

  private static final long serialVersionUID = -5550319851335074631L;

  public ClientInterruptedException(Exception e) {
    super(e);
  }

}
