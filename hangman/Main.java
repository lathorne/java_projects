package hangman;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Main{


	public static void main(String[] args){
		if(args.length > 3 || args.length < 0){
			System.out.println("Invalid command line arguments.");
			return;
		}
		try{

		Hangman evilgame = new Hangman();
		int numguesses = Integer.parseInt(args[2]);
		
		//check to make sure the File can open
		evilgame.startGame(new File(args[0]), Integer.parseInt(args[1]));
		evilgame.runGame(numguesses);
		}catch(NumberFormatException ex){
			System.out.println("Invalid command line arguments.");
		}
	}


}