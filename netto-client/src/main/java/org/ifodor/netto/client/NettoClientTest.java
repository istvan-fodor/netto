package org.ifodor.netto.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettoClientTest {

  public static void main(String[] args) {
    NettoClient client = new NettoClient("localhost", 9500);
    client.subscribe("helloworld", bytes -> {
      log.info("received message: {}", new String(bytes));
    });

    client.publish("Hello World - 1".getBytes(), "helloworld");
    client.publish("Hello World - 2".getBytes(), "helloworld");
    client.publish("Hello Isti - 1".getBytes(), "helloisti");
    
    


  }
}
