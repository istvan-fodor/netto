package org.ifodor.netto.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BrokenBarrierException;

import org.ifodor.netto.client.NettoClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
public class ClientServerTest {

  private int port;

  private NettoServer server;

  @BeforeEach
  public void init() throws IOException, InterruptedException, BrokenBarrierException {
    port = getOpenPort();
    server = new NettoServer(port);
    server.start();
  }

  @AfterEach
  public void stop() throws InterruptedException {
    server.stop();
    server.awaitTermination();
  }

  public int getOpenPort() throws IOException {
    ServerSocket sock = new ServerSocket(0);
    int port = sock.getLocalPort();
    sock.close();
    return port;
  }

  @Test
  public void testUpdate() {
    NettoClient client = new NettoClient("127.0.0.1", port);
    
  }
}
