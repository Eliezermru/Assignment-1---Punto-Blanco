package mru.game.view;

import java.util.Scanner;

import mru.game.model.Player;

public class AppMenu {

	Scanner input;

	public AppMenu() {
		input = new Scanner(System.in);
	}

	/**
	 * This class will be used to show the menus and sub menus to the user It also
	 * prompts the user for the inputs and validates them
	 */
	public char showMainMenu() {
		System.out.println("Select one option:\n");
		System.out.println("\t(P) Play Game");
		System.out.println("\t(S) Search");
		System.out.println("\t(E) Save and Exit\n");
		System.out.print("Enter a choice:");
		char option = input.nextLine().toLowerCase().charAt(0);
		return option;
	}

	public char showSubMenu() {
		System.out.println("Select one option:\n");
		System.out.println("\t(T) Top Palyer (Most number of wins)");
		System.out.println("\t(N) Looking far a Name");
		System.out.println("\t(B) Back to Main Menu\n");
		System.out.print("Enter a choice:");
		char sub = input.nextLine().toLowerCase().charAt(0);
		return sub;
	}

	public String promptName() {
		System.out.println("What is your name:");
		String name = input.nextLine().trim();
		return name;
	}

	public void showPlayer(Player ply) {
		if (ply == null) {
			System.out.println("Player not found in the database.");
		} else {
			System.out.println("\n    - PLAYER INFO -");
			System.out.println("+-----------+---------+----------+");
			System.out.println("| NAME      | # WINS  | BALANCE  |");
			System.out.println("+-----------+---------+----------+");
			System.out.printf("| %-9s | %-7d | %-8.0f $|%n", ply.getName(), ply.getNumOfWins(), ply.getBalance());
			System.out.println("+-----------+---------+----------+");
		}
		promptEnter();
	}

	public void promptEnter() {
		System.out.print("Press \"Enter\" to continue...");
		input.nextLine();
	}

	public static void emptyDeck() { // prints a message when the deck is empty
		System.out.println("Deck empty; shuffling new deck...");
	}

}
