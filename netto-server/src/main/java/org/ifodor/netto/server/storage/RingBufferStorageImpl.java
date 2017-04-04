package org.ifodor.netto.server.storage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RingBufferStorageImpl implements Storage {

  public RingBufferStorageImpl(int capacity) throws IOException {
    ByteBuffer.allocateDirect(capacity);
  }

  @Override
  public String put(byte[] body) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String put(String id, byte[] body) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte[] retrieve(String id) {
    // TODO Auto-generated method stub
    return null;
  }


}
