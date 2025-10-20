


package mru.test.driver;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import mru.game.model.Player;
import mru.game.controller.CardDeck;
import mru.game.controller.BlackjackGame;
import mru.game.controller.Card;



class MethodTests {
	@Test
	public void testDeckGeneration() { // test that a new deck contains 52 cards
		CardDeck deck = new CardDeck(); // creating a new deck object
		assertEquals(52, deck.getRemainingCards()); // using assert equals to verify that remaining cards in deck is 52
	}
	
	@Test
	public void testDeckSizeAfterDrawCard() { // drawing one card and making sure the deck size goes down to 51
		CardDeck deck = new CardDeck(); // creating new deck object
		deck.drawCard(); // using draw card method
		assertEquals(51, deck.getRemainingCards()); // using assert equals to verify that remaining cards in deck is now 51
	
	}
	
	@Test
	public void testEmptyDeckResets() { //drawing all cards from deck, making sure empty deck = 0, then resetting deck to make sure it isn't null
		CardDeck deck = new CardDeck();
		for (int i = 0; i < 52; i++) { // loop that draws all cards
			deck.drawCard();
		}
		assertEquals(0, deck.getRemainingCards()); // checking if there are no cards remaining in the deck
		Card c = deck.drawCard(); // creating a new deck
		assertNotNull(c); // making sure card isn't null, meaning a new deck was created
		assertEquals(51, deck.getRemainingCards()); // checking that the remaining cards are now 51
	}
	
	
	@Test
	public void testToStringAce() { // tests string output for Ace or rank 1
		Card c = new Card(1, "Diamonds");
		assertEquals("Ace of Diamonds", c.toString());
	}
	
	@Test
	public void testToStringTen() { // tests the string output for a numbered card
		Card c = new Card(10, "Hearts");
		assertEquals("10 of Hearts", c.toString());
	}
	
	@Test
	public void testToStringKing() { // tests a face card
		Card c = new Card(13, "Spades");
		assertEquals("King of Spades", c.toString());
	}
@Test
	public void testConstructorAndGetters() {
		Player p = new Player("Brogan", 100.00, 1);
		assertEquals("Brogan", p.getName());
		assertEquals(100.00, p.getBalance());
		assertEquals(1, p.getNumOfWins());
	}
	
	@Test
	public void testSetters() {
		Player p = new Player("Brogan", 0.00, 0);
		p.setBalance(100.00);
		p.setNumOfWins(1);
		p.setName("Jag");
		assertEquals("Jag", p.getName());
		assertEquals(100.00, p.getBalance());
		assertEquals(1, p.getNumOfWins());
	}
	
	@Test
	public void testFormat() {
		Player p = new Player("Brogan", 100.00, 1);
		assertEquals("Brogan,100.0,1", p.format());
	}

	
}
