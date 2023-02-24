package com.example.robotarmclient;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UDPSocket implements Runnable {
    private Thread __socketThread__ = null;
    private Thread __sendDataListenerThread__ = null;
    private Thread __receiveDataListenerThread__ = null;
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
        System.out.println("RA_UDPSocket_Run: Start");

        this.setErrorStatus(false);
        this.createSendDataListenerThread();
        this.createReceiveDataListenerThread();

        while (true) {
            if (this.getErrorStatus()) {
                try {
                    this.getSendDataListenerThread().join();
                    this.getReceiveDataListenerThread().join();
                } catch (InterruptedException e) {
                    System.out.println("RA_UDPSocket_Run: Error (" + e + ").");
                }

                break;
            }
        }

        System.out.println("RA_UDPSocket_Run: End");
    }

    public void createSocketThread() {
        this.__socketThread__ = new Thread(this::run);
        this.__socketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__socketThread__.start();
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

    public void sendDataListener() {
        System.out.println("RA_UDPSocket_SendDataListener: Start");

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

        System.out.println("RA_UDPSocket_SendDataListener: End");
    }

    public void receiveDataListener() {
        System.out.println("RA_UDPSocket_ReceiveDataListener: Start");

        while (true) {
            this.addReceiveDataArray(this.receiveData());

            if (this.getErrorStatus()) {
                break;
            }
        }

        System.out.println("RA_UDPSocket_ReceiveDataListener: End");
    }

    public void sendData(byte[] data) {
        int i = 0;
    }

    public byte[] receiveData() {
        int i = 0;

        return new byte[0];
    }
}
