package com.example.robotarmclient;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientTcpSocket implements Runnable {
    private Thread clientTcpThread;
    private Socket clientTcpSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private final String RPI_IP;
    private final Integer PORT;

    public ClientTcpSocket(String rpi_ip, Integer port) {
        this.clientTcpThread = new Thread(this);
        this.clientTcpThread.setPriority(Thread.NORM_PRIORITY);
        this.clientTcpThread.start();
        this.RPI_IP = rpi_ip;
        this.PORT = port;
    }

    @Override
    public void run() {
        try {
            this.clientTcpSocket = new Socket(this.RPI_IP , this.PORT);
        } catch(IOException e) {
            System.out.println( "Failed to create TCP socket." );
            e.printStackTrace();
        }

        System.out.println("Connected.");

        try {
            this.dataInputStream = new DataInputStream(new BufferedInputStream( this.clientTcpSocket.getInputStream()));
            this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(this.clientTcpSocket.getOutputStream()));
        } catch ( IOException e ) {
            System.out.println( "Failed to create streams." );
            e.printStackTrace();
        }

        String data = "";
        try {
            data = scanner();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Objects.equals(data, "Server success.")) {
            sender("Client success.");

            try {
                scanner();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sender(String data) {
        PrintWriter printWriter = new PrintWriter(this.dataOutputStream);
        printWriter.print(data);
        System.out.println("Message sent. Message text: " + data);
        printWriter.flush();
        printWriter.close();
    }

    public String scanner() throws IOException {
        String data = "";
        int bytesRead;
        byte[] buffer = new byte[256];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
        InputStream inputStream = this.dataInputStream;

        try {
            bytesRead = inputStream.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byteArrayOutputStream.write(buffer, 0, bytesRead);

        try {
            data = byteArrayOutputStream.toString("UTF-8");
            System.out.println("The message is received. Message text: " + data);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        byteArrayOutputStream.flush();

        try {
            byteArrayOutputStream.close();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}