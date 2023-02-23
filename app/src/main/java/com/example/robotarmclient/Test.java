package com.example.robotarmclient;

import java.io.IOException;
import java.net.Socket;

public class Test implements Runnable{
    private Thread __thread__;
    private Socket __socket__;
    private int i = 0;

    public Test() {
        System.out.println("Init_1: " + this.getThread());
        this.createThread();
        System.out.println("Init_2: " + this.getThread());
    }

    @Override
    public void run() {
        this.createSocket();
        System.out.println(this.__socket__);
        System.out.println("!");
        this.closeSocket();
    }

    public void createThread() {
        this.__thread__ = new Thread(this, "Thread-" + this.i);
        this.__thread__.setPriority(Thread.NORM_PRIORITY);
        this.__thread__.start();
        this.i += 1;
    }

    public void createSocket() {
        try {
            this.__socket__ = new Socket("192.168.43.154", 7000);
        } catch (IOException e) {
            System.out.println("Test_CreateSocket: ERROR (" + e.toString() + ")");
        }
    }

    public void closeSocket() {
        try {
            this.__socket__.close();
        } catch (IOException e) {
            System.out.println("Test_CloseSocket: ERROR (" + e.toString() + ")");
        }
    }

    public Thread getThread() {
        return this.__thread__;
    }

    public Socket getSocket() {
        return this.__socket__;
    }
}
