package main;

import java.util.Timer;
import java.util.TimerTask;

//"calendarsystemfsktm@gmail.com","calendarsystem123++"

public class Test {

    public static void main(String[] args) {
    	int delay = 1000; // delay for 5 sec.
	    int period = 1000; // repeat every sec.
	    Timer timer = new Timer();

	    timer.scheduleAtFixedRate(new TimerTask() {
	      public void run() {
	        System.out.println("1");
	      }
	    }, delay, period);
	    
	    System.out.println("hellowoel");
    }
}