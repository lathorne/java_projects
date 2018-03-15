package spell;

import java.io.*;
import java.util.*;

public class Corrector implements ISpellCorrector{

	Words trie = new Words();
	Words comparator = new Words();

	public void useDictionary(String dictionaryFileName) throws IOException{
		String input;
		Scanner in = new Scanner(new File(dictionaryFileName));
		while(in.hasNext()){
			input = in.next();
			if(isAlpha(input)){
				trie.add(input.toLowerCase());
			}
		}
		in.close();

		// Scanner in2 = new Scanner(new File("input2.txt"));
		// while(in2.hasNext()){
		// 	input = in2.next();
		// 	if(isAlpha(input)){
		// 		comparator.add(input.toLowerCase()); //to make a second equal trie
		// 	}
		// }
		// in2.close();
		// trie.printOut();
		// comparator.printOut();
		// trie.toString();
		// if(trie.equals(comparator)){
		// 	System.out.println("EQUAL");
		// }else{
		// 	System.out.println("NOT EQUAL");
		// }
	}

	public String suggestSimilarWord(String inputWord){
		String lowercaseword = inputWord.toLowerCase();
		if(trie.find(lowercaseword) == null){ //it wasn't found
			trie.clearoneAway(); //clears the array
			trie.findDeletion(lowercaseword);
			trie.findTransposition(lowercaseword);
			trie.findAlteration(lowercaseword);
			trie.findInsertion(lowercaseword); //should have a complete arraylist of words that are one away from input
			String outputafterOne = trie.pilotOne();
			if(outputafterOne == null){ //move on to finding words that are two away
				//second round of checks!
				String outputafterTwo = trie.pilotTwo();
				if(outputafterTwo == null){
					return null; //no matching strings
				}
				return outputafterTwo; //matching second string
			}

			return outputafterOne; //proper word after one round through checks
		}

		return lowercaseword; //original word
	}

	public boolean isAlpha(String name){
		return name.matches("[A-Za-z]+");
	}

	public void seek(String input){
		if(trie.find(input) == null){
			System.out.println("NO" + "\n");
		}else{
			System.out.println("YES" + "\n");
		}
	}

}