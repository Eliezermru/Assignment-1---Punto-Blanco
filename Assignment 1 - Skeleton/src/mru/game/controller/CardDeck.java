package mru.game.controller;
import java.util.ArrayList;
import java.util.Collections;
import mru.game.view.*;
/**
* This class represents a card deck
* @author ksalmani
* @version 1.0
*/
public class CardDeck {
	
	/**
	 * deck holds all of the cards that currently are in the current deck
	 */
	private ArrayList <Card> deck;
	
	/**
	 * This constructor initiate the arraylist and calls the repective methods to create a new deck
	 */
	public CardDeck() {
		deck = new ArrayList<Card>();
		createDeck();
		shuffleDeck();
	}
	/**
	 * This method creates the deck
	 */
	private void createDeck() {
		// suits holds the name of the suits
		String[] suits = {"Spades", "Diamond", "Clubs", "Hearts"};
		
		/*
		 * The for loop creates a whole new deck based on their suit and rank
		 */
		for (int i = 0 ; i < 4; i++) {
			for (int j = 1 ; j <=13 ; j++) {
				deck.add(new Card (j,suits[i]));
			}
		}
		
	}
	/**
	 * this method shuffle the deck after creating a new deck
	 */
	private void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
	
	public Card drawCard() { // draws, or removes and returns, the top card from the deck
		if (deck.isEmpty()) { // if the deck is empty, shows a message and resets/ shuffles deck
			AppMenu.emptyDeck(); // print message to user
			resetDeck(); // reset and shuffle deck
		}
		return deck.remove(0); // return the top card, index 0
		}
	
	public int getRemainingCards() {
		return deck.size();
	}
	
	public void resetDeck() { // empties the current deck, then creates a new deck and shuffles it
		deck.clear();
		createDeck();
		shuffleDeck();
	}
	/**
	 * The deck getter method
	 * @return the deck
	 */
	public ArrayList<Card> getDeck() {
		return deck;
	}
}
