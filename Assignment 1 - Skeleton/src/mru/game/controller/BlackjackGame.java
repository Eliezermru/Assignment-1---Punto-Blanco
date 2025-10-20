package mru.game.controller;

import java.util.ArrayList;
import java.util.Scanner;
import mru.game.model.Player;
import mru.game.view.AppMenu;

public class BlackjackGame {

	/**
	 * In this class you implement the game You should use CardDeck class here See
	 * the instructions for the game rules
	 */
	private Player currentPlayer;
	private CardDeck deck;
	private ArrayList<Card> playerHand;
	private ArrayList<Card> dealerHand;
	private double currentBet;
	private Scanner input;
	private AppMenu appMenu;

	/**
	 * Constructor for BlackjackGame
	 * 
	 * @param player  The player who is playing the game
	 * @param deck    The card deck to use
	 * @param appMenu The app menu for user interaction
	 */
	public BlackjackGame(Player player, CardDeck deck, AppMenu appMenu) {
		this.currentPlayer = player;
		this.deck = deck;
		this.appMenu = appMenu;
		this.input = new Scanner(System.in);
		this.playerHand = new ArrayList<>();
		this.dealerHand = new ArrayList<>();
	}

	/**
	 * Starts the Blackjack game
	 */
	public void playGame() {
		if (currentPlayer.getBalance() < 2) {
			System.out.println("Insufficient funds! Minimum bet is $2. Returning to main menu.");
			return;
		}

		boolean playAgain = true;

		while (playAgain && currentPlayer.getBalance() >= 2) {
			System.out.println("\nCurrent balance: $" + currentPlayer.getBalance());

			// Get bet amount
			currentBet = getBetAmount();

			// Deal cards according to official rules
			dealCardsProperly();

			// Check for naturals (blackjack)
			boolean playerBlackjack = isNatural(playerHand);
			boolean dealerBlackjack = isNatural(dealerHand);

			// Show initial state with dealer's second card hidden
			displayGameState(false);

			
			if (playerBlackjack || dealerBlackjack) {
				resolveNaturals(playerBlackjack, dealerBlackjack);
			} else {
				//Player's turn
				playerTurn();

				// Dealer's turn only if player didn't bust
				if (calculateHandValue(playerHand) <= 21) {
					dealerTurn();
				}

				// Determine winner and update balance
				determineWinner();
				break;
			}

			// Display updated balance
			System.out.println("Updated balance: $" + currentPlayer.getBalance());

			playAgain = askToPlayAgain();
			
			// Ask if player wants to play again
			if (currentPlayer.getBalance() >= 2 && playAgain) {
				System.out.println("Insufficient funds to continue playing.");
				playAgain = false;
			} 

			// Clear hands for next round
			playerHand.clear();
			dealerHand.clear();
		}
	}
	private void resolveNaturals(boolean playerBlackjack, boolean dealerBlackjack) {
		{
		    displayGameState(true); // Reveal dealer’s hidden card

		    if (playerBlackjack && dealerBlackjack) {
		        System.out.println("\nBoth have Blackjack! It's a tie.");
		        // No change in balance
		    } else if (playerBlackjack) {
		        double winnings = currentBet * 2;
		        System.out.println("\nBlackjack! You win $" + winnings);
		        currentPlayer.setBalance(currentPlayer.getBalance() + winnings);
		        currentPlayer.setNumOfWins(currentPlayer.getNumOfWins() + 1);
		    } else if (dealerBlackjack) {
		        System.out.println("\nDealer has Blackjack! You lose $" + currentBet + ".");
		        currentPlayer.setBalance(currentPlayer.getBalance() - currentBet);
		    }
		}
	}

	private boolean isNatural(ArrayList<Card> hand) {
		return calculateHandValue(hand) == 21 && hand.size() == 2;	}

	//}

