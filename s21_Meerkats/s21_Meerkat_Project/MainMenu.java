package s21_Meerkat_Project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {

	private Scanner scan = new Scanner(System.in);

	public int menu() {
		int value = 0;
		Scanner scan = new Scanner(System.in);
		System.out.println(" Welcome to Puppy Heaven!");
		System.out.println("1. Search puppies: ");
		System.out.println("2. Sign in: ");
		System.out.println("3. Load sample data: (for testing/grading purposes: dynamic dates)"); // Program autoloads sample data for testing purposes
		System.out.println("4. Display active bids: ");
		System.out.println("5. Exit: ");
		System.out.print("Choice: ");
		try {
			value = scan.nextInt();
		} catch (InputMismatchException e) {
			System.out.print("Input Error. ");
			scan.nextLine();
		}
		return value;
	}

	public User menuChoice(int choice, AuctionHouse ah, InputOutputMethods io) {
		User loggedIn = null;

		if (choice == 1) {
			printPups(ah.getAllPups());
		} else if (choice == 2) {
			loggedIn = loginInMenu(ah.getAllUsers());
		} else if (choice == 3) {
			sampleData(ah);
		} else if (choice == 4) {
			ah.activeBids();
		} else if (choice == 5) {
			io.outputData(ah.getAllPups(), ah.getAllUsers(), ah.getAllBids());
			System.out.println("Bye!!!!!");
			System.exit(0);
			;
		} else {
			System.out.println("I don't understand, please enter a number from 1-5? ");
		}

		return loggedIn;
	}

	public void printPups(ArrayList<Puppies> pupList) {
		Scanner scan = new Scanner(System.in);
		int selection = 0;
		System.out.println("Would you prefer to sort by breed or price? \n1. Breed\n2. Price ");
		boolean validNum = false;
		while (!validNum) {
			try {
				selection = scan.nextInt();
				if (selection <= 2 && selection >= 1) {
					validNum = true;
				} else {
					System.out.println("Please enter a number between 1-2: ");
				}
			} catch (InputMismatchException e) {
				System.out.println("Input a number between 1-2: ");
				scan.nextLine();
			}

		}

		scan.nextLine();
		String breed = " ";
		double budget = 0;

		if (selection == 1) {
			System.out.println("What breed are you looking for? ");
			validNum = false;
			while (!validNum) {
				try {
					breed = scan.nextLine();
					validNum = true;
				} catch (InputMismatchException e) {
					System.out.println("Please enter a valid dog breed. ");
					scan.nextLine();
				}

			}

			int counter = 0;

			for (int i = 0; i < pupList.size(); i++) {
				String pupName = (String) pupList.get(i).getBreed();
				if (pupName.equalsIgnoreCase(breed)) {
					System.out.println(pupList.get(i).toString());
					counter++;
				}
			}
			if (counter == 0) {
				System.out.println("We do not have that breed. ");
			}
		} else if (selection == 2) {
			System.out.println("How much is your budget? ");
			validNum = false;
			while (!validNum) {
				try {
					budget = scan.nextDouble();
					validNum = true;
				} catch (InputMismatchException e) {
					System.out.println("Please enter a valid number. ");
					scan.nextLine();
				}
			}

			int counter2 = 0;
			for (int j = 0; j < pupList.size(); j++) {
				if (pupList.get(j).getPrice() <= budget) {
					System.out.println(pupList.get(j).toString());
					counter2++;
				}
			}
			if (counter2 == 0) {
				System.out.println("We don't have anything in your budget. ");
			}
		}

	}

	public User loginInMenu(ArrayList<User> users) {
		// Variable Declaration
		Scanner scan = new Scanner(System.in);
		int choice = 0;
		boolean answer = true;// turns false on a valid answer

		while (answer) {
			// ask user if returning or not
			System.out.println("1. Sign in\n2. Create Customer Account");
			try {
				choice = scan.nextInt();
			} catch (InputMismatchException ime) {
				System.out.println("Invalid input enter and integer from (1-2). Try again");
				scan.nextLine();
			}
			// choose the appropriate menu
			switch (choice) {
			case 1:
				return returningUser(users);
			case 2:
				return createCust(users);// make it so you can return to this method
			}// end of switch
			System.out.println("Input an integer from 1 to 2");
		} // end of while
		return new User();// never Runs

	}

	public User returningUser(ArrayList<User> users) {
		// Variable Declaration
		String userName;
		String password;
		Scanner scan = new Scanner(System.in);
		boolean notSignedIn = true;

		// End when the user enter a valid sign in
		while (notSignedIn) {
			// get userName
			System.out.println("Input Username: ");
			userName = scan.nextLine();
			// get password
			System.out.println("Input password: ");
			password = scan.nextLine();
			// cycle through users
			for (int i = 0; i < users.size(); i++) {
				User u = users.get(i);
				if (u.getUserName().equals(userName) && u.getPassword().equals(password)) {
					// launch menu for whatever type it is
					System.out.println("Welcome " + u.getUserName());
					return u;
					// how are you going to run the menu if its a class?
				}
			}

			System.out.println("Invalid Username or Password. Try again.");
		} // end of while

		return new User();// never runs
	}

	public User createCust(ArrayList<User> users) {
		// Variable Declaration
		Scanner scan = new Scanner(System.in);
		String username;
		String password;
		String address;

		System.out.println("Enter your username:");
		username = scan.nextLine();
		checkValid(username);
		//loop to check for duplicate username and also validate new entry for the word "null" or pipes
		for (int i =0; i< users.size(); i++) {
			if (users.get(i).getUserName().equals(username)) {
				System.out.println("That username is already taken, try a different one. ");
				username = scan.nextLine();
				checkValid(username);
			}
		}
		
		System.out.println("Enter your password:");
		password = scan.nextLine();
		checkValid(password); 
		
		System.out.println("Enter your address so we can ship your purchased products:");
		address = scan.nextLine();
		checkValid(address);
		
		System.out.println("What is your Paypal email address? ");
		String payPal = scan.nextLine();
		checkValid(payPal);

		users.add(new Customer(username, password, address, payPal));
		// sign in now
		return returningUser(users);
	}

	public void sampleData(AuctionHouse ah) {// adds some extra puppies and auctions to the program
		//Check to see if these dogs are already in the database
		if(pupExists("Ginger", ah) || pupExists("Pepper", ah) || pupExists("Red", ah) || pupExists("Weston", ah)) {
			System.out.println("Cannot load sample data one or more dogs already in database.");
		} else {
			Puppies pupA = new Puppies("Ginger", "poodle", "male", true, 2000.0, false);
			Puppies pupB = new Puppies("Pepper", "beagle", "male", true, 1000.0, false);
			Puppies pupC = new Puppies("Red", "German Shepherd", "female", true, 1500.0, false);
			Puppies pupD = new Puppies("Weston", "Alsation", "female", false, 200, true);
			ah.getAllPups().add(pupA);
			ah.getAllPups().add(pupB);
			ah.getAllPups().add(pupC);
			ah.getAllPups().add(pupD);
			// plus different default times for testing purposes Jolly
			// this could be a potential error since it is not referencing the
			ah.addBid(new Bids(findPup("Ginger", ah), LocalDateTime.now().plusHours(2).withSecond(0).withNano(0)));
			ah.addBid(new Bids(findPup("Pepper", ah), LocalDateTime.now().plusMinutes(1).withSecond(0).withNano(0)));
			ah.addBid(new Bids(findPup("Red", ah), LocalDateTime.now().plusMinutes(5).withSecond(0).withNano(0)));
		}//end of else
	}

	// this method is be used only by sample data
	private Puppies findPup(String name, AuctionHouse ah) {
		for (Puppies p : ah.getAllPups()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		} // end of for loop
			// should never run
		System.out.println("Error in findPup.");
		return null;
	}
	
	private boolean pupExists(String name, AuctionHouse ah) {
		for (Puppies p : ah.getAllPups()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return true;
			}
		} // end of for loop
		return false;
	}

	public Puppies addPup(AuctionHouse ah) {
		String name = "";
		String breed = "";
		String sex = "";
		boolean ped = true;
		boolean hypo = true;
		boolean valid = false;
		double price = 0;
		System.out.println("What is the name of the new pup for sale? ");
		while (!valid) {
			try {
				name = scan.nextLine();
				checkValid(name);
				boolean sameName = false;
				for (int i = 0; i < ah.getAllPups().size(); i++) {
					if (name.equalsIgnoreCase(ah.getAllPups().get(i).getName())) {
						System.out.println("There is already a puppy with that name. Please enter a different name: ");
						sameName = true;
					}
				}
				if (sameName == true) {
					valid = false;
				} else {
					valid = true;
				}

			} catch (InputMismatchException e) {
				System.out.println("That isn't a puppy name, try again. ");
				scan.nextLine();
			}
		}
		valid = false;
		System.out.println("What is the breed of the puppy? ");
		while (!valid) {
			try {
				breed = scan.nextLine();
				checkValid(sex);
				valid = true;
			} catch (InputMismatchException e) {
				System.out.println("That isn't a puppy breed, try again. ");
				scan.nextLine();
			}
		}
		valid = false;
		System.out.println("What is the sex of the new puppy (male/female)? ");
		while (!valid) {
			try {
				sex = scan.nextLine();
				checkValid(sex);
				if (sex.equalsIgnoreCase("Male") || sex.equalsIgnoreCase("Female")) {
					valid = true;
				} else {
					System.out.println("I don't understand, please enter male or female. ");
				}
			} catch (InputMismatchException e) {
				System.out.println("I don't understand, please enter male or female. ");
				scan.nextLine();
			}
		}
		valid = false;
		System.out.println("Does the puppy have a pedigree (true/false)? ");
		while (!valid) {
			try {
				ped = scan.nextBoolean();
				if (ped == true || ped == false) {
					valid = true;
				} else {
					System.out.println("I don't understand, please enter true or false. ");
				}
			} catch (InputMismatchException e) {
				System.out.println("I don't understand, please enter true or false. ");
				scan.nextLine();
			}
		}
		valid = false;
		System.out.println("What is the starting price for the puppy? ");
		while (!valid) {
			try {
				price = scan.nextDouble();
				if (price > 0) {
					valid = true;
				} else {
					System.out.println("Please enter a starting price above $0. ");
				}
			} catch (InputMismatchException e) {
				System.out.println("I don't understand, please enter a numerical price. ");
				scan.nextLine();
			}
		}
		valid = false;
		System.out.println("Is the puppy hypoallergenic (true/false)? ");
		while (!valid) {
			try {
				hypo = scan.nextBoolean();
				if (hypo == true || hypo == false) {
					valid = true;
				} else {
					System.out.println("I don't understand, please enter true or false. ");
				}
			} catch (InputMismatchException e) {
				System.out.println("I don't understand, please enter true or false. ");
				scan.nextLine();
			}
		}
		Puppies pup = new Puppies(name, breed, sex, ped, price, hypo);//note removed available
		return pup;
	}
	
	public void checkValid(String input) {
		Scanner scan = new Scanner(System.in);
		
		while (input.contains("|") || input.equals("null")) {
			System.out.println("Your input cannot include the word 'null' or the pipe symbol '|'");
			input = scan.nextLine();
		}
	}

}
