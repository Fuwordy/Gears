package io.github.cheesyfriedbacon.gears.server;

import io.github.cheesyfriedbacon.gears.packet.ConnectedPongPacket;
import io.github.cheesyfriedbacon.gears.packet.OpenConnectionReply2Packet;
import io.github.cheesyfriedbacon.gears.packet.OpenConnectionReplyPacket;
import io.github.cheesyfriedbacon.gears.packet.OpenConnectionRequest2Packet;
import io.github.cheesyfriedbacon.gears.packet.OpenConnectionRequestPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * A Gears Server.
 * @author Cheesy
 */
public class GearsServer {
	/**
	 * The default packet size.
	 */
	public static final int PACKET_SIZE = 4098;

	/**
	 * The port that the server runs on.
	 */
	private int port = 19132;
	
	/**
	 * The unique ID for this server.
	 */
	private byte[] serverID = new byte[] {
		0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00
	};
	
	/**
	 * Is the server?
	 */
	private boolean running = false;
	
	/**
	 * The current DatagramSocket.
	 */
	private DatagramSocket datagramSocket = null;
	
	/**
	 * List of client IDs.
	 */
	private ArrayList<Long> clientIDs = new ArrayList<Long>();

	/**
	 * Is this server quiet?
	 * If it is, it produces no STDOUT input.
	 */
	private boolean quiet;

	/**
	 * The random object for this server.
	 */
	private Random random;
	
	/**
	 * Constructs a new server.
	 * @param port
	 * @throws SocketException 
	 */
	public GearsServer(int port) throws SocketException {
		this.port = port;
		this.quiet = true;
		this.datagramSocket = new DatagramSocket(this.port);
		this.generateNewServerID();
	}
	
	/**
	 * Constructs a new server.
	 * @param port
	 * @param quiet
	 * @throws SocketException
	 */
	public GearsServer(int port, boolean quiet) throws SocketException {
		this.port = port;
		this.quiet = quiet;
		this.datagramSocket = new DatagramSocket(this.port);
		this.generateNewServerID();
	}
	
	/**
	 * Generates a new server ID for this server.
	 */
	public void generateNewServerID() {
		if (this.random == null) { // Looks like we have no random, we
									// will make another one.
			this.random = new Random();
			this.random.setSeed((long) 0xDEADBEEF); // Hehe
		}
		
		new Random().nextBytes(this.serverID);
	}
	
	/**
	 * Converts a byte[] to a hex string.
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unused")
	private String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	        sb.append(String.format("%02X ", b));
	    }
	    return sb.toString();
	}
	
	/**
	 * Converts a byte to a hex string.
	 * @param byte_
	 * @return
	 */
	@SuppressWarnings("unused")
	private String byteToHexString(byte byte_) {
		return String.format("%02X", byte_);
	}
	
	/**
	 * Stops the server.
	 */
	public void stop() {
		this.running = false;
	}
	
	/**
	 * Are these bytes the RakNet magic?
	 * @param bytes
	 * @return
	 */
	public boolean isMagic(byte[] bytes) {
		byte[] magic = RakNetConstants.RAKNET_MAGIC;
		return Arrays.equals(bytes, magic);
	}
	
	/**
	 * Send a packet.
	 * @param data
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	private void sendPacket(byte[] data, InetAddress address, int port) throws IOException {
		DatagramPacket pkt = new DatagramPacket(data, data.length, address, port);
		this.datagramSocket.send(pkt);
	}
	
	/**
	 * Starts the server.
	 * @throws IOException 
	 */
	public void start() throws IOException {
		/* ===========================================================
		 * ===========================================================
		 * 
		 * Bytes Reference:
		 * Length of byte: 1 byte
		 * Length of short: 2 bytes
		 * Length of int32: 4 bytes
		 * Length of int64: 8 bytes
		 * Length of MAGIC: 16 bytes, always 0x00ffff00fefefefefdfdfdfd12345678.
		 * 
		 * String breakdown:
		 * 		2 bytes: short, indicates how long it is!
		 * 		- bytes: The actual string.
		 * 
		 * ===========================================================
		 * ===========================================================
		 */
		// Specify that this server is running.
		this.running = true;
		
		// Make a new array for received data.
		byte[] receivedData = new byte[GearsServer.PACKET_SIZE];
		
		if (!quiet) System.out.println("Gears: Server: This server's unique ID is: " + this.getServerID());
		
		// Put an alert.
		if (!quiet) System.out.println("Gears: Server: Started.");
		
		while(this.isRunning()) { // While the server is running...
			// A packet to store the received data.
			DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);
			
			// Receive the data and put in the above packet.
			this.datagramSocket.receive(packet);
			
			// Get the bytes from the packet.
			byte[] data = packet.getData();
			
			// Get the address from which the packet originated from.
			InetAddress fromAddress = packet.getAddress();
			int fromPort = packet.getPort();
			
			// TODO: Make packet handler.
			if (data[0] == 0x01) {
				// Make a connected pong packet.
				ConnectedPongPacket cp_pkt = new ConnectedPongPacket(System.currentTimeMillis() /* ping */, 1 /* server id */, "MCCPP;MINECON;Gears Server");
				
				// Encode the packet.
				cp_pkt.encode();
				byte[] finalbytes = cp_pkt.getByteBuffer().array();

				// Send the final packets.
				this.sendPacket(finalbytes, fromAddress, fromPort);
				
				if (!quiet) System.out.println("Gears: Server: Sent pong!");
			} else if (data[0] == 0x05) {
				OpenConnectionRequestPacket ocr_packet = new OpenConnectionRequestPacket(data);
				ocr_packet.decode();
				int protocolver = ocr_packet.getProtocolVersion();
				if (!quiet) System.out.println("Gears: Server: Client attempting connect with protocol " + protocolver + ".");
				
				// We will use this to convert the server ID to a long.
				ByteBuffer bb = ByteBuffer.wrap(this.getServerID());
				
				OpenConnectionReplyPacket ocr2_packet = new OpenConnectionReplyPacket(
						bb.getLong(), 
						(short) GearsServer.PACKET_SIZE);
				ocr2_packet.encode();
				this.sendPacket(ocr2_packet.getByteBuffer().array(), fromAddress, fromPort);
				if (!quiet) System.out.println("Gears: Server: Sent OpenConnectionReply.");
			} else if (data[0] == 0x07) {
				if (!quiet) System.out.println("Gears: Server: Recieved client connect stage 2!");
				OpenConnectionRequest2Packet ocr_packet = new OpenConnectionRequest2Packet(data);
				ocr_packet.decode();
				System.out.println("!!! Size: " + ocr_packet.getMTUSize());
				
				// We will use this to convert the server ID to a long.
				ByteBuffer bb = ByteBuffer.wrap(this.getServerID());
				
				OpenConnectionReply2Packet aocr2_packet = new OpenConnectionReply2Packet(
						bb.getLong(), (short) fromPort, (short) 0);
				aocr2_packet.encode();
				
				this.sendPacket(aocr2_packet.getByteBuffer().array(), fromAddress, fromPort);
				if (!quiet) System.out.println("Gears: Server: Sent response stage 2!");
			} else {
				System.out.println("Gears: Server: !!!! Received unknown packet ID " + this.byteToHexString(data[0]));
			}
		}
	}

	public byte[] getServerID() {
		return serverID;
	}

	public boolean isQuiet() {
		return quiet;
	}

	/**
	 * Returns the port the server is running on.
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns whether the server is running.
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}
}
