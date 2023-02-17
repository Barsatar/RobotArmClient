package com.example.robotarmclient;

public class RobotAramManager implements  Runnable {
    private String __ip__ = "192.168.43.154";
    private int __port__ = 7000;
    private TCPSocket __TCPSocket__;
    private UDPSocket __UDPSocket__;

    public RobotAramManager() {
        this.createTCPSocket();
        //this.createUDPSocket();

        System.out.println("RA_RobotArmManager_Init: OK");
    }

    @Override
    public void run() {

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

    private void SetPort(int port) {
        this.__port__ = port;
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
