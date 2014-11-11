package io.github.cheesyfriedbacon.gears.packet;

import io.github.cheesyfriedbacon.gears.server.GearsServer;
import io.github.cheesyfriedbacon.gears.server.RakNetConstants;

public class OpenConnectionReply2Packet extends Packet {

	private short clientUDPPort;
	private long serverID;
	private short MTUSize;

	public OpenConnectionReply2Packet(long serverID, short clientUDPPort, short MTUSize) {
		super(30);
		
		this.serverID = serverID;
		this.clientUDPPort = clientUDPPort;
		this.MTUSize = MTUSize;
	}

	@Override
	public void decode() {
		// Do nothing, this is STC
	}

	@Override
	public void encode() {
		this.getByteBuffer().put(this.getPacketID());
		this.getByteBuffer().put(RakNetConstants.RAKNET_MAGIC);
		this.getByteBuffer().putLong(this.serverID);
		this.getByteBuffer().putShort(this.clientUDPPort);
		this.getByteBuffer().putShort(this.MTUSize);
		this.getByteBuffer().put((byte) 0);
	}

	@Override
	public byte getPacketID() {
		return (byte) 0x08;
	}
}
