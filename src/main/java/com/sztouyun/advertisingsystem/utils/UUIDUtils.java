package com.sztouyun.advertisingsystem.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtils {
    public static String generateOrderedUUID(){
        String uuid = Generators.timeBasedGenerator(EthernetAddress.fromInterface()).generate().toString().replace("-", "").toUpperCase();
        return uuid.substring(12,16)+uuid.substring(20,32)+uuid.substring(16,20)+uuid.substring(0,12);
    }

    public static String generateBase58UUID(){
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return Base58.encode(byteBuffer.array());
    }
}
