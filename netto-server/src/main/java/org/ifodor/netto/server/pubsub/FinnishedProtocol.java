package org.ifodor.netto.server.pubsub;

import lombok.Builder;
import lombok.Data;

public abstract class FinnishedProtocol {

  public static class CompleteAll {

    private static final CompleteAll instance = new CompleteAll();

    private CompleteAll() {}
    
    public static CompleteAll getInstance() {
      return instance;
    }


  }

  @Data
  @Builder
  public static class CompletedAll {

    private final String channel;

    private final int numberOfObserversClosed;

  }
}
