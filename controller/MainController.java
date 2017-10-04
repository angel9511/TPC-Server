package controller;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chess.Board;
import chess.Piece;
import view.MainFrame;

public class MainController {
  
    public MainController() {
		window = new MainFrame();
		server = new Server();
		drawAvailable = false;
		moveSuggestions = new ArrayList<>();
		moveVotes = new ArrayList<>();
		usersThatAlreadyVoted = new ArrayList<>();
		inDemocracy = false;
	}
    
    /**
	 * Initializes the window with the default parameters and title
	 */
	public void initGraphic(){
		window.setTitle(_tittle );
		window.setVisible(true);
		window.setBounds(0, 0, 390, 192);
		window.setResizable(false);
		//window.setExtendedState( Frame.MAXIMIZED_BOTH );
		window.setBackground(Color.WHITE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainController main = getInstance();
		main.initGraphic();
        main.startServer();
        main.color = "white";
        main.gameBoard = new Board();
//        System.out.println(main.getBoardAsText().length());
//        System.out.println(main.getBoardAsText().substring(0,129));
	}
	
	public int makeAPlay(String move){
		System.out.println(color);
		System.out.println(move.charAt(0)!=color.charAt(0));
		System.out.println(move);
		if(move.charAt(0)!=color.charAt(0))
			return 0;
		move.replaceAll("\n", "");
		move.replaceAll("\r", "");
		if(move.length() < 5 )
			return 0;
		move = move.substring(move.length()-5);
		System.out.println("move "+move);
		if(drawAvailable){
            if(move.contains("draw")){
                System.out.println("The game is a draw.");
                return -1;
            }else{
                drawAvailable = false;
            }
        }

        if(move.contains("resign")){
            System.out.println(color + " resigns");
            System.out.println(colorToggle(color) + " wins the game!");
            return -1;
        }

        try {
            gameBoard.performMove(move, color, true);
        } catch (Exception e) {
            // Ask for user input again
            System.out.println("Not a move, sent as message");
            return 0;
        } 

        Piece[][] oldBoard = gameBoard.board.clone();

        if(!gameBoard.canAnyPieceMakeAnyMove(colorToggle(color))){
            if(gameBoard.isInCheck(colorToggle(color))){
                System.out.println("Checkmate. " + color + " wins");
                System.out.println("Game over!");
            }else{
                System.out.println("Stalemate!");
            }
            return -1;
        }

        gameBoard.board = oldBoard;

        if(gameBoard.isInCheck(colorToggle(color))){
            System.out.println(colorToggle(color) + " is in check.");
        }

        if(move.contains("draw?")){
            drawAvailable = true;
        }

        //Now I have to check to see if either player is in check or checkmate
        //I also have to see if there is a stalemate

        color = colorToggle(color);

        return 1;
	}
	
	
	private void startServer() {
		Thread thread = new Thread(server);
		thread.start();
	}

	public static String colorToggle(String color){
        if(color.equals("white")){
            return "black";
        }

        return "white";
    }

	/**
	 * Singleton
	 * @return MainController instance of the singleton pattern
	 */
	public static MainController getInstance() {
		if ( main == null ) {
			main = new MainController();
		}
		return main;
	}
			

	private MainFrame window;
	private Server server;
	private static MainController main;
	private String _tittle = "Shared Chess Server V0.9";
	private Board gameBoard;
	private String color;
	private boolean drawAvailable;
	private ArrayList<String> moveSuggestions;
	private ArrayList<Integer> moveVotes;
	private ArrayList<String> usersThatAlreadyVoted;
	private boolean inDemocracy;
	
	public void setDemocracy(boolean b){
		inDemocracy = b;
	}
	
	public boolean getDemocracy(){
		return inDemocracy;
	}
	
	public void addLine(String line) {
		window.addText(line);
	}

	public String getBoardAsText() {
		return color.substring(0, 1)+gameBoard.toString();
	}

	public int checkIfIsPlay(String move) {
		System.out.println(color);
		System.out.println(move.charAt(0)!=color.charAt(0));
		System.out.println(move);
		if(move.charAt(0)!=color.charAt(0))
			return 0;
		move.replaceAll("\n", "");
		move.replaceAll("\r", "");
		if(move.length() < 5 )
			return 0;
		move = move.substring(move.length()-5);
		System.out.println("move "+move);
		if(drawAvailable){
            if(move.contains("draw")){
                System.out.println("The game is a draw.");
                return -1;
            }else{
                drawAvailable = false;
            }
        }

        if(move.contains("resign")){
            return -1;
        }

        try {
            gameBoard.checkMove(move, color, true);
        } catch (Exception e) {
            // Ask for user input again
            System.out.println("Not a move, sent as message");
            return 0;
        } 

        Piece[][] oldBoard = gameBoard.board.clone();

        if(!gameBoard.canAnyPieceMakeAnyMove(colorToggle(color))){
            if(gameBoard.isInCheck(colorToggle(color))){
                System.out.println("Checkmate. " + color + " wins");
                System.out.println("Game over!");
            }else{
                System.out.println("Stalemate!");
            }
            return -1;
        }

        gameBoard.board = oldBoard;

        if(gameBoard.isInCheck(colorToggle(color))){
            System.out.println(colorToggle(color) + " is in check.");
        }

        if(move.contains("draw?")){
            drawAvailable = true;
        }

        return 1;

	}

	public void pickMove() {
		int index = -1;
		int bigger = -1;
		for(int i = 0; i < moveSuggestions.size(); i++){
			System.out.println(moveSuggestions.get(i) + " has "+ moveVotes.get(i)+" votes");
			if(moveVotes.get(i) > bigger){
				index = i;
				bigger = moveVotes.get(i);
			}
		}
		makeAPlay(usersThatAlreadyVoted.get(index) + moveSuggestions.get(index));
		server.sendBoardToClients(usersThatAlreadyVoted.get(index) + moveSuggestions.get(index));
		moveSuggestions.clear();
		moveVotes.clear();
		usersThatAlreadyVoted.clear();
	}

	public void suggestMove(String input) {
		int index = -1;
		String user = input.substring(0, input.length()-5);
		String move = input.substring(input.length()-5);
		System.out.println(user + " chooses "+move);
		if(usersThatAlreadyVoted.contains(user)){
			//Do nothing
		}else{
			for(int i = 0; i < moveSuggestions.size(); i++){
				if(moveSuggestions.get(i).equals(move)){
					index = i;
					i = moveSuggestions.size();
				}
			}
			if(index == -1){
				moveSuggestions.add(move);
				moveVotes.add(1);
			}else{
				moveVotes.set(index, moveVotes.get(index) + 1);
			}
			usersThatAlreadyVoted.add(user);
		}
	}
	
}
