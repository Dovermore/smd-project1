package automail;

import exceptions.InvalidAddItemException;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;

import java.util.*;

/**
 * The robot delivers mail!
 */
public class Robot implements IRobot {
    /** Possible states the robot can be in */
    private IMailDelivery delivery;
    private IMailPool mailPool;

    private final String id;
    private RobotState robotState;
    private TeamState teamState;
    private int currentFloor;
    private boolean receivedDispatch;

    private MailItem deliveryItem = null;
    private MailItem tube = null;

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IMailDelivery delivery, IMailPool mailPool) {
    	id = "R" + hashCode();
    	robotState = RobotState.RETURNING;
    	teamState = TeamState.SINGLE;
        currentFloor = Building.MAILROOM_LOCATION;
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
    }
    
    public void dispatch() {
    	receivedDispatch = true;
    }

    @Override
    public void startDelivery() {receivedDispatch = false;}

    /**
     * This is called on every time step
     */
    public ArrayList<IRobot> step() {
        return robotState.step(this);
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    public void moveTowards(int destination) {
        if(currentFloor < destination){
            currentFloor++;
        } else {
            currentFloor--;
        }
    }
    
    private String getIdTube() {
    	return String.format("%s(%1d)", id, (tube == null ? 0 : 1));
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    public void changeState(RobotState nextState){
        // Cannot be holding mail in tube but not mail in hand!
    	assert(!(deliveryItem == null && tube != null));
    	if (robotState != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), robotState, nextState);
    	}
    	robotState = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }

	static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<>();

	private void addToHand(MailItem mailItem) throws ItemTooHeavyException {
		assert(deliveryItem == null);
		deliveryItem = mailItem;
		if (teamState.validWeight() < mailItem.getWeight()) throw new ItemTooHeavyException();
	}

	private void addToTube(MailItem mailItem) throws ItemTooHeavyException {
		assert(tube == null);
        tube = mailItem;
		if (teamState.validWeight() < mailItem.getWeight()) throw new ItemTooHeavyException();
	}

    @Override
    public ArrayList<MailItem> listMailItems() {
        ArrayList<MailItem> mailItems = new ArrayList<>();
        if (deliveryItem != null) {
            mailItems.add(deliveryItem);
        }
        if (tube != null) {
            mailItems.add(tube);
        }
        return mailItems;
    }

    @Override
    public ArrayList<Robot> listRobots() {
        ArrayList<Robot> robots = new ArrayList<>();
        robots.add(this);
        return robots;
    }

    @Override
    public boolean canAddMailItem(MailItem mailItem) {
	    // correct weight
	    if (teamState.validWeight() < mailItem.getWeight()) {
	        return false;
        }
	    // Return have space for item
        return (deliveryItem == null | tube == null);
    }

    @Override
    public void addMailItem(MailItem mailItem) throws InvalidAddItemException, ItemTooHeavyException {
	    if (deliveryItem == null) {
	        addToHand(mailItem);
        } else if (tube == null) {
	        addToTube(mailItem);
        } else {
	        throw new InvalidAddItemException();
        }
    }

    @Override
    public boolean canDispatch() {
	    return receivedDispatch;
    }

    @Override
    public void deliver() {
	    delivery.deliver(deliveryItem);
	    deliveryItem = null;
    }

    public void clearDeliveryItem() {
	    deliveryItem = null;
    }

    @Override
    public MailItem getCurrentMailItem() {
	    return deliveryItem;
    }

    @Override
    public boolean hasNextMailItem() {
	    return tube != null;
    }

    @Override
    public void loadNextMailItem() {
	    try {
	        addToHand(tube);
	        tube = null;
        } catch (ItemTooHeavyException e) {
	        e.printStackTrace();
        }
    }

    @Override
    public int getFloor() {
	    return currentFloor;
    }

    @Override
    public void registerWaiting() {
	    mailPool.registerWaiting(this);
    }

    @Override
    public int hashCode() {
        Integer hash0 = super.hashCode();
        Integer hash = hashMap.get(hash0);
        if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
        return hash;
    }

    @Override
    public String getId() {
	    return this.id;
	}

    @Override
    public ArrayList<IRobot> availableIRobots() {
	    return new ArrayList<>(Collections.singletonList(this));
    }

    @Override
    public RobotState getRobotState() {
        return robotState;
    }

    @Override
    public TeamState getTeamState() {
	    return teamState;
    }

    @Override
    public void changeTeamState(TeamState teamState) {
	    this.teamState = teamState;
    }

    @Override
    public boolean canStartDelivery() {
        return receivedDispatch;
    }

    @Override
    public void printIRobot() {
        System.out.println(String.format("Robot:: %s", getId()));

        if (deliveryItem != null) {
            System.out.print(String.format("%s####", deliveryItem.toString()));
        }

        if (tube!=null) {
            System.out.print(tube.toString());
        }

        System.out.println();
    }
}
