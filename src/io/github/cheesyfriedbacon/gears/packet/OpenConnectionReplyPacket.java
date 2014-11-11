package io.github.cheesyfriedbacon.gears.packet;

import io.github.cheesyfriedbacon.gears.server.RakNetConstants;

public class OpenConnectionReplyPacket extends Packet {

	private long serverID;
	private short packetSize;

	public OpenConnectionReplyPacket(long serverID, short packetSize) {
		super(28 + 1);
		
		this.serverID = serverID;
		this.packetSize = packetSize;
	}

	@Override
	public void decode() {
		// Do nothing, this is STC.
	}

	@Override
	public void encode() {
		this.getByteBuffer().put(this.getPacketID());
		this.getByteBuffer().put(RakNetConstants.RAKNET_MAGIC);
		this.getByteBuffer().putLong(this.serverID);
		this.getByteBuffer().put((byte) 0x00);
		this.getByteBuffer().putShort(this.packetSize);
	}

	@Override
	public byte getPacketID() {
		return (byte) 0x06;
	}

}
