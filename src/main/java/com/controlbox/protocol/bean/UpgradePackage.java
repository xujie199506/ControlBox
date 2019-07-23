package com.controlbox.protocol.bean;

import java.util.List;

public class UpgradePackage {
	private byte[] bootInfo;
	private List<byte[]> upgradePackageList;

	public UpgradePackage() {

	}

	public byte[] getBootInfo() {
		return bootInfo;
	}

	public void setBootInfo(byte[] bootInfo) {
		this.bootInfo = bootInfo;
	}

	public List<byte[]> getUpgradePackageList() {
		return upgradePackageList;
	}

	public void setUpgradePackageList(List<byte[]> upgradePackageList) {
		this.upgradePackageList = upgradePackageList;
	}
}
