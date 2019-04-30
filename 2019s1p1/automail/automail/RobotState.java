package automail;

import exceptions.InvalidDispatchException;

/**
 * Defines the states IRobot can be at, and actions they should take.
 */
public enum RobotState implements IRobotState {
    /**
     * IRobot still has uncompleted task
     */
    DELIVERING {
        @Override
        public void step(IRobot iRobot) {
            MailItem mailItem = iRobot.getCurrentMailItem();
            int destination = mailItem.getDestinationFloor();

            // Deliver item and (load next item / return to base)
            if (iRobot.atFloor(destination)) {
                iRobot.deliver();

                if (iRobot.hasNextMailItem()) {
                    iRobot.loadNextMailItem();
                    iRobot.changeState(RobotState.DELIVERING);
                } else {
                    iRobot.changeState(RobotState.RETURNING);
                }
            } else {
                iRobot.moveTowards(destination);
            }
        }
    },
    /**
     * Returning to base
     */
    RETURNING {
        @Override
        public void step(IRobot iRobot) {
            if (iRobot.atFloor(Building.MAILROOM_LOCATION)) {
                iRobot.registerWaiting();
                iRobot.changeState(RobotState.WAITING);
            } else {
                iRobot.moveTowards(Building.MAILROOM_LOCATION);
            }
        }
    },
    /**
     * Waiting for order
     */
    WAITING {
        @Override
        public void step(IRobot iRobot) {
            if (iRobot.canDispatch()) {
                try {
                    iRobot.dispatch();
                } catch (InvalidDispatchException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * By default robot do nothing when stepping (Waiting)
     * @param iRobot The robot to act on
     */
    @Override
    public void step(IRobot iRobot) {}
}
//
//    	switch(current_state) {
//                /** This state is triggered when the robot is returning to the mailroom after a delivery */
//                case RETURNING:
//                /** If its current position is at the mailroom, then the robot should change state */
//                if(current_floor == Building.MAILROOM_LOCATION) {
//                if (tube != null) {
//                mailPool.addToPool(tube);
//                System.out.printf("T: %3d > old addToPool [%s]%n", Clock.Time(), tube.toString());
//                tube = null;
//                }
//                /** Tell the sorter the robot is ready */
//                mailPool.registerWaiting(this);
//                changeState(RobotState.WAITING);
//                } else {
//                /** If the robot is not at the mailroom floor yet, then move towards it! */
//                moveTowards(Building.MAILROOM_LOCATION);
//                }
//                break;
//                case WAITING:
//                /** If the StorageTube is ready and the Robot is in the mailroom then start the delivery */
//                if(!isEmpty() && receivedDispatch){
//                receivedDispatch = false;
//                deliveryCounter = 0; // reset delivery counter
////        			setRoute();
//                changeState(RobotState.DELIVERING);
//                }
//                break;
//                case DELIVERING:
//                if(current_floor == deliveryItem.getDestinationFloor()){ // If already here drop off either way
//                /** Delivery complete, report this to the simulator! */
//                /* TODO leading robot report the delivery */
//                delivery.deliver(deliveryItem);
//                deliveryItem = null;
//                deliveryCounter++;
//                if(deliveryCounter > 2){  // Implies a simulation bug
//                throw new ExcessiveDeliveryException();
//                }
//                /** Check if want to return, i.e. if there is no item in the tube*/
//                if(tube == null) {
//                changeState(RobotState.RETURNING);
//
//                /** If there is another item, set the robot's route to the location to deliver the item */
//                } else {
//                deliveryItem = tube;
//                tube = null;
//                changeState(RobotState.DELIVERING);
//                }
//                } else {
//                /** The robot is not at the destination yet, move towards it! */
//                moveTowards(deliveryItem.getDestinationFloor());
//                }
//                break;
//
////    			if(current_floor == this.currentTask.getDestinationFloor()){ // If already here drop off either way
////                    /** Delivery complete, report this to the simulator! */
////                    delivery.deliver(deliveryItem);
////                    deliveryItem = null;
////                    deliveryCounter++;
////                    if(deliveryCounter > 2){  // Implies a simulation bug
////                    	throw new ExcessiveDeliveryException();
////                    }
////                    /** Check if want to return, i.e. if there is no item in the tube*/
////                    if(tube == null){
////                    	changeState(RobotState.RETURNING);
////                    /** If there is another item, set the robot's route to the location to deliver the item */
////                    } else{
////                        deliveryItem = tube;
////                        tube = null;
//////                        setRoute();
////                        changeState(RobotState.DELIVERING);
////                    }
////    			} else {
////	        		/** The robot is not at the destination yet, move towards it! */
////	                moveTowards(destination_floor);
////    			}
////                break;
//                }
