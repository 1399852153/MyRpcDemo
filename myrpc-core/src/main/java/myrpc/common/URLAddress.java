package myrpc.common;

import java.util.Objects;

public class URLAddress {

    private final String host;
    private final int port;

    public URLAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        URLAddress that = (URLAddress) o;
        return port == that.port && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return "URLAddress{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
