package io.github.cheesyfriedbacon.gears.packet;

import io.github.cheesyfriedbacon.gears.server.RakNetConstants;

public class ConnectedPongPacket extends Packet {
	
	private long pingID;
	private long serverID;
	private String serverIdentifier;

	public ConnectedPongPacket(long pingid, long serverid, String identifier) {
		super(identifier.length() + 35 + 1);
		
		this.pingID = pingid;
		this.serverID = serverid;
		this.serverIdentifier = identifier;
	}

	@Override
	public void decode() {
	}

	@Override
	public void encode() {
		this.getByteBuffer().put(this.getPacketID());
		this.getByteBuffer().putLong(this.pingID);
		this.getByteBuffer().putLong(this.serverID);
		this.getByteBuffer().put(RakNetConstants.RAKNET_MAGIC);
		this.getByteBuffer().putShort((short) this.serverIdentifier.length());
		this.getByteBuffer().put(this.serverIdentifier.getBytes());
	}

	@Override
	public byte getPacketID() {
		return (byte) 0x1C;
	}

}
