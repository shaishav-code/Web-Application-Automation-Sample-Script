package com.demo.utlility;

public class TestUtil {
	
	public static long PAGE_LOAD_TIMEOUT = 230;
	public static long IMPLICIT_WAIT = 230;	
	public static long SET_SCRIPT_TIMEOUT= 230;
	
	
	/**
	 * Pauses for given seconds.
	 * 
	 * @param secs
	 */
	public static void pause(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException interruptedException) {

		}
	}

}
