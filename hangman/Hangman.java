package hangman;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Hangman implements IEvilHangmanGame{

	// Trie dictionaryTrie = new Trie();
	Set<String> dictionarySet = new HashSet<String>();
	StringBuilder currentWord = new StringBuilder();
	ArrayList<Character> guesses = new ArrayList<Character>();
	boolean play;
	StringBuilder emptyWord = new StringBuilder();
	// @SuppressWarnings("serial")
	// public static class GuessAlreadyMadeException extends Exception {


	// 	public GuessAlreadyMadeException(){

	// 	}

	// 	public GuessAlreadyMadeException(String message){
	// 		super(message);
	// 	}

	// }
	public boolean checkStringBuilder(){
		for(int i = 0; i < currentWord.length(); i++){
			if(currentWord.charAt(i) == '-'){ //means it is not complete
				return false;
			}
		}
		return true; //means it is complete
	}

	public void printSet(Set<String> print){
		for(String x: print){
			System.out.println(x);
		}
	}

	public void printALChar(ArrayList<Character> list){
		for(Character x: list){
			System.out.print(x + " ");
		}
	}

	public void printALInt(ArrayList<Integer> list){
		for(Integer x: list){
			System.out.println(x);
		}
	}

	public void printALSetString(ArrayList<Set<String>> list){
		int j = 1;
		for(Set<String> x: list){
			System.out.println("Set Number: " + j);
			printSet(x);
			j++;
		}
	}

	public void printMap(HashMap<String,Set<String>> hashedSets){
		for(Map.Entry<String, Set<String>> value: hashedSets.entrySet()){
			System.out.println(value.getKey() + ": " + value.getValue());
		}
	}

	public void printTreeMap(TreeMap<String,Set<String>> largestGroups){
		for(Map.Entry<String, Set<String>> value: largestGroups.entrySet()){
			System.out.println(value.getKey() + ": " + value.getValue());
		}
	}
	/**
	 * Starts a new game of evil hangman using words from <code>dictionary</code>
	 * with length <code>wordLength</code>.
	 *	<p>
	 *	This method should set up everything required to play the game,
	 *	but should not actually play the game. (ie. There should not be
	 *	a loop to prompt for input from the user.)
	 * 
	 * @param dictionary Dictionary of words to use for the game
	 * @param wordLength Number of characters in the word to guess
	 */
	public void startGame(File dictionary, int wordLength){
		emptyWord.setLength(0);
		for(int i = 0; i < wordLength; i++){
			emptyWord.append("-"); //this should set up the empty string
		}
		play = true;
		dictionarySet.clear();
		guesses.clear();
		String input;
		try{
			Scanner in = new Scanner(dictionary);
			while(in.hasNext()){
				input = in.next();
				int length_of_input = input.length();
				if(isAlpha(input) && (length_of_input == wordLength)){ //checks for proper word length as well
					// dictionaryTrie.add(input.toLowerCase());
					dictionarySet.add(input.toLowerCase());
				}
			}
			if(dictionarySet.size() == 0){
				System.out.println("Empty input file.");
				play = false;
			}else{
				for(int i = 0; i < wordLength; i++){
					currentWord.append("-");
				}
			// printSet(dictionarySet);
			}
			in.close();
		} catch(FileNotFoundException ex){
			System.out.println("Invalid File"); //how should this work?
		}
	}
	
	public Set<String> findRightMostInstance(ArrayList<Set<String>> stringList, char guess){
		//needs to delete inappropriate sets from arraylist

		while(stringList.size() > 1){
			// printALSetString(stringList);
			Set<String> first = stringList.get(0);
			Set<String> second = stringList.get(1);
			Iterator iter = first.iterator();
			String firstString = (String)iter.next();
			Iterator iter2 = second.iterator();
			String secondString = (String)iter2.next();
			char[] one = firstString.toCharArray();
			char[] two = secondString.toCharArray();

			//now the strings are set up and we need to compare from the right side
			for(int i = secondString.length() - 1; i > 0; i--){
				if(one[i] == guess && two[i] != guess){
					stringList.remove(1);
					//delete second entry
					break;
				}else if(two[i] == guess && one[i] != guess){
					stringList.remove(0);
					//delete first entry
					break;
				}
			//this could possibly go forever, but I don't see a way out of it
			}

		}
		//adjust the Stringbuilder here
		ArrayList<Integer> indexofguess = new ArrayList<Integer>();
		Set<String> third = stringList.get(0);
		Iterator iter = third.iterator();
		String blah = (String)iter.next();
		char[] c = blah.toCharArray();
		for(int i = 0; i < c.length; i++){
			if(c[i] == guess){
				indexofguess.add(i);
			}
		}
		// printALInt(indexofguess);
		for(int j = 0; j < indexofguess.size(); j++){
			currentWord.setCharAt(indexofguess.get(j), guess);
		}
		// printSet(stringList.get(0));
		return stringList.get(0);
	}


	/**
	 * Make a guess in the current game.
	 * 
	 * @param guess The character being guessed
	 *
	 * @return The set of strings that satisfy all the guesses made so far
	 * in the game, including the guess made in this call. The game could claim
	 * that any of these words had been the secret word for the whole game. 
	 * 
	 * @throws GuessAlreadyMadeException If the character <code>guess</code> 
	 * has already been guessed in this game.
	 */
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException{
		HashMap<String,Set<String>> hashedSets = new HashMap<String,Set<String>>();
		
		//search for the character in the already made guesses
		for(int i = 0; i < guesses.size(); i++){
			// System.out.println("HERE");
			if(guesses.get(i) == guess){
				// System.out.println("ALREADY made that guess");
				throw new GuessAlreadyMadeException(); 
			}
		}
		
		guesses.add(guess); //adds the valid guess to the arrayList of guesses
		// printALChar(guesses);
		//continue on here
		for(String s: dictionarySet){ //iterates over the dictionary set, maybe dictionarySet isn't being reset properly
			StringBuilder newBuilder = new StringBuilder();
			char[] s2 = s.toCharArray();
			for(int i = 0; i < s2.length; i++){ //for each letter in each word
				int number_of_indeces = 0;
				if(s2[i] == guess){ //checks to see if letter is the same as the guess
					newBuilder.append(guess);
				}else{
					newBuilder.append("-");
				}
				// hashcode = hashcode * number_of_indeces;
			} //word has been worked on at this point
			//need to add to proper set of strings
			if(hashedSets.containsKey(newBuilder.toString())){ //add to set
				Set<String> newSetToAdd = hashedSets.get(newBuilder.toString());
				newSetToAdd.add(s); //adds word to set
				// hashedSets.put(hashcode, newSetToAdd); //places set back in hashmap
			}else{ //create new set
				Set<String> newSetToAdd = new HashSet<String>();
				newSetToAdd.add(s);
				hashedSets.put(newBuilder.toString(), newSetToAdd);
			}	
		} //at this point the map should be complete

		//printMap(hashedSets); 

		//FIND THE LARGEST SUBSET IN THE MAP
		int largestgroupnum = 0;
		TreeMap<String,Set<String>> largestGroups = new TreeMap<String,Set<String>>(); //this logic must be WRONG
		for(Map.Entry<String, Set<String>> value: hashedSets.entrySet()){ //go through the map
			 if(value.getValue().size() > largestgroupnum){
			 	largestGroups.clear(); //needs to be cleared?
			 	largestgroupnum = value.getValue().size();
			 	largestGroups.put(value.getKey(), value.getValue());
			 }else if(value.getValue().size() == largestgroupnum){
			 	largestGroups.put(value.getKey(), value.getValue()); //grab the hashcode of that Set
			 }
		}
		// printTreeMap(largestGroups);
		// System.out.println(largestGroups.size() + "HERER");
		if(largestGroups.size() == 1){ //there is only one Set with the largest number of words
			//adjust the Stringbuilder here
			//need to check if the largest set contains the guess

			ArrayList<Integer> indexofguess = new ArrayList<Integer>();
			Set<String> third = new HashSet<String>();
			boolean toAdd  = false;
			for(Map.Entry<String, Set<String>> value: largestGroups.entrySet()){
				third = value.getValue(); //this needs to grab the only set in the map
			}
			for(String thisString: third){ //check to see if the Set contains the element and whether or not to add it to the StringBuilder
				char[] c = thisString.toCharArray(); 
				for(int i = 0; i < c.length; i++){
						// System.out.println(c[i]);
						// System.out.println(guess);
					if(c[i] == guess){
						toAdd = true;
					}
				}
			}
			// System.out.println(toAdd);
			if(toAdd == true){ //changes the stringBuilder if it needs to be added
				Iterator iter = third.iterator(); 
				String blah = (String)iter.next();
				char[] c = blah.toCharArray();
				for(int i = 0; i < c.length; i++){
					if(c[i] == guess){
						indexofguess.add(i);
					}
				}
				//need to adjust StringBuilder here
				for(int j = 0; j < indexofguess.size(); j++){
					currentWord.setCharAt(indexofguess.get(j), guess);
				}
			}
			dictionarySet = largestGroups.firstEntry().getValue();
			return largestGroups.firstEntry().getValue();
		}
		//there are multiple sets with a larger number of words
		// System.out.println("TREEMAP");
		// printTreeMap(largestGroups);
		
		//search for set with none of the letter found
		if(largestGroups.containsKey(emptyWord.toString())){
			// System.out.println("TRUE");
			//adjust the Stringbuilder here
			ArrayList<Integer> indexofguess = new ArrayList<Integer>();
			Set<String> third = largestGroups.get(emptyWord.toString()); //this needs to grab the 37 set in the map
			Iterator iter = third.iterator();
			String blah = (String)iter.next();
			char[] c = blah.toCharArray();
			for(int i = 0; i < c.length; i++){
				if(c[i] == guess){
					indexofguess.add(i);
				}
			}//adjust Stringbuilder
			for(int j = 0; j < indexofguess.size(); j++){
				currentWord.setCharAt(indexofguess.get(j), guess);
			}
			dictionarySet = largestGroups.get(emptyWord.toString());
			return largestGroups.get(emptyWord.toString());
		}
		// System.out.println("FALSE");

		//search for set with the fewest of the guess
		int leastNumberOfGuess = Integer.MAX_VALUE;
		ArrayList<Set<String>> returnSet = new ArrayList<Set<String>>();
		for(Set<String> value: largestGroups.values()){ //for each set of strings
			Iterator iter = value.iterator();
			int numOfGuess = 0; //only for this set
			String thisString = (String)iter.next();
			char[] c = thisString.toCharArray();
			for(int i = 0; i < c.length; i++){
				if(guess == c[i]){
					numOfGuess++;
				}
			}
			if(numOfGuess < leastNumberOfGuess){
				leastNumberOfGuess = numOfGuess;
				returnSet.clear();
				returnSet.add(value);
			}else if(numOfGuess == leastNumberOfGuess){
				returnSet.add(value);
			}	
		}
		// System.out.println(returnSet.size());
		// printALSetString(returnSet);

		// printALSetString(returnSet);

		if(returnSet.size() == 1){ //there is only one Set with the largest number of words and the fewest guessed letter
			//adjust the Stringbuilder here
			ArrayList<Integer> indexofguess = new ArrayList<Integer>();
			Set<String> third = returnSet.get(0); //this needs to grab the only set in the map
			Iterator iter = third.iterator();
			String blah = (String)iter.next();
			char[] c = blah.toCharArray();
			for(int i = 0; i < c.length; i++){
				if(c[i] == guess){
					indexofguess.add(i);
				}
			}
			for(int j = 0; j < indexofguess.size(); j++){
				currentWord.setCharAt(indexofguess.get(j), guess);
			}
			dictionarySet = returnSet.get(0);
			return returnSet.get(0);
		}

		//moving onto the set of words with the rightmost guessed letter, and then onward after that
		//use method here
		dictionarySet = findRightMostInstance(returnSet, guess);
		return findRightMostInstance(returnSet, guess); 
	}

	public void runGame(int numguesses){
		if(play == false){
			return;
		}
		char nextChar = 'x';
		Scanner input = new Scanner(System.in);
		

		while(numguesses > 0){ //lets them guess up to the number of guesses
			boolean prompt = true;
			if(numguesses == 1){
				System.out.println("You have " + numguesses + " guess left."); 
			}else{
				System.out.println("You have " + numguesses + " guesses left."); //change this to guess for one				
			}
			System.out.print("Used Letters: "); //all be lowercase
			Collections.sort(guesses);
			printALChar(guesses);
			System.out.print("\nWord: " + currentWord.toString() + "\n");

			while(prompt == true){
				System.out.print("Enter guess: "); 
				String next = input.next().toLowerCase();
				if(isAlpha(next) && (next.length() == 1)){ //proper character
					nextChar = next.charAt(0);
					prompt = false;
				}else{
					System.out.println("Invalid input\n");
				}
			}
			try{
				dictionarySet = makeGuess(nextChar);
				// printSet(dictionarySet);
				if(checkStringBuilder()){ //they've guessed correctly!
					System.out.println("You Win!");
					System.out.println("Word: " + currentWord.toString());
					break;
				}
				// numguesses--; //need the output for the number of letter here
				// System.out.println("HERE");
				//find the number of the guess character in Set
				Iterator iter = dictionarySet.iterator();
				String checkstring = (String)iter.next();
				int outputNum = 0;
				for(char x: checkstring.toCharArray()){
					if(x == nextChar){
						outputNum++;
					}
				} //output results of guess, needs to output loss at the end
				if(numguesses == 0){
					if(checkStringBuilder()){ //win conditions once guesses run out
						System.out.println("You Win!");
						System.out.println("Word: " + currentWord.toString());
						break;
					}else{
						System.out.println("You lose!");
						System.out.println("The word was: " + checkstring);
						break;
					}
				}

				if(outputNum == 0){ //check these for proper output
					System.out.println("Sorry, there are no " + nextChar + "'s\n");
					numguesses--;
				}else if(outputNum == 1){
					System.out.println("Yes, there is " + outputNum + " " + nextChar + "\n");
				}else{
					System.out.println("Yes, there are " + outputNum + " " + nextChar + "'s\n");
				}	


			}catch(GuessAlreadyMadeException ex){
				System.out.println("You already used that letter.\n"); 
				prompt = true; //reprompts here
			}
			
		}

	}

	public boolean isAlpha(String name){
		return name.matches("[A-Za-z]+");
	}

}
	

// for(Object name: structure_going_through)

// }