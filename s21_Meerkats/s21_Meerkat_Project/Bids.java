package s21_Meerkat_Project;

import java.text.NumberFormat;
import java.time.LocalDateTime;

public class Bids {

	private double currentBid;

	private double maxBid;

	private double increment;

	private LocalDateTime endBy;

	private LocalDateTime startBy;

	private User winner;

	private Puppies pup;

	private boolean active;

	public Bids() {

	}

	public Bids(Puppies pup, LocalDateTime end, LocalDateTime start , double currentBid, double maxBid, double increment, User winner, boolean active)//will be to bring in bids already made before
	{
		this.pup = pup;
		this.endBy = end;
		this.startBy = start;
		this.maxBid = maxBid;
		this.currentBid = currentBid;
		this.increment = increment;
		this.winner = winner;
		this.active = active;
	}
	
	public Bids(Puppies pup, LocalDateTime end) {//initial creation of an auction/bid
		this.pup = pup;
		currentBid = pup.getPrice();
		maxBid = 0;
		if (pup.getPrice() < 1000) {
			increment = 10;
		} else {
			increment = 50;
		}
		startBy = LocalDateTime.now();
		endBy = end;

		winner = new User("no one", "apple", 'C'); // make sure this is fixed see checkBid method 
		active = true;
	}
	
	public String toString() { 
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(this.pup.getName() + " is ");
		if (active == false) {
			sb.append("not on auction. The winner was " + this.winner.getUserName() + ". The auction ended on " + endBy
					+ " and the final price was " + nf.format(getCurrentBid()) + ".");
		} else {
			sb.append("on auction for " + nf.format(currentBid) + ". The current winner of the auction is "
					+ this.winner.getUserName() + ". This auction started on " + startBy + " and ends on " + endBy
					+ ".");
		}
		String bidString = sb.toString();
		return bidString;
	}

	public String toStringF() {
				
		return this.getPup().getName()+"|"+dateToString(startBy)+"|"+dateToString(endBy)+"|"+currentBid+"|"+maxBid+"|"+winner.getUserName()+"|"+active;
	}
	
	public String dateToString(LocalDateTime ldt) {
		String year, month, day, hour, min;
		String date = ldt.toString();
		year = date.substring(0, 4);
		month = date.substring(5, 7);
		day = date.substring(8, 10);
		hour = date.substring(11,13);
		min = date.substring(14,16);
		return year+month+day+hour+min;
	}
	
		
	public void checkBid(User cust, double newBid) {
		if (winner == null) { // fix from constructor change
			winner = cust;
			maxBid = newBid;
		} else if (winner == cust) {
			maxBid = newBid;
		} else {
			if (newBid > currentBid + increment && newBid < maxBid + increment) {
				currentBid = currentBid + increment;
			} else if (newBid > maxBid + increment) {
				winner = cust;
				currentBid = maxBid + increment;
				maxBid = newBid;
			}
		}
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

}
