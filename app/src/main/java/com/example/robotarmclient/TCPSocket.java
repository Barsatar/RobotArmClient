package com.example.robotarmclient;

import java.io.*;
import java.net.Socket;

public class TCPSocket implements Runnable {
    private final String __ip__;
    private final int __port__;
    private final Thread __TCPSocketThread__;
    private Socket __socket__;
    private DataOutputStream __dataOutputStream__;
    private DataInputStream __dataInputStream__;

    public TCPSocket(String ip, int port) {
        this.__ip__ = ip;
        this.__port__ = port;

        this.__TCPSocketThread__ = new Thread(this);
        this.__TCPSocketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__TCPSocketThread__.start();
    }
    @Override
    public void run() {
        try {
            this.__socket__ = new Socket(this.__ip__, this.__port__);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.__dataOutputStream__ = new DataOutputStream(new BufferedOutputStream(this.__socket__.getOutputStream()));
            this.__dataInputStream__ = new DataInputStream(new BufferedInputStream(this.__socket__.getInputStream()));
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

        /*
        Thread sendDataListenerThread  = new Thread(this::sendDataListener);
        Thread receiveDataListenerThread  = new Thread(this::receiveDataListener);

        sendDataListenerThread.setPriority(Thread.NORM_PRIORITY);
        receiveDataListenerThread.setPriority(Thread.NORM_PRIORITY);

        sendDataListenerThread.start();
        receiveDataListenerThread.start();
        */
    }

    public void sendData(String data) {
        PrintWriter printWriter = new PrintWriter(this.__dataOutputStream__);
        printWriter.print(data);
        printWriter.flush();
        printWriter.close();

        System.out.println(data);
    }

    public byte[] receiveData() {
        int bytesRead;
        byte[] buffer = new byte[256];
        byte[] data;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
        InputStream inputStream = this.__dataInputStream__;

        try {
            bytesRead = inputStream.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byteArrayOutputStream.write(buffer, 0, bytesRead);
        data = byteArrayOutputStream.toByteArray();

        try {
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public void sendDataListener() {
        while (true) {
            System.out.println("sendDataListener");
        }
    }

    public void receiveDataListener() {
        while (true) {
            System.out.println("receiveDataListener");
        }
    }
}
