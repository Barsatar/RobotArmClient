package com.example.robotarmclient;

public class RobotArmManager implements  Runnable {
    private String __ip__ = "192.168.43.154";
    private int __port__ = 7000;
    private Thread __robotArmManagerThread__;
    private TCPSocket __TCPSocket__;
    private UDPSocket __UDPSocket__;

    public RobotArmManager() {
        this.createTCPSocket();
        //this.createUDPSocket();
        this.createRobotArmManagerThread();

        System.out.println("RA_RobotArmManager_Init: OK");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createRobotArmManagerThread() {
        this.__robotArmManagerThread__ = new Thread(this);
        this.__robotArmManagerThread__.setPriority(Thread.NORM_PRIORITY);
        this.__robotArmManagerThread__.start();

        System.out.println("RA_RobotArmManager_CreateRobotArmManagerThread: OK");
    }

    private void createTCPSocket() {
        this.__TCPSocket__ = new TCPSocket(this.getIP(), this.getPort());
    }

    private void createUDPSocket() {
        this.__UDPSocket__ = new UDPSocket(this.getIP(), this.getPort());
    }

    private void setIP(String ip) {
        this.__ip__ = ip;
    }

    private void setPort(int port) {
        this.__port__ = port;
    }

    private Thread getRobotArmManagerThread() {
        return this.__robotArmManagerThread__;
    }

    private TCPSocket getTCPSocket() {
        return this.__TCPSocket__;
    }

    private UDPSocket getUDPSocket() {
        return this.__UDPSocket__;
    }

    private String getIP() {
        return this.__ip__;
    }

    private int getPort() {
        return this.__port__;
    }
}