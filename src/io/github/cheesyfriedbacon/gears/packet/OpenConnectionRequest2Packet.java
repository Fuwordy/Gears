package io.github.cheesyfriedbacon.gears.packet;

import io.github.cheesyfriedbacon.gears.server.RakNetConstants;

public class OpenConnectionRequest2Packet extends Packet {

	public byte[] getSecurityCookie() {
		return securityCookie;
	}

	public short getServerPort() {
		return serverPort;
	}

	public short getMTUSize() {
		return MTUSize;
	}

	public long getClientID() {
		return clientID;
	}

	private byte[] securityCookie;
	private short serverPort;
	private short MTUSize;
	private long clientID;

	public OpenConnectionRequest2Packet(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void decode() {
		byte id = this.getByteBuffer().get();
		if (!(id == this.getPacketID())) return;
		byte[] magic = RakNetConstants.RAKNET_MAGIC;
		this.getByteBuffer().get(magic, 0, RakNetConstants.RAKNET_MAGIC_LENGTH);
		
		this.securityCookie = new byte[] {
			0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00
		};
		
		this.getByteBuffer().get(this.securityCookie, 0, 5);
		
		this.serverPort = (short) 0;
		this.getByteBuffer().getShort(this.serverPort);
		
		this.MTUSize = (short) 0;
		this.getByteBuffer().getShort(this.MTUSize);
		
		this.clientID = (long) 0;
		this.clientID = this.getByteBuffer().getLong();
	}

	@Override
	public void encode() {
		// Do nothing, this is CTS.
	}

	@Override
	public byte getPacketID() {
		return (byte) 0x07;
	}

}
