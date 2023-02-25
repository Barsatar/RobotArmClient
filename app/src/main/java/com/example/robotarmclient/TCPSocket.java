package com.example.robotarmclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TCPSocket implements Runnable {
    private Socket __socket__ = null;
    private Thread __socketThread__ = null;
    private Thread __sendDataListenerThread__ = null;
    private Thread __receiveDataListenerThread__ = null;
    private Thread __testConnectionThread__ = null;
    private OutputStream __outputStream__ = null;
    private InputStream __inputStream__ = null;
    private String __ip__ = "";
    private int __port__ = 0;
    private boolean __isErrorStatus__ = false;
    private ArrayList<byte[]> __sendDataArray__ = new ArrayList<>();
    private ArrayList<byte[]> __receiveDataArray__ = new ArrayList<>();

    public TCPSocket(String ip, int port) {
        this.setIP(ip);
        this.setPort(port);
        this.createSocketThread();
    }

    @Override
    public void run() {
        System.out.println("RA_TCPSocket_Run: Start");

        this.setErrorStatus(false);

        this.createSocket();
        if (this.getErrorStatus()) {
            return;
        }

        this.createInputStream();
        if (this.getErrorStatus()) {
            this.closeSocket();
            return;
        }

        this.createOutputStream();
        if (this.getErrorStatus()) {
            this.closeInputStream();
            this.closeSocket();
            return;
        }

        //this.createSendDataListenerThread();
        //this.createReceiveDataListenerThread();
        this.createTestConnectionThread();

        while (true) {
            if (this.getErrorStatus()) {
                try {
                    this.getSendDataListenerThread().join();
                    this.getReceiveDataListenerThread().join();
                    this.getTestConnectionThread().join();
                } catch (InterruptedException e) {
                    System.out.println("RA_TCPSocket_Run: Error (" + e + ").");
                }

                break;
            }
        }

        System.out.println("RA_TCPSocket_Run: End");

        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
    }

    public void createSocketThread() {
        this.__socketThread__ = new Thread(this::run);
        this.__socketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__socketThread__.start();
    }

    public void createSocket() {
        try {
            this.__socket__ = new Socket(this.getIP(), this.getPort());
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateSocket: Error(" + e + ").");
            this.setErrorStatus(true);
        }
    }

    private void createOutputStream() {
        try {
            this.__outputStream__ = this.getSocket().getOutputStream();
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateOutputStream: Error (" + e + ").");
            this.setErrorStatus(true);
        }
    }

    private void createInputStream() {
        try {
            this.__inputStream__ = this.getSocket().getInputStream();
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateInputStream: Error (" + e + ").");
            this.setErrorStatus(true);
        }
    }

    public void closeSocket() {
        try {
            this.__socket__.close();
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CloseSocket: Error(" + e + ").");
            this.setErrorStatus(true);
        }
    }

    private void closeOutputStream() {
        try {
            this.getOutputStream().close();
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CloseOutputStream: Error (" + e + ").");
            this.setErrorStatus(true);
        }
    }

    private void closeInputStream() {
        try {
            this.getInputStream().close();
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CloseInputStream: Error (" + e + ").");
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

    public Socket getSocket() {
        return this.__socket__;
    }

    public OutputStream getOutputStream() {
        return this.__outputStream__;
    }

    public InputStream getInputStream() {
        return this.__inputStream__;
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

    public Thread getSendDataListenerThread() {
        return this.__sendDataListenerThread__;
    }

    public Thread getReceiveDataListenerThread() {
        return this.__receiveDataListenerThread__;
    }

    public Thread getTestConnectionThread() {
        return this.__testConnectionThread__;
    }

    public void sendDataListener() {
        System.out.println("RA_TCPSocket_SendDataListener: Start");

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

        System.out.println("RA_TCPSocket_SendDataListener: End");
    }

    public void receiveDataListener() {
        System.out.println("RA_TCPSocket_ReceiveDataListener: Start");

        while (true) {
            this.addReceiveDataArray(this.receiveData());

            if (this.getErrorStatus()) {
                break;
            }
        }

        System.out.println("RA_TCPSocket_ReceiveDataListener: End");
    }

    public void testConnection() {
        System.out.println("RA_TCPSocket_TestConnection: Start");

        while (true) {
            this.sendData("test".getBytes(StandardCharsets.UTF_8));

            if (this.getErrorStatus()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("RA_TCPSocket_TestConnection: End");
    }

    public void sendData(byte[] data) {
        try {
            this.getOutputStream().write(data);
            System.out.println("RA_TCPSocket_SendData: OK (" + this.byteToString(data) + ").");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_SendData: Error (" + e + ").");
            this.setErrorStatus(true);
        }
    }

    public byte[] receiveData() {
        int bufferSize = 1024;
        byte[] data = new byte[bufferSize];

        try {
            this.getInputStream().read(data);
            System.out.println("RA_TCPSocket_ReceiveData: OK (" + this.byteToString(this.trimData(data)) + ").");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_ReceiveData: Error (" + e + ").");
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
}