package io.github.cheesyfriedbacon.gears.server;

public class RakNetConstants {
	// 0x00ffff00fefefefefdfdfdfd12345678
	
	/**
	 * Raknet's magic.
	 */
	public static final byte[] RAKNET_MAGIC = {
		(byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, 
		(byte) 0xfe, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0x12, (byte) 0x34, 
		(byte) 0x56, (byte) 0x78
	};
	
	public static final String RAKNET_MAGIC_STRING = "0x00ffff00fefefefefdfdfdfd12345678";
}
