package automail;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:19:52
 * description: This class represent priority mail item which inheritances from MailItem Class.
 **/

public class PriorityMailItem extends MailItem{
	
	/** The priority of the mail item from 1 low to 100 high */
    private final int PRIORITY_LEVEL;

    /**
     * Constructor for PriorityMailItem, MailItem that should be delivered earlier than others.
     * @param dest_floor Which floor is this item delivering to?
     * @param arrival_time When does this item arrive at MailPool?
     * @param weight What is the weight of this MailItem?
     * @param priority_level How prioritised is this item?
     */
	public PriorityMailItem(int dest_floor, int arrival_time, int weight, int priority_level) {
		super(dest_floor, arrival_time, weight);
        this.PRIORITY_LEVEL = priority_level;
	}
	
    /**
    *
    * @return the priority level of a mail item
    */
   public int getPriorityLevel(){
       return PRIORITY_LEVEL;
   }

    /**
     *
     * @return the toString format for PriorityMailItem object.
     */
   @Override
   public String toString(){
       return super.toString() + String.format(" | Priority: %3d", PRIORITY_LEVEL);
   }

}
