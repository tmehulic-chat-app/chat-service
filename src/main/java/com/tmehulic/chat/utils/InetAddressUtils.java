package com.tmehulic.chat.utils;

import org.jspecify.annotations.Nullable;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class InetAddressUtils {
    private InetAddressUtils() {}

    @Nullable
    public static String getIpAddress(InetSocketAddress inetSocketAddress) {
        if (inetSocketAddress == null) {
            return null;
        } else if (inetSocketAddress.isUnresolved()) {
            return inetSocketAddress.getHostName();
        } else {
            InetAddress address = inetSocketAddress.getAddress();
            return address instanceof Inet6Address
                    ? "[" + address.getHostAddress() + "]"
                    : address.getHostAddress();
        }
    }
}
