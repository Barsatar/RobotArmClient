package com.example.robotarmclient;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSocket implements Runnable {
    private String __ip__;
    private int __port__;
    private Thread __TCPSocketThread__;
    private Socket __socket__;
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

        Thread listenSendData1Thread = new Thread(this::listenSendData1);
        Thread listenSendData2Thread = new Thread(this::listenSendData2);
        listenSendData1Thread.setPriority(Thread.NORM_PRIORITY);
        listenSendData2Thread.setPriority(Thread.NORM_PRIORITY);
        listenSendData1Thread.start();
        listenSendData2Thread.start();
    }

    public void sendData(String data) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(this.__socket__.getOutputStream()));
            PrintWriter printWriter = new PrintWriter(dataOutputStream);

            printWriter.print(data);
            printWriter.flush();

            printWriter.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listenSendData1() {
        while (true) {
            sendData("1");
        }
    }

    public void listenSendData2() {
        while (true) {
            sendData("2");
        }
    }
}
