package strategies;

import automail.IMailDelivery;
import automail.IRobot;
import automail.RobotFactory;
import exceptions.InvalidDispatchException;

import java.util.ArrayList;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 14:23:02
 * description: the automail system for the mail delivery
 **/

public class Automail {
    /**
     * the List to store the IRobot to be stepped in current time frame
     * */
    private ArrayList<IRobot> currentStepIRobotList;

    /**
     * the List to store the IRobot to be stepped in next time frame
     * */
    private ArrayList<IRobot> nextStepIRobotList;


    /**
     * the mailPool component
     */
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

    /**
     * @return the mail pool in the auto mail
     * */
    public IMailPool getMailPool() {return mailPool;}
}
