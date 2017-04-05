package org.ifodor.netto.server;

import java.io.IOException;
import java.net.ServerSocket;

import org.ifodor.netto.client.NettoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "server.port=9500", classes = Netto.class)
public class ClientServerTest {

  @Value("${server.port}")
  private int port;

  @Autowired
  private NettoServer server;

  @Test
  public void testUpdate() throws InterruptedException {
    NettoClient client = new NettoClient("localhost", port);
    client.subscribe("helloworld", bytes -> {
      log.info("received message: {}", new String(bytes));
    });
    client.publish("Hello World - 1".getBytes(), "helloworld");
    client.publish("Hello World - 2".getBytes(), "helloworld");
    client.publish("Hello Isti - 1".getBytes(), "helloisti");
    
    Thread.sleep(1000000l);
    
  }
}
