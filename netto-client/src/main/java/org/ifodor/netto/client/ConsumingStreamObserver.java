package org.ifodor.netto.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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

  private BlockingQueue<Subscription> blockingQueue;

  private CountDownLatch latch;

  public ConsumingStreamObserver(@NonNull Consumer<byte[]> consumer, CountDownLatch latch) {
    this.latch = latch;
    this.consumer = consumer;
    this.blockingQueue = new ArrayBlockingQueue<>(1);
  }

  protected Subscription getSubscription() {
    try {
      return this.blockingQueue.poll(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onNext(StreamMessage value) {
    if (value.getMsgCase().equals(StreamMessage.MsgCase.SUBSCRIPTION)) {
      if (latch != null) {
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
