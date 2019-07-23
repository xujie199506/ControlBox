package com.controlbox.controller;

import java.util.List;

import com.controlbox.protocol.bean.UpgradePackage;

public class Content {
	public static byte[] key= new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
	public static String path="D:\\updata\\";
	public static byte[] devHartbeatCode= new byte[]{(byte)0x07,(byte)0x00,(byte)0xD1,(byte)0x21};
	
	public static UpgradePackage upgradePackage = null;
	
}
