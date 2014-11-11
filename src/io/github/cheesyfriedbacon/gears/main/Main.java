package io.github.cheesyfriedbacon.gears.main;

import java.io.IOException;
import java.net.SocketException;

import io.github.cheesyfriedbacon.gears.server.Server;

public class Main {
	public static void main(String[] args) {
		System.out.println("Gears MCPE Server [Alpha v0.0.1]");
		System.out.println("Gears: Starting server...");
		
		try {
			(new Server(19132)).start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
