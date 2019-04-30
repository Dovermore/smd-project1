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
    
    public Automail(IMailDelivery delivery, int numRobots) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/* Initialize the MailPool */
    	mailPool = new MailPool(this);
    	
    	/* Initialize robots */
        currentStepIRobotList = new ArrayList<>();
        nextStepIRobotList = new ArrayList<>();
        /* initial robot in returning state to mail room */
    	for (int i = 0; i < numRobots; i++) {
            currentStepIRobotList.add(RobotFactory.getInstance().createRobot(mailPool, delivery));
        }
    }

    public void step() throws InvalidDispatchException {
        this.mailPool.step();

        for (IRobot currentIRobot: this.currentStepIRobotList) {
            currentIRobot.step();
        }

        this.currentStepIRobotList = this.nextStepIRobotList;
        this.nextStepIRobotList.clear();
    }

    public IMailPool getMailPool() {
        return mailPool;
    }

    public void addIRobot(IRobot robot) {
        assert robot != null;
    }
}
