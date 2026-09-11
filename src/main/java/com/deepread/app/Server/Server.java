package com.deepread.app.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Map<String,Socket> sockets= new ConcurrentHashMap<>();
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    public Server( int port){
        try (ServerSocket ssocket= new ServerSocket(port)) {
            log("Server run on port: " + port);
            log("Waiting for connections"); 
            while (true) {
                Socket cliente = ssocket.accept();
                log("connection:: "+ cliente.getInetAddress());
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


