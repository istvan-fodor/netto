package org.ifodor.netto.server;

import java.io.IOException;

public class Netto {

  public static void main(String[] args) throws IOException, InterruptedException {
    NettoServer nettoGrpcServer = new NettoServer(9500);
    nettoGrpcServer.start();
    nettoGrpcServer.awaitTermination();
  }
  
}
