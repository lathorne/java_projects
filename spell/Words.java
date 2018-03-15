package spell;

import java.util.*;
import java.io.*;
import java.lang.*;

public class Words implements ITrie {

	WordNode root = new WordNode();
	int wordcount = 0;
	int nodecount = 1;
	StringBuilder buildit = new StringBuilder();
	StringBuilder smallbuild = new StringBuilder();

	int nodecheck;
	boolean equal;

	ArrayList<String> oneAway = new ArrayList<String>(); //this needs to be reset each time
	ArrayList<String> twoAway = new ArrayList<String>(); //this needs to be reset each time

	ArrayList<String> highestvaluestrings = new ArrayList<String>(); //to put the highest values in
	int highestvalue; //records the highest frequency of all words in trie

	 
	public void add(String word) { 
		WordNode currentNode = root;
		char[] letters = word.toCharArray();
		int ascii = 1;
		for(int i = 0; i < letters.length; i++){
			ascii = (int)letters[i] - 97;
			if(currentNode.nodeArray[ascii] == null){ 
				currentNode.nodeArray[ascii] = new WordNode();
				currentNode = currentNode.nodeArray[ascii];
				nodecount++;
			}else{
				currentNode = currentNode.nodeArray[ascii];
			}
		}
		if(currentNode.getValue() == 0){
			wordcount++;	//only for unique words
		}
		currentNode.increment(); //increase the value of the node at the end of the word by one
	}
	 		
	public ITrie.INode find(String word) { //seems to be working, checked with seek class in Corrector
		WordNode currentNode = root;
		char[] letters = word.toCharArray();
		int ascii;
		for(int i = 0; i < letters.length; i++){
			ascii = (int)letters[i] - 97;
			if(currentNode.nodeArray[ascii] == null){ //it isn't there
				return null;
			}else{ //it is!
				currentNode = currentNode.nodeArray[ascii];
			}	
		}
		if(currentNode.getValue() >= 1){
			return currentNode;
		}
		return null;
	}

	public void printOut(){
		System.out.println("WORD COUNT: " + wordcount + "\nNODE COUNT: " + nodecount + "\n");
	}
	 
	public int getWordCount() { 
		return wordcount;
	}
	 
	public int getNodeCount() {
		return nodecount;
	}

	@Override
	public String toString() {
		buildit.setLength(0);
		toString(root);
		// System.out.println(buildit.toString());
		return buildit.toString();
	}
	
	//recursive portion of function
	public void toString(WordNode node){
		for(int i = 0; i < node.nodeArray.length; i++){
			if(node.nodeArray[i] != null){
				smallbuild.append((char)(i+97));
				if(node.nodeArray[i].getValue() >= 1){
					buildit.append(smallbuild.toString() + "\n");
				}
				toString(node.nodeArray[i]); //recurses here
			}
		} //reduce the smallbuild size by one
		if(smallbuild.length() > 0)
			smallbuild.deleteCharAt(smallbuild.length() - 1);
	}

	@Override
	public int hashCode() { 
		return wordcount * nodecount * 37;
	}
	
	@Override
	public boolean equals(Object o) { 
		equal = true;
		nodecheck = 0;
		if(o == null){
			return false;
		}
		if(o.getClass() != this.getClass()){
			return false;
		}
		Words w = (Words)o;
		if(w.root.getValue() != this.root.getValue()){
			return false;
		}
		if(w.nodecount != this.nodecount){
			return false;
		}
		if(w.wordcount != this.wordcount){
			return false;
		}
		WordNode compareRoot = w.root;
		return this.equals(root, compareRoot);
	}

	//recursive function
	public boolean equals(WordNode first, WordNode second) { 
		for(int i = 0; i < first.nodeArray.length; i++){ //check the nodes before putting them through a function
			if(second.nodeArray[i] == null && first.nodeArray[i] != null){
				return false;
			}
			if(second.nodeArray[i] != null && first.nodeArray[i] == null){
				return false;
			}
			if(second.nodeArray[i] != null && first.nodeArray[i] != null){
				if(second.nodeArray[i].getValue() != first.nodeArray[i].getValue()){
					return false;
				}
				nodecheck++;
				if(nodecheck != nodecount){ //stops once all nodes are found
					equal = equals(first.nodeArray[i], second.nodeArray[i]);
				}
			}
		}
		return equal;
	}


	public class WordNode implements INode {

		private int value;
		private WordNode[] nodeArray = new WordNode[26];
		
		WordNode(){
			value = 0;
		}

		public int getValue() { 
			return value;
		}

		public void increment(){
			value++;
		}
	 
	}

	public void findDeletion(String inputWord){ //adds all possible deletions of the word to an arrayList
		for(int i = 0; i < inputWord.length(); i++){
			StringBuilder stringit = new StringBuilder(inputWord);
			stringit.deleteCharAt(i);
			oneAway.add(stringit.toString());
		}
	}

