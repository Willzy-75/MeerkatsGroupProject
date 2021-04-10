package s21_Meerkat_Project;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Bids {
	// fields
	private double currentBid;
	private double maxBid;
	private double increment;
	private LocalDateTime endBy;
	private LocalDateTime startBy;
	private User winner;
	private Puppies pup;
	private boolean active;
	private boolean paidFor = false;// will be used for checkpoint 6
	private Queue<String> backlogg;// this is where backlogg is stored for checkpoint 2
	private Queue<String> bidHistory;

	public Bids() {

	}

	public Bids(Puppies pup, LocalDateTime end, LocalDateTime start, double currentBid, double maxBid, double increment,
			User winner, boolean active, boolean paidFor)// will be to bring in bids already made before
	{
		this.pup = pup;
		this.endBy = end;
		this.startBy = start;
		this.maxBid = maxBid;
		this.currentBid = currentBid;
		this.increment = increment;
		this.winner = winner;
		this.active = active;
		this.paidFor = paidFor;
		// temp till save fix
		this.backlogg = new Queue<>();
		this.bidHistory = new Queue<>();
	}

	public Bids(Puppies pup, LocalDateTime end) {// initial creation of an auction/bid
		this.pup = pup;
		currentBid = pup.getPrice();
		maxBid = 0;
		if (pup.getPrice() < 1000) {
			increment = 10;
		} else {
			increment = 50;
		}
		startBy = LocalDateTime.now().withSecond(0).withNano(0);
		endBy = end;

		winner = new User("null", "apple", 'C'); // make sure this is fixed see checkBid method *)$
		active = true;

		this.backlogg = new Queue<>();
		this.bidHistory = new Queue<>();
	}

	public String toString() {
		String win = " ";
		if (this.winner.getUserName().equalsIgnoreCase("null")) {
			win = "no one";
		} else {
			win = this.winner.getUserName().toString();
		}
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(this.pup.getName() + " is ");
		if (active == false) {
			sb.append("not on auction. The winner was " + win + ". The auction ended on " + endBy
					+ " and the final price was " + nf.format(getCurrentBid()) + "." + " Paid for: " + paidFor);
		} else {
			sb.append("on auction for " + nf.format(currentBid) + ". The current winner of the auction is " + win
					+ ". This auction started on " + startBy + " and ends on " + endBy + ".");
		}
		String bidString = sb.toString();
		return bidString;
	}

	public String toStringF() {
		return this.getPup().getName() + "|" + dateToString(startBy) + "|" + dateToString(endBy) + "|" + currentBid
				+ "|" + maxBid + "|" + increment + "|" + winner.getUserName() + "|" + active + "|" + paidFor + "|"
				+ backlogg.size() + "|" + bidHistory.size();
	}

	public String dateToString(LocalDateTime ldt) {
		String year, month, day, hour, min;
		String date = ldt.toString();
		year = date.substring(0, 4);
		month = date.substring(5, 7);
		day = date.substring(8, 10);
		hour = date.substring(11, 13);
		min = date.substring(14, 16);
		return year + month + day + hour + min;
	}

	// use for the first bidder
	private void newBHQueue(User u, double newBid) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String bh = (pup.getName() + "'s Bid History");
		bidHistory.enqueue(bh);
		bh = ("Bidder \t\tResult \t\t\tWinner\t\tBid\t\tCurrent Price \t\tMax willing to pay");
		bidHistory.enqueue(bh);
		bh = (u.getUserName() + "\t\tFirst bid\t\t" + u.getUserName() + "\t\t"+nf.format(newBid)+"\t\t" + nf.format(currentBid) + "\t\t\t"
				+ nf.format(maxBid));
		bidHistory.enqueue(bh);
	}

	// use if the same customer increases their bid, if they are already winning
	private void updateBHQueue(User u, double newBid) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String bh = (u.getUserName() + "\t\tUpdated bid \t\t" + winner.getUserName() + "\t\t" +nf.format(newBid)+ "\t\t" + nf.format(currentBid)
				+ "\t\t\t" + nf.format(maxBid));
		bidHistory.enqueue(bh);
	}

	// use if a new user wins
	private void newWinBHQueue(User u, double newBid) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String bh = (u.getUserName() + "\t\tNew winner \t\t" + winner.getUserName() + "\t\t" +nf.format(newBid)+ "\t\t" + nf.format(currentBid)
				+ "\t\t\t" + nf.format(maxBid));
		bidHistory.enqueue(bh);
	}

	// use if there is a new bid, but it isn't enough to win
	private void notEnoughBHQueue(User u, double newBid) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String bh = (u.getUserName() + "\t\tRejected (too low) \t" + winner.getUserName() + "\t\t" +nf.format(newBid)+ "\t\t"
				+ nf.format(currentBid) + "\t\t\t" + nf.format(maxBid));
		bidHistory.enqueue(bh);
	}

	public void checkBid(User cust, double newBid) {
		
		if (winner.getUserName().equalsIgnoreCase("null")) { // do this if the bid is the first bid
			winner = cust;
			maxBid = newBid;
			newBHQueue(cust, newBid);

		} else if (winner == cust) { // do this if the customer is upping their bid on the same dog
			if (newBid>maxBid) { // if the bid is more than the current max bid
				maxBid = newBid;
				updateBHQueue(cust, newBid);
			} else if (newBid<=maxBid){				// if the bid is less than or equals to the max bid
				notEnoughBHQueue(cust, newBid);
			}

		} else {									// if neither of the above situations apply then do this:
			if (newBid <= maxBid) {					// if the newBid is less than or equals to maxBid
				currentBid = newBid;
				notEnoughBHQueue(cust, newBid);
			} else if (newBid > maxBid) {			// if the newBid is greater than maxBid
				winner = cust;
				currentBid = maxBid+increment;
				maxBid = newBid;
				newWinBHQueue(cust, newBid);
			}
		}
	}

	public void storeBid(User cust, double newBid) {
		// Variable Declaration
		String savedBid = cust.getUserName() + " " + newBid;
		backlogg.enqueue(savedBid);
	}

	public double getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(double currentBid) {
		this.currentBid = currentBid;
	}

	public double getMaxBid() {
		return maxBid;
	}

	public void setMaxBid(double maxBid) {
		this.maxBid = maxBid;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}

	public LocalDateTime getEndBy() {
		return endBy;
	}

	public void setEndBy(LocalDateTime endBy) {
		this.endBy = endBy;
	}

	public LocalDateTime getStartBy() {
		return startBy;
	}

	public void setStartBy(LocalDateTime startBy) {
		this.startBy = startBy;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public Puppies getPup() {
		return pup;
	}

	public void setPup(Puppies pup) {
		this.pup = pup;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPaidFor() {
		return paidFor;
	}

	public void setPaidFor(boolean paidFor) {
		this.paidFor = paidFor;
	}

	public Queue<String> getBacklogg() {
		return backlogg;
	}

	public void setBacklogg(Queue<String> backlogg) {
		this.backlogg = backlogg;
	}

	public Queue<String> getBidHistory() {
		return bidHistory;
	}

	public void setBidHistory(Queue<String> bidHistory) {
		this.bidHistory = bidHistory;
	}

}
