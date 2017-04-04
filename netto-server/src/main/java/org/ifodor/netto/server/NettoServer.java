package org.ifodor.netto.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettoServer {

  private Server server;

  public NettoServer(int port) throws IOException {
    server = ServerBuilder.forPort(port).addService(new NettoServiceImpl()).build();
  }

  public void start() throws IOException {
    server.start();
  }

  public void awaitTermination() throws InterruptedException {
    server.awaitTermination();
  }

  public void stop() throws InterruptedException {
    server.shutdown();
  }
  

}
