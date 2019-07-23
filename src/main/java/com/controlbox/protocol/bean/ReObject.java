package com.controlbox.protocol.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 超级返回对象，所有来自第三方平台和模块的信息的解析
public class ReObject {
	public static class expCodeType {
		public final static int DATA_FAIL = -999;
		public final static int DATA_IS_NULL = -1;
		public final static int DATA_HEAD_FAIL = -2;
		public final static int DATA_NO_COMPLETE = -3;
		public final static int DATA_CRC_FAIL = -4;
		public final static int DATA_BODYLEN_FAIL = -5;
		public final static int DATA_CMD00_FAIL = -6;
		public final static int DATA_HARTBEAT_FAIL = -7;
	}

	public static class expMsgType {
		public final static String DATA_FAIL = "未知异常";
		public final static String DATA_IS_NULL = "数据为空";
		public final static String DATA_HEAD_FAIL = "数据包头部错误";
		public final static String DATA_NO_COMPLETE = "数据不完整";
		public final static String DATA_CRC_FAIL = "CRC校验没有通过";
		public final static String DATA_BODYLEN_FAIL = "包体长度不对";
		public final static String DATA_CMD00_FAIL = "模块登录连接数据不完整";
		public final static String DATA_HARTBEAT_FAIL = "服务收到的心跳字节码不对";
	}

	// 是否校验通过
	private boolean isCheckPass = false;
	// 异常代码
	private int expCode = 0;
	// 异常信息
	private String expMsg = "";
	// 包头
	private byte head;
	// 包总长度，单位字节
	private int totalLength;
	// 包的校验字节
	private byte[] crc;
	// 数据体长度 单位字节
	private int bodyLength;
	// 命令类型
	private byte cmdType;
	// 设备号
	private byte[] devNo;
	// 版本号
	private byte[] version;
	// IP
	private byte[] ip;
	// 电话号码
	private byte[] phone;
	// 心跳
	private byte heartbeat;
	// key
	private byte[] key;
	// 卡总数
	private int codeCount;
	// 卡号列表
	private List<byte[]> codeList = new ArrayList<byte[]>();
	// 返回状态
	private byte reState;
	// 参数类型
	private byte parmType;
	// 模块的状态字节
	private byte stateByte;
	
	//状态类型
	private byte stateType;

	//状态值
	private byte stateValue;
	
	//状态值
	 private byte[] typeValue;
		//状态值
	 private byte  uuidNum;
	 private List<byte[]> uuidList = new ArrayList<byte[]>();

	public ReObject() {

	}

	
	
	
	 



	public byte[] getTypeValue() {
		return typeValue;
	}








	public void setTypeValue(byte[] typeValue) {
		this.typeValue = typeValue;
	}







  




	public byte getUuidNum() {
		return uuidNum;
	}








	public void setUuidNum(byte uuidNum) {
		this.uuidNum = uuidNum;
	}








	public List<byte[]> getUuidList() {
		return uuidList;
	}




	public void setUuidList(List<byte[]> uuidList) {
		this.uuidList = uuidList;
	}




	public byte getStateValue() {
		return stateValue;
	}




	public void setStateValue(byte stateValue) {
		this.stateValue = stateValue;
	}




	public byte getStateType() {
		return stateType;
	}




	public void setStateType(byte stateType) {
		this.stateType = stateType;
	}




	public boolean isCheckPass() {
		return isCheckPass;
	}

	public void setCheckPass(boolean isCheckPass) {
		this.isCheckPass = isCheckPass;
	}

	public int getExpCode() {
		return expCode;
	}

	public void setExpCode(int expCode) {
		this.expCode = expCode;
	}

	public String getExpMsg() {
		return expMsg;
	}

	public void setExpMsg(String expMsg) {
		this.expMsg = expMsg;
	}

	public byte getHead() {
		return head;
	}

	public void setHead(byte head) {
		this.head = head;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	public byte[] getCrc() {
		return crc;
	}

	public void setCrc(byte[] crc) {
		this.crc = crc;
	}

	public int getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}

	public byte getCmdType() {
		return cmdType;
	}

	public void setCmdType(byte cmdType) {
		this.cmdType = cmdType;
	}

	public byte[] getDevNo() {
		return devNo;
	}

	public void setDevNo(byte[] devNo) {
		this.devNo = devNo;
	}

	public byte[] getVersion() {
		return version;
	}

	public void setVersion(byte[] version) {
		this.version = version;
	}

	public byte[] getIp() {
		return ip;
	}

	public void setIp(byte[] ip) {
		this.ip = ip;
	}

	public byte[] getPhone() {
		return phone;
	}

	public void setPhone(byte[] phone) {
		this.phone = phone;
	}

	public byte getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(byte heartbeat) {
		this.heartbeat = heartbeat;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	public List<byte[]> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<byte[]> codeList) {
		this.codeList = codeList;
	}

	public byte getReState() {
		return reState;
	}

	public void setReState(byte reState) {
		this.reState = reState;
	}

	public byte getParmType() {
		return parmType;
	}

	public void setParmType(byte parmType) {
		this.parmType = parmType;
	}

	public byte getStateByte() {
		return stateByte;
	}

	public void setStateByte(byte stateByte) {
		this.stateByte = stateByte;
	}








	@Override
	public String toString() {
		return "ReObject [isCheckPass=" + isCheckPass + ", expCode=" + expCode + ", expMsg=" + expMsg + ", head=" + head
				+ ", totalLength=" + totalLength + ", crc=" + Arrays.toString(crc) + ", bodyLength=" + bodyLength
				+ ", cmdType=" + cmdType + ", devNo=" + Arrays.toString(devNo) + ", version=" + Arrays.toString(version)
				+ ", ip=" + Arrays.toString(ip) + ", phone=" + Arrays.toString(phone) + ", heartbeat=" + heartbeat
				+ ", key=" + Arrays.toString(key) + ", codeCount=" + codeCount + ", codeList=" + codeList + ", reState="
				+ reState + ", parmType=" + parmType + ", stateByte=" + stateByte + ", stateType=" + stateType
				+ ", stateValue=" + stateValue + ", typeValue=" + Arrays.toString(typeValue) + ", uuidNum=" + uuidNum
				+ ", uuidList=" + uuidList + "]";
	}
	
		
}
