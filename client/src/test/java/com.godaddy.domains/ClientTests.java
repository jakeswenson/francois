package com.godaddy.domains;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ClientTests {
    private long msb;

    @Test
    public void Test() throws Exception {
        final UUID uuid = UUID.randomUUID();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16);
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeLong(uuid.getMostSignificantBits());
        dataOutputStream.writeLong(uuid.getLeastSignificantBits());
        final byte[] uuidBytes = byteArrayOutputStream.toByteArray();

        final String uuidEncoded = Base64.getEncoder().withoutPadding().encodeToString(uuidBytes);
        System.out.println(uuidEncoded);

        final byte[] decode = Base64.getDecoder().decode(uuidEncoded);
        final long lsb = ByteBuffer.wrap(decode).getLong(Long.BYTES);

        System.out.println(new UUID(msb, lsb));
        System.out.println(uuid);

        BigInteger b = new BigInteger(uuidBytes);


        System.out.println(b.toString(62));

    }
}
