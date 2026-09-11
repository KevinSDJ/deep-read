package com.deepread.app.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class HandleClient implements Runnable{
    private Socket socket;
    private String id;
    private Consumer<String> onCloseSckt;

    public HandleClient(String iD, Socket client,Consumer<String> onCl){
        socket=client;
        id= iD;
        onCloseSckt= onCl;
    }
    @Override
    public void run() {
        DataInputStream in;
        try {
            in= new DataInputStream( new BufferedInputStream(socket.getInputStream()));
            String msg="";
            while(!msg.equals("Over")){
                msg= in.readUTF();
                
                System.out.println("Server: Msg: " + msg);
            }
            socket.close();
            in.close();
            onCloseSckt.accept(id);
            System.out.println("Client: " + id + " removed");
        } catch (Exception e) {
            
            System.out.println("Error manejando cliente: " + e.getMessage());
        }
    }
}

