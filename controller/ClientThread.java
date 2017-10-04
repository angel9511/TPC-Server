// place this file the path such ends with: ChatServer/server/ClientThread.java

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private Server server;

    public ClientThread(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }
    
    public void sendBoardToClients(String move){
    	String boardstate = MainController.getInstance().getBoardAsText();
    	move = boardstate + move;
        for(ClientThread thatClient : server.getClients()){
            PrintWriter thatClientOut = thatClient.getWriter();
            if(thatClientOut != null){
                thatClientOut.write(move + "\r\n");
                thatClientOut.flush();
            }
        }
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                    // System.out.println(input);
                    //MainController.getInstance().addLine(input); 
                    if(MainController.getInstance().checkIfIsPlay(input) == 1){
                    	if(MainController.getInstance().getDemocracy() == false){
                    		MainController.getInstance().setDemocracy(true);
                    		MoveDemocracyManager democracy = new MoveDemocracyManager();
                    		Thread democracyThread = new Thread(democracy);
                    		democracyThread.start();
                    		System.out.println("Democracy has started");                    		
                    	}
                    	MainController.getInstance().suggestMove(input);
                    }
                    //MainController.getInstance().makeAPlay(input);
                    String boardstate = MainController.getInstance().getBoardAsText();
                    input = boardstate+input;
                    for(ClientThread thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
