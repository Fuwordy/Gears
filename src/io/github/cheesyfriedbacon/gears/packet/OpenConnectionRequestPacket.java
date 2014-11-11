package io.github.cheesyfriedbacon.gears.packet;

import io.github.cheesyfriedbacon.gears.server.RakNetConstants;

public class OpenConnectionRequestPacket extends Packet {
	private int protocolVersion = -1;
	
	public OpenConnectionRequestPacket(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void decode() {
		byte[] id = new byte[] {
				0x00
		};
		
		this.getByteBuffer().get(id, 0, 1);
		
		if (!(id[0] == this.getPacketID())) return;
		
		byte[] magic = RakNetConstants.RAKNET_MAGIC; // This will be overwritten by below code.
		this.getByteBuffer().get(magic, 0, 16);
		
		byte[] protocol = {
				0x00
		};
		this.getByteBuffer().get(protocol, 0, 1);
		
		this.protocolVersion = (int) protocol[0];
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	@Override
	public void encode() {
		// Do nothing, this is CTS.
	}

	@Override
	public byte getPacketID() {
		return 0x05;
	}
}
