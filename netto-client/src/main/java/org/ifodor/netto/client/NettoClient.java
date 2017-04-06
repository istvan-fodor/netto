package org.ifodor.netto.client;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.ifodor.netto.api.NettoGrpc;
import org.ifodor.netto.api.NettoGrpc.NettoStub;
import org.ifodor.netto.api.Protocol.Command;
import org.ifodor.netto.api.Protocol.Datum;
import org.ifodor.netto.api.Protocol.PublishEnvelope;
import org.ifodor.netto.api.Protocol.Subscribe;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class NettoClient {

  private ManagedChannel channel;

  private NettoStub client;

  public NettoClient(String hostname, int port) {
    this(hostname, port, null);
  }

  public NettoClient(String hostname, int port, Executor executor) {
    channel = ManagedChannelBuilder.forAddress(hostname, port).usePlaintext(true).build();
    client = NettoGrpc.newStub(channel).withDeadlineAfter(Long.MAX_VALUE, TimeUnit.DAYS);
  }

  public void publish(byte[] body, String... streams) {
    if (streams.length > 0) {
      PublishEnvelope envelope =
          PublishEnvelope.newBuilder().addData(Datum.newBuilder().setBody(ByteString.copyFrom(body)).build())
              .addAllChannels(Arrays.asList(streams)).build();
      client.publish(envelope, NoOpStreamObserver.getInstance());
    }
  }

  public void subscribe(String node, Consumer<byte[]> consumer) {
    CountDownLatch latch = new CountDownLatch(1);
    StreamObserver<Command> listen = client.listen(new ConsumingStreamObserver(consumer, latch));
    listen.onNext(Command.newBuilder().setSubscribe(Subscribe.newBuilder().setChannel(node)).build());
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void unsubscribe(String node, Consumer<byte[]> consumer) {
    ConsumingStreamObserver observer = new ConsumingStreamObserver(consumer, null);
    client.listen(observer);
  }

}
