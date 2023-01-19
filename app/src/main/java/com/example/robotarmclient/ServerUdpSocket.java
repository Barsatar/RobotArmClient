package com.example.robotarmclient;

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class ServerUdpSocket implements Runnable {
    private Thread serverUdpThread;
    private DatagramSocket serverUdpSocket;
    private DatagramPacket videoPacket;
    private boolean isWork = true;
    private InetAddress address;
    private final Integer PORT;

    public ServerUdpSocket(String ip, Integer port) {
        this.serverUdpThread = new Thread(this);
        this.serverUdpThread.setPriority(Thread.NORM_PRIORITY);
        this.serverUdpThread.start();
        this.PORT = port;

        try {
            InetAddress address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            this.serverUdpSocket = new DatagramSocket(PORT, address);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        while (isWork) {
            DatagramPacket videoPacket = Scanner();
        }

        this.serverUdpSocket.close();
    }

    public DatagramPacket Scanner() {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            this.serverUdpSocket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String data = new String(packet.getData(), 0, packet.getLength());
        System.out.println(data);

        return packet;
    }

    public void setLoopState(boolean state) {
        isWork = state;
    }

    public DatagramPacket getVideoPacket() {
        return videoPacket;
    }
}