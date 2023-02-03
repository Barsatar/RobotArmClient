package com.example.robotarmclient;

import java.io.IOException;
import java.net.*;

public class UDP implements Runnable {
    private DatagramSocket UDPsocket;
    private final int PORT;
    private final String IP;
    InetAddress IPAddress;

    public UDP(String ip, int port) {
        PORT = port;
        IP = ip;

        Thread thread = new Thread(this);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }

    @Override
    public void run() {
        try {
            UDPsocket = new DatagramSocket(this.PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        try {
            IPAddress = InetAddress.getByName(this.IP);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        this.listen();
    }

    private void listen() {
        while (true) {
            sendMessage("Start recording.");
            String message = new String(receiveMessage());

            if (message.equals("OK.")) {
                while (true) {
                    receiveMessage();
                }
            }
        }

    }

    private void sendMessage(String message) {
        byte[] sendData = message.getBytes();
        DatagramPacket send_packet = new DatagramPacket(sendData, message.length(), this.IPAddress, this.PORT);

        try {
            UDPsocket.send(send_packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] receiveMessage() {
        byte[] receiveData = new byte[50000];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        try {
            UDPsocket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String message = new String(receivePacket.getData());
        System.out.println(message);

        return receivePacket.getData();
    }
}