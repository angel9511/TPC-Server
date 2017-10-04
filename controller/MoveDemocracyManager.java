package controller;

import java.time.LocalDateTime;

public class MoveDemocracyManager implements Runnable {

	public MoveDemocracyManager(){
		
	}
	
	@Override
	public void run() {
        LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt.getMinute() + " now " + ldt.getSecond());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ldt = LocalDateTime.now();
		System.out.println(ldt.getMinute()+" In there "+ldt.getSecond());
		MainController.getInstance().pickMove();
		MainController.getInstance().setDemocracy(false);
		System.out.println("Democracy is dead");
	}

}
