package com.example.robotarmclient;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPSocket implements Runnable {
    private String __ip__;
    private int __port__;
    private Thread __TCPSocketThread__;
    private Socket __socket__;
    private OutputStream __outputStream__;
    private InputStream __inputStream__;
    private ReceiveDataListener __receiveDataListener__;
    private SendDataListener __sendDataListener__;
    private TestConnection __testConnection__;
    private boolean __workStatus__;
    private boolean __errorStatus__;

    public TCPSocket(String ip, int port) {
        this.setIP(ip);
        this.setPort(port);
        this.setErrorStatus(false);
        this.setWorkStatus(false);
        this.setTcpSocketThreadNull();
        this.setSocketNull();
        this.setOutputStreamNull();
        this.setInputStreamNull();
        this.createTCPSocketThread();

        System.out.println("RA_TCPSocket_Init: OK");
    }

    @Override
    public void run() {
        this.setErrorStatus(false);
        this.setWorkStatus(true);

        this.createSocket();
        if (this.getErrorStatus()) {
            this.closeOutputStream();
            this.closeInputStream();
            this.closeSocket();
            this.setWorkStatus(false);

            return;
        }

        this.createOutputStream();
        if (this.getErrorStatus()) {
            this.closeOutputStream();
            this.closeInputStream();
            this.closeSocket();
            this.setWorkStatus(false);

            return;
        }

        this.createInputStream();
        if (this.getErrorStatus()) {
            this.closeOutputStream();
            this.closeInputStream();
            this.closeSocket();
            this.setWorkStatus(false);
            return;
        }

        this.createSendDataListener();
        this.createReceiveDataListener();
        this.createTestConnection();

        while (true) {
            if (this.getErrorStatus()) {
                   break;
            }
        }

        this.closeOutputStream();
        this.closeInputStream();
        this.closeSocket();
        this.setWorkStatus(false);

        return;
    }

    public void createTCPSocketThread() {
        this.__TCPSocketThread__ = new Thread(this);
        this.__TCPSocketThread__.setPriority(Thread.NORM_PRIORITY);
        this.__TCPSocketThread__.start();

        System.out.println("RA_TCPSocket_CreateTCPSocketThread: OK");
    }

    private void createSocket() {
        try {
            this.__socket__ = new Socket(this.getIP(), this.getPort());

            System.out.println("RA_TCPSocket_CreateSocket: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateSocket: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private void createOutputStream() {
        try {
            this.__outputStream__ = this.getSocket().getOutputStream();

            System.out.println("RA_TCPSocket_CreateOutputStream: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateOutputStream: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private void createInputStream() {
        try {
            this.__inputStream__ = this.getSocket().getInputStream();

            System.out.println("RA_TCPSocket_CreateInputStream: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CreateInputStream: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private void createSendDataListener() {
        this.__sendDataListener__ = new SendDataListener(this);

        System.out.println("RA_TCPSocket_CreateSendDataListener: OK");
    }

    private void createReceiveDataListener() {
        this.__receiveDataListener__ = new ReceiveDataListener(this);

        System.out.println("RA_TCPSocket_CreateReceiveDataListener: OK");
    }

    private void createTestConnection() {
        this.__testConnection__ = new TestConnection(this);

        System.out.println("RA_TCPSocket_CreateTestConnection: OK");
    }

    private void closeSocket() {
        try {
            this.getSocket().close();
            this.setSocketNull();

            System.out.println("RA_TCPSocket_CloseSocket: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CloseConnection: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private void closeOutputStream() {
        try {
            this.getOutputStream().close();
            this.setOutputStreamNull();

            System.out.println("RA_TCPSocket_CloseOutputStream: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CloseOutputStream: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private void closeInputStream() {
        try {
            this.getInputStream().close();
            this.setInputStreamNull();

            System.out.println("RA_TCPSocket_CloseInputStream: OK");
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_CloseInputStream: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private void setTcpSocketThreadNull() {
        this.__TCPSocketThread__ = null;
    }

    private void setSocketNull() {
        this.__socket__ = null;
    }

    private void setOutputStreamNull() {
        this.__outputStream__ = null;
    }

    private void setInputStreamNull() {
        this.__inputStream__ = null;
    }

    private void setIP(String ip) {
        this.__ip__ = ip;
    }

    private void setPort(int port) {
        this.__port__ = port;
    }

    private void setWorkStatus(boolean value) {
        this.__workStatus__ = value;
    }

    private void setErrorStatus(boolean value) {
        this.__errorStatus__ = value;
    }

    public boolean getWorkStatus() {
        return this.__workStatus__;
    }

    private boolean getErrorStatus() {
        return this.__errorStatus__;
    }

    private String getIP() {
        return this.__ip__;
    }

    private int getPort() {
        return this.__port__;
    }

    public Thread getTCPSocketThread() {
        return this.__TCPSocketThread__;
    }

    public Socket getSocket() {
        return this.__socket__;
    }

    private OutputStream getOutputStream() {
        return this.__outputStream__;
    }

    private InputStream getInputStream() {
        return this.__inputStream__;
    }

    private SendDataListener getSendDataListener() {
        return this.__sendDataListener__;
    }

    private ReceiveDataListener getReceiveDataListener() {
        return this.__receiveDataListener__;
    }

    private TestConnection getTestConnection() {
        return this.__testConnection__;
    }

    private void sendData(byte[] data) {
        try {
            this.getOutputStream().write(data);

            if (this.removeTestConnectionData(this.byteToString(this.trimData(data))).length() != 0) {
                System.out.println("RA_TCPSocket_SendData: OK (" + this.byteToString(data) + ")");
            }
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_SendData: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
        }
    }

    private byte[] receiveData() {
        int bufferSize = 1024;
        byte[] data = new byte[1024];

        try {
            this.getInputStream().read(data);

            if (this.removeTestConnectionData(this.byteToString(this.trimData(data))).length() != 0) {
                System.out.println("RA_TCPSocket_ReceiveData: OK (" + this.byteToString(this.trimData(data)) + ")");
            }
        } catch (IOException e) {
            System.out.println("RA_TCPSocket_ReceiveData: ERROR (" + e.toString() + ")");
            this.setErrorStatus(true);
       }

        return data;
    }

    private byte[] trimData(byte[] data) {
        int outDataSize = data.length;
        int bufferSize = data.length;

        for (int i = bufferSize - 1; i >= 0; --i, --outDataSize) {
            if (data[i] != 0) {
                break;
            }
        }

        byte[] outData = new byte[outDataSize];

        for (int i = 0; i < outDataSize; ++i) {
            outData[i] = data[i];
        }

        return outData;
    }

    private String byteToString(byte[] data) {
        return new String(data);
    }

    private String removeTestConnectionData(String data) {
        return data.replaceAll("TEST_CONNECTION", "");
    }

    private static class SendDataListener implements Runnable {
        private Thread __sendDataListenerThread__;
        private TCPSocket __TCPSocket__;

        public SendDataListener(TCPSocket tcpSocket) {
            this.setTCPSocket(tcpSocket);
            this.createSendDataListenerThread();

            System.out.println("RA_TCPSocket_SendDataListener_Init: OK");
        }
        @Override
        public void run() {
            System.out.println("RA_TCPSocket_SendDataListener_Start: OK");

            while (true) {
                if (this.getTCPSocket().getErrorStatus()) {
                    break;
                }

                this.getTCPSocket().sendData("".getBytes(StandardCharsets.UTF_8));

                if (this.getTCPSocket().getErrorStatus()) {
                    break;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("RA_TCPSocket_SendDataListener_End: OK");

            return;
        }

        private void createSendDataListenerThread() {
            this.__sendDataListenerThread__ = new Thread(this);
            this.__sendDataListenerThread__.setPriority(Thread.NORM_PRIORITY);
            this.__sendDataListenerThread__.start();

            System.out.println("RA_TCPSocket_SendDataListener_CreateSendDataListenerThread: OK");
        }

        private void setTCPSocket(TCPSocket tcpSocket) {
            this.__TCPSocket__ = tcpSocket;
        }

        private Thread getSendDataListenerThread() {
            return this.__sendDataListenerThread__;
        }

        private TCPSocket getTCPSocket() {
            return this.__TCPSocket__;
        }
    }

    private static class ReceiveDataListener implements Runnable {
        private Thread __receiveDataListenerThread__;
        private TCPSocket __TCPSocket__;

        public ReceiveDataListener(TCPSocket tcpSocket) {
            this.setTCPSocket(tcpSocket);
            this.createReceiveDataListenerThread();

            System.out.println("RA_TCPSocket_ReceiveDataListener_Init: OK");
        }
        @Override
        public void run() {
            System.out.println("RA_TCPSocket_ReceiveDataListener_Start: OK");

            while (true) {
                if (this.getTCPSocket().getErrorStatus()) {
                    break;
                }

                this.getTCPSocket().receiveData();

                if (this.getTCPSocket().getErrorStatus()) {
                    break;
                }
            }

            System.out.println("RA_TCPSocket_ReceiveDataListener_End: OK");

            return;
        }

        private void createReceiveDataListenerThread() {
            this.__receiveDataListenerThread__ = new Thread(this);
            this.__receiveDataListenerThread__.setPriority(Thread.NORM_PRIORITY);
            this.__receiveDataListenerThread__.start();

            System.out.println("RA_TCPSocket_ReceiveDataListener_CreateReceiveDataListenerThread: OK");
        }

        private void setTCPSocket(TCPSocket tcpSocket) {
            this.__TCPSocket__ = tcpSocket;
        }

        private Thread getReceiveDataListenerThread() {
            return this.__receiveDataListenerThread__;
        }

        private TCPSocket getTCPSocket() {
            return this.__TCPSocket__;
        }
    }

    private static class TestConnection implements Runnable {
        private Thread __testConnectionThread__;
        private TCPSocket __TCPSocket__;

        public TestConnection(TCPSocket tcpSocket) {
            this.setTCPSocket(tcpSocket);
            this.createTestConnectionThread();

            System.out.println("RA_TCPSocket_TestConnection_Init: OK");
        }
        @Override
        public void run() {
            System.out.println("RA_TCPSocket_TestConnection_Start: OK");

            while (true) {
                if (this.getTCPSocket().getErrorStatus()) {
                    break;
                }

                this.getTCPSocket().sendData("TEST_CONNECTION".getBytes(StandardCharsets.UTF_8));

                if (this.getTCPSocket().getErrorStatus()) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("RA_TCPSocket_TestConnection_End: OK");

            return;
        }

        private void createTestConnectionThread() {
            this.__testConnectionThread__ = new Thread(this);
            this.__testConnectionThread__.setPriority(Thread.NORM_PRIORITY);
            this.__testConnectionThread__.start();

            System.out.println("RA_TCPSocket_TestConnection_CreateTestConnectionThread: OK");
        }

        private void setTCPSocket(TCPSocket tcpSocket) {
            this.__TCPSocket__ = tcpSocket;
        }

        private Thread getTestConnectionThread() {
            return this.__testConnectionThread__;
        }

        private TCPSocket getTCPSocket() {
            return this.__TCPSocket__;
        }
    }
}