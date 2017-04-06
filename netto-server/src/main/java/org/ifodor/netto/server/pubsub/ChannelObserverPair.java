package org.ifodor.netto.server.pubsub;

import java.util.Base64;

import org.ifodor.netto.api.Protocol.StreamMessage;

import io.grpc.stub.StreamObserver;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ChannelObserverPair {
  
  private final @NonNull String channel;

  private final @NonNull StreamObserver<StreamMessage> streamObserver;

  public String base64ChannelName() {
    return Base64.getEncoder().encodeToString(getChannel().getBytes());
  }
}
