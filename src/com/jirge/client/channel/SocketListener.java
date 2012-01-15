package com.jirge.client.channel;

public interface SocketListener {
    void onOpen();
    void onMessage(String message);
}
