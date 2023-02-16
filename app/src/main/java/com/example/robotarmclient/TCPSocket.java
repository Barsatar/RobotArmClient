package com.example.robotarmclient;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class TCPSocket implements Runnable {
    private String __ip__;
    private int __port__;
    private Thread __TCPSocketThread__;
    private Socket __socket__;
    private DataOutputStream __dataOutputStream__;
    private DataInputStream __dataInputStream__;

    public TCPSocket(String ip, int port) {
        this.setIP(ip);
        this.setPort(port);

        this.createTCPSocketThread();

        System.out.println("RA_TCPSocket_Init: OK");
    }

    @Override
    public void run() {
        this.createSocket();
        this.createDataInputStream();
        this.createDataOutputStream();
    }

    public void createTCPSocketThread() {
        this.__TCPSocketThread__ = new Thread(this);
        this.__TCPSocketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__TCPSocketThread__.start();

        System.out.println("RA_TCPSocket_CreateTCPSocketThread: OK");
    }

    public void sendData(String data) {
        PrintWriter printWriter = new PrintWriter(this.getDataOutputStream());
        printWriter.print(data);
        printWriter.flush();
        printWriter.close();

        System.out.println("RA_TCPSocket_SendData: OK (" + data + ")");
    }

    public String receiveData(){
        String data = "";
        int bytesRead;
        byte[] buffer = new byte[256];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
        InputStream inputStream = this.getDataInputStream();

        try {
            bytesRead = inputStream.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byteArrayOutputStream.write(buffer, 0, bytesRead);

        try {
            data = byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("RA_TCPSocket_ReceiveData: OK (" + data + ")");

        return data;
    }

    private void setIP(String ip) {
        this.__ip__ = ip;
    }

    private String getIP() {
        return this.__ip__;
    }

    private void setPort(int port) {
        this.__port__ = port;
    }

    private int getPort() {
        return this.__port__;
    }

    private void createSocket() {
        try {
            this.__socket__ = new Socket(this.getIP(), this.getPort());

            System.out.println("RA_TCPSocket_CreateSocket: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateSocket: ERROR (" + e.toString() + ")");
            throw new RuntimeException(e);
        }
    }

    private Socket getSocket() {
        return this.__socket__;
    }

    private void createDataOutputStream() {
        try {
            this.__dataOutputStream__ = new DataOutputStream(new BufferedOutputStream(this.getSocket().getOutputStream()));

            System.out.println("RA_TCPSocket_CreateDataOutputStream: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateDataOutputStream: ERROR (" + e.toString() + ")");
            throw new RuntimeException(e);
        }
    }

    private DataOutputStream getDataOutputStream() {
        return this.__dataOutputStream__;
    }

    private void createDataInputStream() {
        try {
            this.__dataInputStream__ = new DataInputStream(new BufferedInputStream(this.getSocket().getInputStream()));

            System.out.println("RA_TCPSocket_CreateDataOutputStream: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateDataInputStream: ERROR (" + e.toString() + ")");
            throw new RuntimeException(e);
        }
    }

    private DataInputStream getDataInputStream() {
        return this.__dataInputStream__;
    }
}
