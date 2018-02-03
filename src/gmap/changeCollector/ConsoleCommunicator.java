/*
 * Author: Rayhanur Rahman
 * Updated by: Saif Uddin Mahmud
 * Last updated: January 30, 2018
 * 
 * */


package gmap.changeCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleCommunicator {

	private String command;
	
//	public ConsoleCommunicator(String command) {
//		this.command = command;
//	}
	
	public String executeCommand(String command) {

			
		StringBuilder sb = new StringBuilder();

		try {
			Process process = Runtime.getRuntime().exec(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				sb.append(line + "\n");
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
