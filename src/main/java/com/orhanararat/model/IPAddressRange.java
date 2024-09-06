package com.orhanararat.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IPAddressRange {
    @NotNull
    private String startIp;
    @NotNull
    private String endIp;

    public static boolean isInRange(String ip, String startIp, String endIp) throws UnknownHostException {
        long ipLong = ipToLong(ip);
        long startLong = ipToLong(startIp);
        long endLong = ipToLong(endIp);
        return ipLong >= startLong && ipLong <= endLong;
    }

    public static long ipToLong(String ip) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ip);
        byte[] octets = address.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }
}