package com.controlbox.controller;

import com.controlbox.nio.TextTools;
import com.controlbox.protocol.ProtocelKit;

public class DemoTest {
	
	public static void main(String[] args) {
		ProtocelKit protocelKit = new ProtocelKit();
	/*	byte[] cmd = protocelKit.getCmd((byte)0x00,new byte[]{(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x04,(byte)0x04,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
				(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,});*/
		//byte[] cmd = protocelKit.getCmd((byte)0x06,new byte[]{(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x04,(byte)0x01});
		byte[] cmd = protocelKit.getCmd((byte)0xF5,new byte[]{(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x04,(byte)0x01});
 		
		System.out.println(TextTools.byteToHexString(cmd));
	}

}
