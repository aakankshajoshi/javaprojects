//Akanksha Joshi - akankshj
package hw3;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

//child class of GameRound used to set and get hitCount, missCount
public class HangmanRound extends GameRound {

	private IntegerProperty hitCount;
	private IntegerProperty missCount;

	public HangmanRound() {
		this.hitCount = new SimpleIntegerProperty(); 
		this.missCount = new SimpleIntegerProperty(); 
	}

	public int getHitCount() {
		return this.hitCount.get();
	}

	public void setHitCount(int hitCount) {
		this.hitCount.set(hitCount);
	}

	public IntegerProperty hitCountProperty() {
		return hitCount;

	}

	public int getMissCount() {
		return this.missCount.get();
	}

	public void setMissCount(int missCount) {
		this.missCount.set(missCount);
	}

	public IntegerProperty missCountProperty() {
		return hitCount;

	}
}
