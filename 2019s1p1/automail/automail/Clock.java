package automail;

/**
 * Team Number: WS12-3
 * Group member: XuLin Yang(904904), Zhuoqun Huang(908525), Renjie Meng(877396)
 *
 * @create 2019-5-3 15:09:12
 * description: Clock class is used to represent the time
 **/

public class Clock {
	/**
     * Represents the current time
     * */
    private static int Time = 0;
    
    /**
     * The threshold for the latest time for mail to arrive
     * */
    public static int LAST_DELIVERY_TIME;

    /**
     * Get the time of clock
     */
    public static int Time() {return Time;}

    /**
     * Update the time of clock by 1.
     */
    public static void Tick() {Time++;}
}
