package myrpc.netty.message;

import myrpc.netty.message.enums.MessageSerializeType;
import myrpc.netty.message.model.MessageHeader;
import myrpc.netty.message.model.MessageProtocol;
import myrpc.netty.message.model.RpcRequest;
import org.junit.Test;

import java.io.IOException;

public class MessageCodecTest {

    @Test
    public void messageCodecTest() throws IOException {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageFlag(true);
        messageHeader.setTwoWayFlag(false);
        messageHeader.setEventFlag(true);
        messageHeader.setSerializeType(MessageSerializeType.HESSIAN.getCode());
        messageHeader.setResponseStatus((byte)'a');
        messageHeader.setMessageUUId(123456789L);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName("com.aaa.bcd");
        rpcRequest.setMethodName("echo");
        rpcRequest.setParameterClasses(new Class[]{String.class});
        rpcRequest.setParams(new Object[]{"name1"});
        rpcRequest.setReturnClass(String.class);

        MessageProtocol<RpcRequest> messageProtocol = new MessageProtocol<>(messageHeader,rpcRequest);

//        byte[] messageEncodeResult = MessageCodecUtil.messageEncode(messageHeader);
//        Message<RpcRequest> messageDecodeResult = MessageCodecUtil.messageHeaderDecode(messageEncodeResult,RpcRequest.class);

    }
}
