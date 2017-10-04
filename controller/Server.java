// place this file the path such ends with: ChatServer/server/ChatServer.java

package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

    private static final int portNumber = 4444;

    private int serverPort;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"
    private ClientThread client;

    public Server(){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }

    public void run(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            MainController.getInstance().addLine(serverSocket.getLocalSocketAddress().toString());
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    public void sendBoardToClients(String move){
    	client.sendBoardToClients(move);
    }
    
    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        MainController.getInstance().addLine("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                MainController.getInstance().addLine("accepts : " + socket.getRemoteSocketAddress());
                client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}
