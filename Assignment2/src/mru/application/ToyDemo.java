package mru.application;

import java.util.ArrayList;

public class ToyDemo {

	public static void main(String[] args) {
		
		//Create a list to hold toy objects
		ArrayList<Toy> toys = new ArrayList<>();
		
		//Reaad txt file, read object
		Toy t1 = new Toy1("Type 1"); /// Polymorhpism
		toys.add(t1);
		
		
		Toy t2 = new Toy2(10);
		
		toys.add(t2);
		
		Toy2 tt = (Toy2)t2;
		
		if(toys.get(1) instanceof Toy1) {
			Toy1 tt = (Toy1)toys.get(1);
			System.out.println("Type Toy 1");
		} else {
			Toy2 tt = (Toy2)toys.get(1);
			System.out.println("Type Toy 2");
		}
		
		
		
		
	}
}
