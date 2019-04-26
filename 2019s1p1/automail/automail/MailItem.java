package automail;

import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a mail item
 */
public class MailItem {
	
    /** Represents the destination floor to which the mail is intended to go */
    private final int destinationFloor;
    /** The mail identifier */
    private final String id;
    /** The time the mail item arrived */
    private final int arrivalTime;
    /** The weight in grams of the mail item */
    private final int weight;

    /**
     * Constructor for a MailItem
     * @param destinationFloor the destination floor intended for this mail item
     * @param arrivalTime the time that the mail arrived
     * @param weight the weight of this mail item
     */
    public MailItem(int destinationFloor, int arrivalTime, int weight){
        this.destinationFloor = destinationFloor;
        this.id = String.valueOf(hashCode());
        this.arrivalTime = arrivalTime;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return String.format("Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d",
                getId(), getArrivalTime(), getDestinationFloor(), getWeight());
    }
    
    /** The mail identifier
     *
     * @return the ID of the mail item
     */
    public String getId() {
        return id;
    }

    /** The weight in grams of the mail item
     *
     * @return the weight of the mail item
     */
    public int getWeight(){
       return weight;
    }
   
	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

	@Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}

    /** Represents the destination floor to which the mail is intended to go */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /** The time the mail item arrived */
    public int getArrivalTime() {
        return arrivalTime;
    }
}
