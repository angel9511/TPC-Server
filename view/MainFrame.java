package view;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import chess.Piece;
import controller.MainController;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The Window, contains the subpanels Library, Menu, Console and Editor
	 */
	public MainFrame() {
		text = new JTextPane();
		text.setEditable(false);
		this.getContentPane().setLayout (new GridBagLayout());
		constraints = new GridBagConstraints();
		text.setText("Starting...");
		//text.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10");
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		this.getContentPane().add (text, constraints);
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.fill = GridBagConstraints.NONE;
		
//		constraints.gridx = 1; 
//		constraints.gridy = 1; 
//		constraints.gridwidth = 1; 
//		constraints.gridheight = 2;
//		constraints.fill = GridBagConstraints.BOTH;
//		constraints.ipadx = 100;
//		constraints.weightx = 0.3;
//		constraints.weighty = 1;
//		this.getContentPane().add (imageDeckButton, constraints);
//		constraints.weightx = 0.0;
//		constraints.weighty = 0.0;
//		constraints.fill = GridBagConstraints.NONE;
		
	}
	
	public void addText(String line){
		String s = text.getText();
		ArrayList<String> lines = new ArrayList<>(Arrays.asList(s.split("\n")));
		if(lines.size()>=10)
			lines.remove(0);
		lines.add(line);
		s = "";
		for(String string : lines)
			s+=string+"\n";
		s.substring(0, s.length()-2);
		text.setText(s);
	}
	
//	/**
//	 * Revalidates itself and repaints itself and the canvas
//	 */
	public void forceRepaint() {
		revalidate();
		repaint();
	}

	@Override
	public void repaint(){
		super.repaint();
		System.out.println(this.getWidth());
		System.out.println(this.getHeight());
	}
	
	//JPanel
	JPanel panelButton = new JPanel();
	//Buttons
	JButton textDeckButton = new JButton("Generate text deck");
	JButton imageThreeBoosterButton = new JButton("Generate image of 3 booster packs");
	JButton imageDeckButton = new JButton("Generate image of 9 booster packs");
	JButton parseButton = new JButton("Parse given text(parseless)");
	
	private JTextPane text;
	private JScrollPane scrollpanel;
	private JScrollPane scrollconsole;
	private GridBagConstraints constraints;

}
