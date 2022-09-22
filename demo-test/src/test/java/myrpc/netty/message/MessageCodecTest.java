package myrpc.netty.message;

import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.RpcRequest;
import myrpc.netty.message.util.MessageCodecUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class MessageCodecTest {

    @Test
    public void messageCodecTest() throws IOException {
        Message<RpcRequest> message = new Message<>();
        message.setMessageFlag(true);
        message.setTwoWayFlag(false);
        message.setEventFlag(true);
        message.setSerializeType(MessageSerializeType.HESSIAN.getCode());
        message.setResponseStatus((byte)'a');
        message.setMessageUUId(123456789L);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName("com.aaa.bcd");
        rpcRequest.setMethodName("echo");
        rpcRequest.setParameterClasses(new Class[]{String.class});
        rpcRequest.setParams(new Object[]{"name1"});
        rpcRequest.setReturnClass(String.class);

        message.setBizData(rpcRequest);

        byte[] messageEncodeResult = MessageCodecUtil.messageEncode(message);
        Message<RpcRequest> messageDecodeResult = MessageCodecUtil.messageDecode(messageEncodeResult,RpcRequest.class);

    }
}
