package com.example.robotarmclient;

import java.io.IOException;
import java.net.*;

public class UDPSocket implements Runnable {
    private final String __ip__;
    private final int __port__;
    private final Thread __UDPSocketThread__;
    private DatagramSocket __socket__;
    InetAddress __address__;

    public UDPSocket(String ip, int port) {
        this.__ip__ = ip;
        this.__port__ = port;

        this.__UDPSocketThread__ = new Thread(this);
        this.__UDPSocketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__UDPSocketThread__.start();
    }

    @Override
    public void run() {
        try {
            this.__socket__ = new DatagramSocket(this.__port__);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        try {
            this.__address__ = InetAddress.getByName(this.__ip__);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendData(String data) {
        byte[] sendData = data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, data.length(), this.__address__, this.__port__);

        try {
            this.__socket__.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] receiveData() {
        byte[] data;

        try {
            byte[] buffer = new byte[50000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            this.__socket__.receive(packet);
            byte[] byteBuffer = packet.getData();
            byte[] outData = new byte[packet.getLength()];

            synchronized (outData) {
                for (int i = 0; i < outData.length; ++i) {
                    outData[i] = byteBuffer[i];
                }
            }

            data = outData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private void receiveDataListener() {
        while (true) {
            System.out.println(receiveData().length);
        }
    }
}