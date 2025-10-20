package mru.game.controller;

import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//import jdk.internal.classfile.instruction.SwitchCase;
import mru.game.model.*;
import mru.game.view.*;

public class GameManager {

	/*
	 * In this class toy'll need these methods: A constructor A method to load the
	 * txt file into an arraylist (if it exists, so you check if the txt file exists
	 * first) A save method to store the arraylist into the the txt file A method to
	 * search for a player based their name A method to find the top players
	 * Depending on your designing technique you may need and you can add more
	 * methods here
	 */

	private final String FILE_PATH = "res/CasinoInfo.txt";
	ArrayList<Player> players;
	AppMenu appMen;

	public GameManager() throws Exception {
		players = new ArrayList<>();
		appMen = new AppMenu();
		loadData();
		lunchApplication();
	}

	private void lunchApplication() throws IOException {

		boolean flag = true;

		while (flag) {
			char option = appMen.showMainMenu();
			switch (option) {
			case 'p':
				playGame();
				break;
			case 's':
				Search();
				break;
			case 'e':
				Exit();
				flag = false;
				break;
			default:
				System.out.println("Please chose one of the three options");

			}
		}

	}

	private void playGame() {
		String name = appMen.promptName();
		Player currentPlayer = searchByName(name);

		// If player doesn't exist, create new player
		if (currentPlayer == null) {
			currentPlayer = new Player(name, 100.0, 0);
			players.add(currentPlayer);
			System.out.println("******************************");
			System.out.println("*** Welcome " + name.toUpperCase() + "! Your starting balance is: 100 $ ***");
			System.out.println("******************************");
		} else {
			System.out.println("******************************");
			System.out.println("*** Welcome back " + name.toUpperCase() + " --- Your balance is: "
					+ currentPlayer.getBalance() + " $ ***");
			System.out.println("******************************");
		}

		// Check if player has sufficient funds
		if (currentPlayer.getBalance() < 2) {
			System.out.println("Insufficient funds! Minimum bet is $2. Returning to main menu.");
			return;
		}

		// Create card deck and start the game
		CardDeck deck = new CardDeck();
		BlackjackGame game = new BlackjackGame(currentPlayer, deck, appMen);
		game.playGame();
	}

	private void Search() {
		while (true) {
			char option = appMen.showSubMenu();

			switch (option) {
			case 't': // Top player
				TopPlayer();
				appMen.promptEnter();
				break;
			case 'n': // Search by name
				searchPlayerByName();
				break;
			case 'b': // Back to main menu
				return; // Exit search menu
			default:
				System.out.println("Please choose one of the three options");
			}
		}
	}

	private void searchPlayerByName() {
        String name = appMen.promptName();
        Player foundPlayer = searchByName(name);
        appMen.showPlayer(foundPlayer);
    }
	
	 private Player searchByName(String name) {
	        for (Player p : players) {
	            if (p.getName().equals(name)) {
	                return p;
	            }
	        }
	        return null;
	    }
	

	private void TopPlayer() {
		if (players.isEmpty()) {
			System.out.println("No players found in the database.");
			return;
		}

		// Find the maximum number of wins
		int maxWins = 0;
		for (Player p : players) {
			if (p.getNumOfWins() > maxWins) {
				maxWins = p.getNumOfWins();
			}
		}

		// Find all players with the maximum number of wins
		ArrayList<Player> topPlayers = new ArrayList<>();
		for (Player p : players) {
			if (p.getNumOfWins() == maxWins) {
				topPlayers.add(p);
			}
		}

		// Display the top players
		displayTopPlayers(topPlayers);
	}

	/**
	 * Displays the top players in a formatted table
	 */
	private void displayTopPlayers(ArrayList<Player> topPlayers) {
		System.out.println("\n    - TOP PLAYERS -");
		System.out.println("+-----------+---------+");
		System.out.println("| NAME      | # WINS  |");
		System.out.println("+-----------+---------+");

		for (Player p : topPlayers) {
			System.out.printf("| %-9s | %-7d |%n", p.getName(), p.getNumOfWins());
		}

		System.out.println("+-----------+---------+");
	}

	private void Exit() throws FileNotFoundException {
		System.out.println("Saving...");
		File db = new File(FILE_PATH);
		PrintWriter pw = new PrintWriter(db);

		for (Player p : players) {
			pw.println(p.format());
		}
		pw.close();

		System.out.println("Done! Please visit us again!");

	}

	private void loadData() throws Exception {
		File db = new File(FILE_PATH);
		String currentLine;
		String[] splittedLine;

		if (db.exists()) {
			Scanner fileReader = new Scanner(db);

			while (fileReader.hasNextLine()) {
				currentLine = fileReader.nextLine();
				splittedLine = currentLine.split(",");
				Player p = new Player(splittedLine[0], Double.parseDouble(splittedLine[1]), Integer.parseInt(splittedLine[2]));
				players.add(p);
			}

			fileReader.close();

		}

	}
}