	public void findTransposition(String inputWord){
		for(int i = 0; i < inputWord.length() - 1; i++){
			char[] c = inputWord.toCharArray();
			char temp = c[i];
			c[i] = c[i + 1];
			c[i + 1] = temp;
			String swap = new String(c);
			oneAway.add(swap);
		}
	}

	public void findAlteration(String inputWord){
		for(int j = 0; j < 26; j++){ //for all 26 letters of the alphabet
			char letter = (char)(j+97);
			for(int i = 0; i < inputWord.length(); i++){ //go through each letter in word and replace
				char[] c = inputWord.toCharArray();
				if(letter != c[i]){ //will this work?
					c[i] = letter;
					String swap = new String(c);
					oneAway.add(swap);
				}
			}
		}
	}

	public void findInsertion(String inputWord){
		for(int j = 0; j < 26; j++){ //for all 26 letters of the alphabet
			char letter = (char)(j+97);
			for(int i = 0; i < inputWord.length() + 1; i++){ //check to make sure this works
				StringBuilder stringit = new StringBuilder(inputWord);
				stringit.insert(i,letter);
				oneAway.add(stringit.toString());
			}
		}
	}

	public String pilotOne(){
		highestvalue = 0;
		for(int i = 0; i < oneAway.size(); i++){
			if(find(oneAway.get(i))!=null){ //the word is found
				int value = find(oneAway.get(i)).getValue(); //grabs value of the node
				if(value > highestvalue){
					highestvaluestrings.clear();
					highestvaluestrings.add(oneAway.get(i));
					highestvalue = value;
				}else if(value == highestvalue){
					highestvaluestrings.add(oneAway.get(i));
				}
			}
		}
		if(highestvaluestrings.size() == 0){
			return null; //means we have to go through two away
		}
		Collections.sort(highestvaluestrings);
		return highestvaluestrings.get(0); //returns the first alphabetically with the highest frequency
	}

	public String pilotTwo(){
		twoAway.clear(); //clears the secondary array
		for(int i = 0; i < oneAway.size(); i++){ //go through all entries in oneAway list and find 2 aways
			findDeletion2(oneAway.get(i));
			findTransposition2(oneAway.get(i));
			findAlteration2(oneAway.get(i));
			findInsertion2(oneAway.get(i)); 
		} //also could sort alphabetically before finding highest frequency string
		highestvalue = 0;
		highestvaluestrings.clear();
		for(int i = 0; i < twoAway.size(); i++){
			if(find(twoAway.get(i))!=null){ //the word is found
				int value = find(twoAway.get(i)).getValue(); //grabs value of the node
				if(value > highestvalue){
					highestvaluestrings.clear();
					highestvaluestrings.add(twoAway.get(i));
					highestvalue = value;
				}else if(value == highestvalue){
					highestvaluestrings.add(twoAway.get(i));
				}
			}
		}
		if(highestvaluestrings.size() == 0){
			return null; //nothing similar
		}
		Collections.sort(highestvaluestrings);
		return highestvaluestrings.get(0); //returns the first alphabetically with the highest frequency
	}

	public void clearoneAway(){
		oneAway.clear();
	}

	public void findDeletion2(String inputWord){ //adds all possible deletions of the word to an arrayList
		for(int i = 0; i < inputWord.length(); i++){
			StringBuilder stringit = new StringBuilder(inputWord);
			stringit.deleteCharAt(i);
			twoAway.add(stringit.toString());
		}
	}

	public void findTransposition2(String inputWord){
		for(int i = 0; i < inputWord.length() - 1; i++){
			char[] c = inputWord.toCharArray();
			char temp = c[i];
			c[i] = c[i + 1];
			c[i + 1] = temp;
			String swap = new String(c);
			twoAway.add(swap);
		}
	}

	public void findAlteration2(String inputWord){
		for(int j = 0; j < 26; j++){ //for all 26 letters of the alphabet
			char letter = (char)(j+97);
			for(int i = 0; i < inputWord.length(); i++){ //go through each letter in word and replace
				char[] c = inputWord.toCharArray();
				if(letter != c[i]){ //will this work?
					c[i] = letter;
					String swap = new String(c);
					twoAway.add(swap);
				}
			}
		}
	}

	public void findInsertion2(String inputWord){
		for(int j = 0; j < 26; j++){ //for all 26 letters of the alphabet
			char letter = (char)(j+97);
			for(int i = 0; i < inputWord.length() + 1; i++){ //check to make sure this works
				StringBuilder stringit = new StringBuilder(inputWord);
				stringit.insert(i,letter);
				twoAway.add(stringit.toString());
			}
		}
	}
	
}
