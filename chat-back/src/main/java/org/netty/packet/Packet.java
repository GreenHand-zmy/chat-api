package org.netty.packet;

/**
 * 数据包定为   魔数 -> 版本号 -> 序列化算法 -> 指令 ->数据长度 -> 数据
 *              4        1          1         1        4        n
 */
public interface Packet {
    // 魔数
    int MAGIC_NUMBER = 0x12345678;

    // 协议版本
    byte VERSION = 1;

    /**
     * 获取指令
     *
     * @return
     */
    byte getCommand();
}
