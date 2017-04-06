package org.ifodor.netto.server;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NettoServer {

  private Server server;
  
  private int port;

  private NettoServiceImpl nettoService;

  @Autowired
  public NettoServer(@Value("${server.port}") int port, NettoServiceImpl nettoService) throws IOException {
    this.port = port;
    this.nettoService = nettoService;
    server = ServerBuilder.forPort(port).addService(nettoService).build();
  }

  @PostConstruct
  public void start() throws IOException {
    log.info("Starting server on port {}", port);
    server.start();
  }

  @PreDestroy
  public void stop() throws InterruptedException {
    nettoService.closeAll();
    server.shutdownNow();
    server.awaitTermination();
  }

}