	/**
	 * Deals cards according to official Blackjack rules: 1. Player gets one card
	 * face up 2. Dealer gets one card face up 3. Player gets second card face up 4.
	 * Dealer gets second card face down
	 */
	private void dealCardsProperly() {
		// First card to player (face up)
		playerHand.add(deck.drawCard());

		// First card to dealer (face up)
		dealerHand.add(deck.drawCard());

		// Second card to player (face up)
		playerHand.add(deck.drawCard());

		// Second card to dealer (face down)
		dealerHand.add(deck.drawCard());
	}

	private boolean isTenCard(Card card) {
	int rank = card.getRank();
	 return rank == 10 || rank == 11 || rank == 12 || rank == 13;
	}

	/**
	 * Gets the bet amount from the player with validation
	 * 
	 * @return The validated bet amount
	 */
	private double getBetAmount() {
		double bet = 0;
		boolean validBet = false;

		while (!validBet) {
			System.out.print("How much do you want to bet this round? ");
			try {
				bet = Double.parseDouble(input.nextLine());

				if (bet < 2) {
					System.out.println("Minimum bet is $2. Please enter a higher amount.");
				} else if (bet > currentPlayer.getBalance()) {
					System.out
							.println("You cannot bet more than your current balance of $" + currentPlayer.getBalance());
				} else {
					validBet = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}

		return bet;
	}

	/**
	 * Handles the player's turn with hit/stand options
	 */
	private void playerTurn() {
		boolean playerStand = false;

		while (!playerStand && calculateHandValue(playerHand) <= 21) {
			int handValue = calculateHandValue(playerHand);
			boolean isSoftHand = isSoftHand(playerHand);

			// Inform player about soft hand if applicable
			if (isSoftHand) {
				//int softValue = calculateSoftHandValue(playerHand);
				//System.out.println("You have a soft " + softValue + " (can be " + handValue + ")");
			} else {
				System.out.println("Your hand value: " + handValue);
			}

			System.out.println("Select an option:");
			System.out.println("1. Hit");
			System.out.println("2. Stand");
			System.out.print("Your choice: ");

			String choice = input.nextLine().trim();

			if (choice.equals("1")) {
				// Player chooses to hit
				playerHand.add(deck.drawCard());
				displayGameState(false);

				if (calculateHandValue(playerHand) > 21) {
					System.out.println("Bust! You went over 21.");
				}
			} else if (choice.equals("2")) {
				// Player chooses to stand
				playerStand = true;
			} else {
				System.out.println("Invalid choice. Please enter 1 or 2.");
			}
		}
	}

	/**
	 * Handles the dealer's turn according to official rules
	 */
	private void dealerTurn() {
		System.out.println("\nDealer's turn:");
		displayGameState(true); // Reveal dealer's hidden card

		while (true) {
			int dealerValue = calculateHandValue(dealerHand);
			boolean isSoft = isSoftHand(dealerHand);

			System.out.println("Dealer's hand value: " + dealerValue + (isSoft ? " (soft hand)" : ""));

			// Dealer must stand on 17 or more
			if (dealerValue >= 17) {
				// Special case: soft 17 where Ace as 11 gives exactly 17
				if (dealerValue == 17 && isSoft) {
					int softValue = calculateHandValue(dealerHand);
					if (softValue == 17) {
						System.out.println("Dealer has soft 17 and must stand.");
						break;
					}
				} else {
					System.out.println("Dealer stands.");
					break;
				}
			}

			// Dealer must hit on 16 or less
			System.out.println("Dealer hits...");
			dealerHand.add(deck.drawCard());
			displayGameState(true);

			// Check if dealer busted
			if (calculateHandValue(dealerHand) > 21) {
				System.out.println("Dealer busts!");
				break;
			}
			
		break;
		}
	}

	
	private int calculateHandValue(ArrayList<Card> hand) {
		int value = 0;
		int aces = 0;

		for (Card card : hand) {
			int rank = card.getRank();
			if (rank == 1) {
				value += 11;
				aces++;
			} else if (rank >= 10) {
				value += 10;
			} else {
				value += rank;
			}
		}

		while (value > 21 && aces > 0) {
			value -= 10;
			aces--;
		}

		return value;
	}

	/**
	 * Determines the winner and updates player balance
	 */
	private void determineWinner() {
		int playerValue = calculateHandValue(playerHand);
		int dealerValue = calculateHandValue(dealerHand);

		System.out.println("\nFinal Results:");
		System.out.println("Player: " + playerValue + " | Dealer: " + dealerValue);

		if (playerValue > 21) {
			System.out.println("You busted! You lose $" + currentBet);
			currentPlayer.setBalance(currentPlayer.getBalance() - currentBet);
		} else if (dealerValue > 21) {
			System.out.println("Dealer busted! You win $" + currentBet);
			currentPlayer.setBalance(currentPlayer.getBalance() + currentBet);
			currentPlayer.setNumOfWins(currentPlayer.getNumOfWins() + 1);
		} else if (playerValue > dealerValue) {
			System.out.println("You win $" + currentBet);
			currentPlayer.setBalance(currentPlayer.getBalance() + currentBet);
			currentPlayer.setNumOfWins(currentPlayer.getNumOfWins() + 1);
		} else if (playerValue < dealerValue) {
			System.out.println("You lose $" + currentBet);
			currentPlayer.setBalance(currentPlayer.getBalance() - currentBet);
		} else {
			System.out.println("It's a tie! Your bet is returned.");
			// No change to balance for ties
		}
	}

	/**
	 * Checks if hand is soft (contains Ace counted as 11 without busting)
	 * 
	 * @param hand The hand to check
	 * @return true if the hand is soft
	 */
	private boolean isSoftHand(ArrayList<Card> hand) {
		int valueWithAceAs1 = 0;
		int aces = 0;

		for (Card card : hand) {
			int rank = card.getRank();
			if (rank == 1) {
				valueWithAceAs1 += 1;
				aces++;
			} else if (rank >= 10) {
				valueWithAceAs1 += 10;
			} else {
				valueWithAceAs1 += rank;
			}
		}

		// If we have aces and counting one as 11 doesn't bust, it's a soft hand
		return aces > 0 && (valueWithAceAs1 + 10) <= 21;
	}

	/**
	 * Displays the current game state
	 * 
	 * @param showDealerCard Whether to show dealer's hidden card
	 */
	private void displayGameState(boolean showDealerCard) {
		
		String player = "PLAYER";
		String dealer = "DEALER";
		
		System.out.println("\n-- BLACKJACK --");
		System.out.printf("+%s+%s+\n", "=".repeat(23), "=".repeat(23));
		System.out.printf("||%-23s|%-23s||\n", player, dealer);
		System.out.printf("+%s+%s+\n", "=".repeat(23), "=".repeat(23));
	
		int maxRows = Math.max(playerHand.size(), dealerHand.size());

		for (int i = 0; i < maxRows; i++) {
			String playerCard = (i < playerHand.size()) ? playerHand.get(i).toString() : "";
			String dealerCard;

			// Dealer's second card is hidden until reveal
			if (i == 1 && !showDealerCard && dealerHand.size() > 1) {
				dealerCard = "[Hidden Card]";
			} else {
				dealerCard = (i < dealerHand.size()) ? dealerHand.get(i).toString() : "";
			}

			System.out.printf("| %-17s | %-17s |%n", playerCard, dealerCard);
		}

		System.out.println("+-------------------+-------------------+");

		// Show player total always, dealer total only when revealed
		System.out.println("Player Total: " + calculateHandValue(playerHand));
		if (showDealerCard) {
			System.out.println("Dealer Total: " + calculateHandValue(dealerHand));
		}
	}

	/**
	 * Asks the player if they want to play another round
	 * 
	 * @return true if player wants to play again, false otherwise
	 */
	private boolean askToPlayAgain() {
		System.out.print("\nDo you want to continue (y/n)? ");
		String response = input.nextLine().trim().toLowerCase();
		return response.equals("y") || response.equals("yes");
	}
}
