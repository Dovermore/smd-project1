package strategies;

import automail.IMailDelivery;
import automail.IRobot;
import automail.RobotFactory;
import exceptions.InvalidDispatchException;

import java.util.ArrayList;

public class Automail {
	      
    private ArrayList<IRobot> currentStepIRobotList;
    private ArrayList<IRobot> nextStepIRobotList;
    private IMailPool mailPool;

    /**
     * create AutoMail with two components (mail pool and robots) in the system
     * @param mailPool: MailPool in the system
     * @param delivery: Confirm the delivery and calculate the total score
     * @param numRobots: number of robots in the building
     * */
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRobots) {
    	// Swap between simple provided strategies and your strategies here

    	/* Initialize the MailPool */
    	this.mailPool = mailPool;
    	
    	/* Initialize robots */
        currentStepIRobotList = new ArrayList<>();
        nextStepIRobotList = new ArrayList<>();
        /* initial robot in returning state to mail room */
    	for (int i = 0; i < numRobots; i++) {
            currentStepIRobotList.add(RobotFactory.getInstance().createRobot(mailPool, delivery));
        }
    }

    /**
     * step every component in the system
     * */
    public void step() throws InvalidDispatchException {
        // Add robots just got loaded
        ArrayList<IRobot> dispatchedRobots = this.mailPool.step();
        currentStepIRobotList.addAll(dispatchedRobots);
        currentStepIRobotList.sort(IRobot.IRobotComparator);

        for (IRobot currentIRobot: this.currentStepIRobotList) {
            ArrayList<IRobot> activeRobots = currentIRobot.step();
            nextStepIRobotList.addAll(activeRobots);
        }

        this.currentStepIRobotList = this.nextStepIRobotList;
        this.nextStepIRobotList = new ArrayList<>();
    }

    /** return the mail pool in the auto mail */
    public IMailPool getMailPool() {return mailPool;}
}
