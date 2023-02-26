package com.example.robotarmclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UDPSocket implements Runnable {
    private DatagramSocket __socket__ = null;
    private Thread __socketThread__ = null;
    private Thread __sendDataListenerThread__ = null;
    private Thread __receiveDataListenerThread__ = null;
    private Thread __testConnectionThread__ = null;
    private InetAddress __address__ = null;
    private String __ip__ = "";
    private int __port__ = 0;
    private boolean __isErrorStatus__ = false;
    private ArrayList<byte[]> __sendDataArray__ = new ArrayList<>();
    private ArrayList<byte[]> __receiveDataArray__ = new ArrayList<>();

    public UDPSocket(String ip, int port) {
        this.setIP(ip);
        this.setPort(port);
        this.createSocketThread();
    }

    @Override
    public void run() {
        this.setErrorStatus(false);

        this.createSocket();
        if (this.getErrorStatus()) {
            return;
        }

        this.createAddress();
        if (this.getErrorStatus()) {
            this.closeSocket();
            return;
        }

        this.createSendDataListenerThread();
        this.createReceiveDataListenerThread();
        this.createTestConnectionThread();

        while (true) {
            if (this.getErrorStatus()) {
                try {
                    this.getSendDataListenerThread().join();
                    this.getReceiveDataListenerThread().join();
                    this.getTestConnectionThread().join();
                } catch (InterruptedException e) {
                    System.out.println("RA_UDPSocket_Run: Error (" + e + ").");
                }

                break;
            }
        }

        this.closeSocket();
    }

    public void createSocketThread() {
        this.__socketThread__ = new Thread(this::run);
        this.__socketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__socketThread__.start();
    }

    public void createAddress() {
        try {
            this.__address__ = InetAddress.getByName(this.getIP());
        } catch (UnknownHostException e) {
            System.out.println("RA_UDPSocket_CreateSocket: Error(" + e + ").");
            this.setErrorStatus(true);
        }
    }

    public void createSendDataListenerThread() {
        this.__sendDataListenerThread__ = new Thread(this::sendDataListener);
        this.__sendDataListenerThread__.setPriority(Thread.NORM_PRIORITY);
        this.__sendDataListenerThread__.start();
    }

    public void createReceiveDataListenerThread() {
        this.__receiveDataListenerThread__ = new Thread(this::receiveDataListener);
        this.__receiveDataListenerThread__.setPriority(Thread.NORM_PRIORITY);
        this.__receiveDataListenerThread__.start();
    }

    public void createTestConnectionThread() {
        this.__testConnectionThread__ = new Thread(this::testConnection);
        this.__testConnectionThread__.setPriority(Thread.NORM_PRIORITY);
        this.__testConnectionThread__.start();
    }

    public void createSocket() {
        try {
            this.__socket__ = new DatagramSocket(this.getPort());
        } catch (IOException e) {
            System.out.println("RA_UDPSocket_CreateSocket: Error(" + e + ").");
            this.setErrorStatus(true);
        }
    }

    public void closeSocket() {
        this.__socket__.close();
    }

    public void addSendDataArray(byte[] data) {
        this.__sendDataArray__.add(data);
    }

    public byte[] popSendDataArray() {
        return this.__sendDataArray__.remove(0);
    }

    public void addReceiveDataArray(byte[] data) {
        this.__receiveDataArray__.add(data);
    }

    public byte[] popReceiveDataArray() {
        return this.__receiveDataArray__.remove(0);
    }

    public void setIP(String ip) {
        this.__ip__ = ip;
    }

    public void setPort(int port) {
        this.__port__ = port;
    }

    public void setErrorStatus(boolean value) {
        this.__isErrorStatus__ = value;
    }

    public Thread getSocketThread() {
        return this.__socketThread__;
    }

    public Thread getSendDataListenerThread() {
        return this.__sendDataListenerThread__;
    }

    public Thread getReceiveDataListenerThread() {
        return this.__receiveDataListenerThread__;
    }

    public Thread getTestConnectionThread() {
        return this.__testConnectionThread__;
    }

    public DatagramSocket getSocket() {
        return this.__socket__;
    }

    public InetAddress getAddress() {
        return this.__address__;
    }

    public  String getIP() {
        return this.__ip__;
    }

    public int getPort() {
        return this.__port__;
    }

    public boolean getErrorStatus() {
        return this.__isErrorStatus__;
    }

    public int getSizeSendDataArray() {
        return this.__sendDataArray__.size();
    }

    public int getSizeReceiveDataArray() {
        return this.__receiveDataArray__.size();
    }

    public void sendDataListener() {
        while (true) {
            if (this.getSizeSendDataArray() > 0) {
                this.sendData(this.popSendDataArray());
            }

            if (this.getErrorStatus()) {
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void receiveDataListener() {
        byte[] data;

        while (true) {
            data = this.receiveData();

            if (this.getErrorStatus()) {
                break;
            }

            if (this.removeTestConnectionData(this.byteToString(this.trimData(data))).length() != 0) {
                this.addReceiveDataArray(data);
            }
        }
    }

    public void testConnection() {
        while (true) {
            this.sendData("RA_TestConnection".getBytes(StandardCharsets.UTF_8));

            if (this.getErrorStatus()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendData(byte[] data) {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, this.getAddress(), this.getPort());

        try {
            this.getSocket().send(sendPacket);

            if (this.removeTestConnectionData(this.byteToString(this.trimData(data))).length() != 0) {
                System.out.println("RA_UDPSocket_SendData: OK (" + this.byteToString(data) + ").");
            }
        } catch (IOException e) {
            System.out.println("RA_UDPSocket_SendData: Error (" + e + ").");
            this.setErrorStatus(true);
        }
    }

    public byte[] receiveData() {
        byte[] data = new byte[0];

        try {
            byte[] buffer = new byte[50000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            this.getSocket().receive(packet);
            byte[] byteBuffer = packet.getData();
            byte[] outData = new byte[packet.getLength()];

            for (int i = 0; i < outData.length; ++i) {
                outData[i] = byteBuffer[i];
            }

            data = outData;

            if (this.removeTestConnectionData(this.byteToString(this.trimData(data))).length() != 0) {
                System.out.println("RA_UDPSocket_ReceiveData: OK (" + this.byteToString(data) + ").");
            }
        } catch (Exception e) {
            System.out.println("RA_UDPSocket_ReceiveData: Error (" + e + ").");
            this.setErrorStatus(true);
        }

        return data;
    }

    public String byteToString(byte[] data) {
        return new String(data);
    }

    private byte[] trimData(byte[] data) {
        int outDataSize = data.length;
        int bufferSize = data.length;

        for (int i = bufferSize - 1; i >= 0; --i, --outDataSize) {
            if (data[i] != 0) {
                break;
            }
        }

        byte[] outData = new byte[outDataSize];

        for (int i = 0; i < outDataSize; ++i) {
            outData[i] = data[i];
        }

        return outData;
    }

    private String removeTestConnectionData(String data) {
        return data.replaceAll("RA_TestConnection", "");
    }
}