package org.ifodor.netto.server.storage;

public interface Storage {
  
  public String put(byte[] body);
  
  public String put(String id, byte[] body);
  
  public byte[] retrieve(String id);
  
}
