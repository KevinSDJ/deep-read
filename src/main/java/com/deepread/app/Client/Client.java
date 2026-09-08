package com.deepread.app.Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket s = null;
    private BufferedReader inTeclado = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Client(String addr, int port)
    {
        // establecer conexion
        try {
            s = new Socket(addr, port);
            System.out.println("Connected");

            // captar datos de terminal
            inTeclado = new BufferedReader(new InputStreamReader(System.in));

            // Sends output to the socket
            out = new DataOutputStream(s.getOutputStream());
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }

        // String to read message from input
        String m = "";

        // Keep reading until "Over" is input
        while (!m.equals("Over")) {
            try {
                m = inTeclado.readLine();
                out.writeUTF(m);
                out.flush();
            }
            catch (IOException i) {
                System.out.println(i);
                break;
            }
        }

        // Close the connection
        try {
            inTeclado.close();
            out.close();
            s.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        Client c = new Client("127.0.0.1", 5000);
    }
}
