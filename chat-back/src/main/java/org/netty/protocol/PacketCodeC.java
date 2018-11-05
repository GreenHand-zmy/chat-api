package org.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.netty.Serializer.JSONSerializer;
import org.netty.Serializer.Serializer;
import org.netty.Serializer.SerializerAlgorithm;
import org.netty.protocol.command.Command;
import org.netty.protocol.request.*;
import org.netty.protocol.response.CreateGroupResponsePacket;
import org.netty.protocol.response.LoginResponsePacket;
import org.netty.protocol.response.LogoutResponsePacket;
import org.netty.protocol.response.MessageResponsePacket;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {
    private static Map<Byte, Class<? extends Packet>> packetMap;
    private static Map<Byte, Serializer> serializerMap;
    private volatile static PacketCodeC instance;

    private PacketCodeC() {
    }

    public static PacketCodeC instance() {
        if (instance == null) {
            synchronized (PacketCodeC.class) {
                if (instance == null) {
                    instance = new PacketCodeC();
                }
            }
        }
        return instance;
    }

    static {
        packetMap = new HashMap<>();
        packetMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetMap.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetMap.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetMap.put(Command.NOTIFY_RESPONSE, NotifyResponsePacket.class);
        packetMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);

        serializerMap = new HashMap<>();
        serializerMap.put(SerializerAlgorithm.JSON, new JSONSerializer());
    }

    public ByteBuf encode(Packet packet) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(Packet.MAGIC_NUMBER);
        byteBuf.writeByte(Packet.VERSION);
        byteBuf.writeByte(SerializerAlgorithm.JSON);
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 先跳过魔数检查
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 获取序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 获取指令
        byte command = byteBuf.readByte();

        // 获取数据长度
        int dataLength = byteBuf.readInt();

        // 获取数据内容
        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        } else {
            throw new RuntimeException("找不到指令类型为: " + command);
        }
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetMap.get(command);
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    public static void main(String[] args) {
        PacketCodeC codeC = PacketCodeC.instance();

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUsername("zmy");
        loginRequestPacket.setPassword("123");

        ByteBuf byteBuf = codeC.encode(loginRequestPacket);
        Packet packet = codeC.decode(byteBuf);
        System.out.println(packet);
    }
}
