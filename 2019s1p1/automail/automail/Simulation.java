package automail;

import exceptions.*;
import strategies.Automail;
import strategies.MailPool;
import strategies.SelectMailItemToDeliverPlan;
import strategies.SelectRobotToDeliverPlan;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {
	
    /** Constant for the mail generator */
    private static int MAIL_TO_CREATE;
    private static int MAIL_MAX_WEIGHT;
    

    private static ArrayList<MailItem> MAIL_DELIVERED;
    private static double total_score = 0;

    public static void main(String[] args)
			throws IOException, NotEnoughRobotException {
        Properties automailProperties = new Properties();
		// Default properties
    	automailProperties.setProperty("Robots", "Standard");
    	automailProperties.setProperty("MailPool", "strategies.SimpleMailPool");
    	automailProperties.setProperty("Floors", "10");
    	automailProperties.setProperty("Fragile", "false");
    	automailProperties.setProperty("Mail_to_Create", "80");
    	automailProperties.setProperty("Last_Delivery_Time", "100");

    	String filename = "./automail.properties";

    	/* The first argument is treated as place holder and second argument as the real argument */
    	if (args.length == 2) {
    		filename = args[1];
    		/* Reset the args to an empty arg */
    		args = new String[]{};
		}

    	// Read properties
		FileReader inStream = null;
		try {
			inStream = new FileReader(filename);
			automailProperties.load(inStream);
		} finally {
			 if (inStream != null) {
                inStream.close();
            }
		}

		//Seed
		String seedProp = automailProperties.getProperty("Seed");
		// Floors
		Building.FLOORS = Integer.parseInt(automailProperties.getProperty("Floors"));
        System.out.printf("Floors: %5d%n", Building.FLOORS);
        // Fragile
        boolean fragile = Boolean.parseBoolean(automailProperties.getProperty("Fragile"));
        System.out.printf("Fragile: %5b%n", fragile);
		// Mail_to_Create
		MAIL_TO_CREATE = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        System.out.printf("Mail_to_Create: %5d%n", MAIL_TO_CREATE);
        // Mail_to_Create
     	MAIL_MAX_WEIGHT = Integer.parseInt(automailProperties.getProperty("Mail_Max_Weight"));
        System.out.printf("Mail_Max_Weight: %5d%n", MAIL_MAX_WEIGHT);
		// Last_Delivery_Time
		Clock.LAST_DELIVERY_TIME = Integer.parseInt(automailProperties.getProperty("Last_Delivery_Time"));
        System.out.printf("Last_Delivery_Time: %5d%n", Clock.LAST_DELIVERY_TIME);
		// Robots
		int robots = Integer.parseInt(automailProperties.getProperty("Robots"));
		System.out.print("Robots: "); System.out.println(robots);
		assert(robots > 0);

		// End properties
		
        MAIL_DELIVERED = new ArrayList<>();
                
        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();
        
        /** Read the first argument and save it as a seed if it exists */
        if (args.length == 0 ) { // No arg
        	if (seedProp == null) { // and no property
        		seedMap.put(false, 0); // so randomise
        	} else { // Use property seed
        		seedMap.put(true, Integer.parseInt(seedProp));
        	}
        } else { // Use arg seed - overrides property
        	seedMap.put(true, Integer.parseInt(args[0]));
        }
        Integer seed = seedMap.get(true);
        System.out.printf("Seed: %s%n", seed == null ? "null" : seed.toString());

        /* check are we have enough robots in system to deliver all mail items */
        int ROBOT_CARRY_MAX_WEIGHT;
		switch (robots) {
			case 1:
				ROBOT_CARRY_MAX_WEIGHT = ITeamState.SINGLE_MAX_WEIGHT;
				break;
			case 2:
				ROBOT_CARRY_MAX_WEIGHT = ITeamState.DOUBLE_MAX_WEIGHT;
				break;
			default:
				ROBOT_CARRY_MAX_WEIGHT = ITeamState.TRIPLE_MAX_WEIGHT;
		}
		if (ROBOT_CARRY_MAX_WEIGHT < MAIL_MAX_WEIGHT) {
			throw new NotEnoughRobotException();
		}

        /* initialize whole system */
        MailPool mailPool = new MailPool(new SelectMailItemToDeliverPlan(), new SelectRobotToDeliverPlan());
        Automail automail = new Automail(mailPool, new ReportDelivery(), robots);

        MailGenerator mailGenerator = new MailGenerator(MAIL_TO_CREATE, MAIL_MAX_WEIGHT, automail.getMailPool(), seedMap);
        
        /** Initiate all the mail */
        mailGenerator.generateAllMail();
        // PriorityMailItem priority;  // Not used in this version
        while (MAIL_DELIVERED.size() != mailGenerator.MAIL_TO_CREATE) {
            mailGenerator.step();
            try {
                /* step all components of the system */
                automail.step();
			} catch (InvalidDispatchException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete.");
				System.exit(0);
			}
            Clock.Tick();
        }
        printResults();
    }
    
    static class ReportDelivery implements IMailDelivery {
    	
    	/** Confirm the delivery and calculate the total score */
    	public void deliver(MailItem deliveryItem){
    		if(!MAIL_DELIVERED.contains(deliveryItem)){
    			MAIL_DELIVERED.add(deliveryItem);
                System.out.printf("T: %3d > Delivered(%4d) [%s]%n", Clock.Time(), MAIL_DELIVERED.size(), deliveryItem.toString());
    			// Calculate delivery score
    			total_score += calculateDeliveryScore(deliveryItem);
    		}
    		else{
    			try {
    				throw new MailAlreadyDeliveredException();
    			} catch (MailAlreadyDeliveredException e) {
                    System.err.println(deliveryItem.toString());
    				e.printStackTrace();
    				System.exit(1);
    			}
    		}
    	}

    }
    
    private static double calculateDeliveryScore(MailItem deliveryItem) {
    	// Penalty for longer delivery times
    	final double penalty = 1.2;
    	double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
    	if(deliveryItem instanceof PriorityMailItem){
    		priority_weight = ((PriorityMailItem) deliveryItem).getPriorityLevel();
    	}
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    public static void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Final Score: %.2f%n", total_score);
    }
}
