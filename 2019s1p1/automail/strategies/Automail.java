package strategies;

import automail.IMailDelivery;
import automail.Robot;
import automail.RobotFactory;

public class Automail {
	      
    private Robot[] robots;
    private IMailPool mailPool;
    
    public Automail(IMailPool mailPool, IMailDelivery delivery, int numRobots) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	
    	this.setMailPool(mailPool);
    	
    	/** Initialize robots */
    	setRobots(new Robot[numRobots]);
    	for (int i = 0; i < numRobots; i++) {
    	    robots[i] = RobotFactory.getInstance().createRobot(mailPool, delivery);
        }
    }

    public Robot getRobot(int i) {
        return robots[i];
    }

    public void setRobots(Robot[] robots) {
        this.robots = robots;
    }

    public IMailPool getMailPool() {
        return mailPool;
    }

    public void setMailPool(IMailPool mailPool) {
        this.mailPool = mailPool;
    }
}
