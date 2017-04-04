package org.ifodor.netto.client;

import io.grpc.stub.StreamObserver;

public class NoOpStreamObserver<T> implements StreamObserver<T> {

  private static final NoOpStreamObserver INSTANCE = new NoOpStreamObserver<>();

  public static <T> NoOpStreamObserver<T> getInstance() {
    return INSTANCE;
  }

  @Override
  public void onNext(T value) {}

  @Override
  public void onError(Throwable t) {}

  @Override
  public void onCompleted() {}

}
