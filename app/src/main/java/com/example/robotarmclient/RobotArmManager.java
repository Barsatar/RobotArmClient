package com.example.robotarmclient;

public class RobotArmManager implements Runnable {
    private TCPSocket __TCPSocket__ = null;
    private  UDPSocket __UDPSocket__ = null;
    private Thread __robotArmManagerThread__ = null;
    private String __ip__ = "192.168.43.154";
    private int __port__ = 7000;

    public RobotArmManager() {
        this.createRobotArmManagerThread();
        this.createTCPSocket();
        this.createUDPSocket();
    }

    @Override
    public void run() {
        while (true) {
           if (this.getTCPSocket() != null && this.getTCPSocket().getSocketThread() != null && !this.getTCPSocket().getSocketThread().isAlive()) {
               //this.getTCPSocket().createSocketThread();
               int i = 0;
           }

            if (this.getUDPSocket() != null && this.getUDPSocket().getSocketThread() != null && !this.getUDPSocket().getSocketThread().isAlive()) {
                this.getUDPSocket().createSocketThread();
            }
        }
    }

    public void createRobotArmManagerThread() {
        this.__robotArmManagerThread__ = new Thread(this::run);
        this.__robotArmManagerThread__.setPriority(Thread.NORM_PRIORITY);
        this.__robotArmManagerThread__.start();
    }

    public void createTCPSocket() {
        this.__TCPSocket__ = new TCPSocket(this.getIP(), this.getPort());
    }

    public void createUDPSocket() {
        this.__UDPSocket__ = new UDPSocket(this.getIP(), this.getPort());
    }

    public void setIP(String ip) {
        this.__ip__ = ip;
    }

    public void setPort(int port) {
        this.__port__ = port;
    }

    public Thread getRobotArmManagerThread() {
        return this.__robotArmManagerThread__;
    }

    public TCPSocket getTCPSocket() {
        return this.__TCPSocket__;
    }

    public UDPSocket getUDPSocket() {
        return this.__UDPSocket__;
    }

    public  String getIP() {
        return this.__ip__;
    }

    public int getPort() {
        return this.__port__;
    }
}
