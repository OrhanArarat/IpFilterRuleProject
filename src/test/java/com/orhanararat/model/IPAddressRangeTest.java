package com.orhanararat.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IPAddressRangeTest {

    @ParameterizedTest
    @CsvSource({
            "192.168.1.1, 192.168.1.1, 192.168.1.255, true",
            "192.168.1.0, 192.168.1.1, 192.168.1.255, false",
            "192.168.1.255, 192.168.1.1, 192.168.1.255, true",
            "192.168.2.1, 192.168.1.1, 192.168.1.255, false",
            "10.0.0.1, 10.0.0.0, 10.255.255.255, true",
            "172.16.0.1, 172.16.0.0, 172.31.255.255, true",
            "192.168.1.1, 192.168.1.1, 192.168.1.1, true"
    })
    void isInRange_ShouldReturnExpectedResult(String ip, String startIp, String endIp, boolean expected) throws UnknownHostException {
        assertEquals(expected, IPAddressRange.isInRange(ip, startIp, endIp));
    }

    @Test
    void constructorAndGetters_ShouldWorkCorrectly() {
        IPAddressRange range = new IPAddressRange("192.168.1.1", "192.168.1.255");
        assertEquals("192.168.1.1", range.getStartIp());
        assertEquals("192.168.1.255", range.getEndIp());
    }

    @Test
    void builder_ShouldWorkCorrectly() {
        IPAddressRange range = IPAddressRange.builder()
                .startIp("10.0.0.1")
                .endIp("10.0.0.255")
                .build();
        assertEquals("10.0.0.1", range.getStartIp());
        assertEquals("10.0.0.255", range.getEndIp());
    }
}