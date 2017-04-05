package org.ifodor.netto.server;

import org.ifodor.netto.api.Protocol.StreamMessage;
import org.ifodor.netto.api.Protocol.Subscription;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionObserverPair {
  
  private Subscription subscription;

  private StreamObserver<StreamMessage> streamObserver;
  
}
