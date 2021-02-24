package s21_Meerkat_Project;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Customer extends User {
	private String payPal;
	private ArrayList<Bids> hiBids; // customer's current high bid
	private String address;

	Customer() {

	}

	Customer(String userName, String password, String address) {
		super(userName, password, 'C');
		this.address = address;
		this.hiBids = new ArrayList<Bids>();
	}

	Customer(String userName, String password, String address, String payPal) {
		super(userName, password, 'C');
		this.address = address;
		this.payPal = payPal;
		this.hiBids = new ArrayList<Bids>();
	}

	public String getPayPal() {
		return payPal;
	}

	public void setPayPal(String payPal) {
		this.payPal = payPal;
	}

	public ArrayList<Bids> getHiBids() {
		return hiBids;
	}

	public void setHiBids(ArrayList<Bids> hiBids) {
		this.hiBids = hiBids;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
}

// make-bid method here: ensure ending date of auction is referenced, get max
// bid from customer, and display global current bid, check if
// bid is sufficient to become current bid, then update current global bid.
// winning bids are auctions you are currently winning
// available bids are auctions you are bidding in, but not currently winning
// global bid is current bid viewable to all

// checkWinBid method

// updateBid method

// view current auctions method
