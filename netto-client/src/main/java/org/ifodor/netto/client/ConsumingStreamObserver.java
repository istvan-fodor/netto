package org.ifodor.netto.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.ifodor.netto.api.Protocol;
import org.ifodor.netto.api.Protocol.Datum;
import org.ifodor.netto.api.Protocol.StreamMessage;
import org.ifodor.netto.api.Protocol.Subscription;

import io.grpc.stub.StreamObserver;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumingStreamObserver implements StreamObserver<Protocol.StreamMessage> {

  private Consumer<byte[]> consumer;

  private List<Subscription> subscriptions;

  private CountDownLatch latch;

  public ConsumingStreamObserver(@NonNull Consumer<byte[]> consumer, CountDownLatch latch) {
    this.latch = latch;
    this.consumer = consumer;
    this.subscriptions = new CopyOnWriteArrayList<>();
  }

  protected List<Subscription> getSubscriptions() {
    return new ArrayList<>(subscriptions);
  }

  @Override
  public void onNext(StreamMessage value) {
    if (value.getMsgCase().equals(StreamMessage.MsgCase.SUBSCRIPTION)) {
      if (latch != null) {
        this.subscriptions.add(value.getSubscription());
        latch.countDown();
      }
    } else {
      onNext(value.getData());
    }
  }

  public void onNext(Datum value) {
    consumer.accept(value.getBody().toByteArray());
  }

  @Override
  public void onError(Throwable t) {
    log.error("Error: ", t);
  }

  @Override
  public void onCompleted() {
    log.info("Completed");
  }
}
