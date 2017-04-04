package org.ifodor.netto.api;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.2.0)",
    comments = "Source: NettoService.proto")
public final class NettoGrpc {

  private NettoGrpc() {}

  public static final String SERVICE_NAME = "org.ifodor.netto.api.Netto";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<org.ifodor.netto.api.Protocol.Command,
      org.ifodor.netto.api.Protocol.StreamMessage> METHOD_LISTEN =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "org.ifodor.netto.api.Netto", "Listen"),
          io.grpc.protobuf.ProtoUtils.marshaller(org.ifodor.netto.api.Protocol.Command.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(org.ifodor.netto.api.Protocol.StreamMessage.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<org.ifodor.netto.api.Protocol.PublishEnvelope,
      org.ifodor.netto.api.NettoService.Empty> METHOD_PUBLISH =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "org.ifodor.netto.api.Netto", "Publish"),
          io.grpc.protobuf.ProtoUtils.marshaller(org.ifodor.netto.api.Protocol.PublishEnvelope.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(org.ifodor.netto.api.NettoService.Empty.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NettoStub newStub(io.grpc.Channel channel) {
    return new NettoStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NettoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NettoBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static NettoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NettoFutureStub(channel);
  }

  /**
   */
  public static abstract class NettoImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<org.ifodor.netto.api.Protocol.Command> listen(
        io.grpc.stub.StreamObserver<org.ifodor.netto.api.Protocol.StreamMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_LISTEN, responseObserver);
    }

    /**
     */
    public void publish(org.ifodor.netto.api.Protocol.PublishEnvelope request,
        io.grpc.stub.StreamObserver<org.ifodor.netto.api.NettoService.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PUBLISH, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_LISTEN,
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.ifodor.netto.api.Protocol.Command,
                org.ifodor.netto.api.Protocol.StreamMessage>(
                  this, METHODID_LISTEN)))
          .addMethod(
            METHOD_PUBLISH,
            asyncUnaryCall(
              new MethodHandlers<
                org.ifodor.netto.api.Protocol.PublishEnvelope,
                org.ifodor.netto.api.NettoService.Empty>(
                  this, METHODID_PUBLISH)))
          .build();
    }
  }

  /**
   */
  public static final class NettoStub extends io.grpc.stub.AbstractStub<NettoStub> {
    private NettoStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NettoStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NettoStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NettoStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<org.ifodor.netto.api.Protocol.Command> listen(
        io.grpc.stub.StreamObserver<org.ifodor.netto.api.Protocol.StreamMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_LISTEN, getCallOptions()), responseObserver);
    }

    /**
     */
    public void publish(org.ifodor.netto.api.Protocol.PublishEnvelope request,
        io.grpc.stub.StreamObserver<org.ifodor.netto.api.NettoService.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PUBLISH, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NettoBlockingStub extends io.grpc.stub.AbstractStub<NettoBlockingStub> {
    private NettoBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NettoBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NettoBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NettoBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.ifodor.netto.api.NettoService.Empty publish(org.ifodor.netto.api.Protocol.PublishEnvelope request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PUBLISH, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NettoFutureStub extends io.grpc.stub.AbstractStub<NettoFutureStub> {
    private NettoFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NettoFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NettoFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NettoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.ifodor.netto.api.NettoService.Empty> publish(
        org.ifodor.netto.api.Protocol.PublishEnvelope request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PUBLISH, getCallOptions()), request);
    }
  }

  private static final int METHODID_PUBLISH = 0;
  private static final int METHODID_LISTEN = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NettoImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NettoImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PUBLISH:
          serviceImpl.publish((org.ifodor.netto.api.Protocol.PublishEnvelope) request,
              (io.grpc.stub.StreamObserver<org.ifodor.netto.api.NettoService.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LISTEN:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.listen(
              (io.grpc.stub.StreamObserver<org.ifodor.netto.api.Protocol.StreamMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class NettoDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.ifodor.netto.api.NettoService.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NettoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NettoDescriptorSupplier())
              .addMethod(METHOD_LISTEN)
              .addMethod(METHOD_PUBLISH)
              .build();
        }
      }
    }
    return result;
  }
}
