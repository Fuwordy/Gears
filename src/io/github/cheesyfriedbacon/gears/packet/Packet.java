package io.github.cheesyfriedbacon.gears.packet;

import java.nio.ByteBuffer;

/**
 * A packet that contains data.
 * It can be decoded and encoded.
 * 
 * @author Cheesy
 */
public abstract class Packet {
	private ByteBuffer byteBuffer;
	
	/**
	 * Constructs a new Packet from an array of bytes.
	 * @param bytes
	 */
	public Packet(byte[] bytes) {
		this.byteBuffer = ByteBuffer.wrap(bytes);
	}
	
	/**
	 * Constructs a new Packet from a ByteBuffer.
	 * @param byteBuffer
	 */
	public Packet(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}
	
	/**
	 * Constructs a new Packet from a size.
	 * @param size
	 */
	public Packet(int size) {
		this.byteBuffer = ByteBuffer.allocate(size);
	}

	/**
	 * Decodes the packet.
	 * All changes are saved into this packet's buffer.
	 */
	public abstract void decode();
	
	/**
	 * Encodes the packet.
	 */
	public abstract void encode();
	
	/**
	 * @return This packet's ByteBuffer.
	 */
	public ByteBuffer getByteBuffer() {
		return this.byteBuffer;
	}
	
	/**
	 * @return This packet's ID.
	 */
	public abstract byte getPacketID();
}
