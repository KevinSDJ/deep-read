package com.deepread.app.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    private Map<String,Socket> sockets= new HashMap<>();
    private ExecutorService pool = Executors.newFixedThreadPool(10);
    

    public static void main(String args[]){
        Server server= new Server(5000);
    }


    public Server( int port){
        try (ServerSocket ssocket= new ServerSocket(port)) {
            log("Server: started");
            log("Waiting for clients"); 
            while (true) {
                Socket cliente = ssocket.accept();
                log("nuevo cliente"+ cliente.getInetAddress());
                // creo un id unico
                String shortHash = UUID.randomUUID().toString().replace("-", "");
                //guardo el cliente conectado
                sockets.put(shortHash, cliente);
                //ejecuto el hilo del cliente
                pool.execute(new HandleClient(shortHash,cliente,this::removeClient));
            }
            
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }
    public void log(String str){
        System.out.println(str);
    }

    public void removeClient(String id){
        sockets.remove(id);
    }
}

class HandleClient implements Runnable{
    private Socket socket;
    private String id;
    private Consumer<String> onCloseSckt;

    public HandleClient(String iD, Socket client,Consumer<String> onCloseSocket){
        socket=client;
        id= iD;
        onCloseSckt= onCloseSocket;
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
            onCloseSckt.accept(id);;
        } catch (Exception e) {
            
            System.out.println("Error manejando cliente: " + e.getMessage());
        }
    }


}
