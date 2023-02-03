package com.example.robotarmclient;

import java.io.IOException;
import java.net.*;

public class UDP implements Runnable {
    private DatagramSocket UDPsocket;
    private int PORT;
    private String IP;

    public UDP(String ip, int port) {
        PORT = port;
        IP = ip;

        Thread thread = new Thread(this);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }

    @Override
    public void run() {
        try {
            UDPsocket = new DatagramSocket(this.PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        InetAddress IPAddress = null;
        try {
            IPAddress = InetAddress.getByName(this.IP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}