package com.controlbox.protocol;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.controlbox.nio.NettyServerHandler;
import com.controlbox.nio.TextTools;
import com.controlbox.protocol.bean.ReObject;
import com.controlbox.protocol.bean.UpgradePackage;
 

public class ProtocelKit {
	private byte HEAD = (byte) 0xAA;

	public byte[] getCmd(byte cmdType, byte[] body) {
		byte[] re = null;

		// 计算数据包总长度
		// 头部1个字节 命令类型1个字节 数据体长度2个字节 校验位两个字节 数据体字节数可变
		int len = 1 + 1 + 2 + 2;
		int bodyLen = 0;
		int next = 0;
		if (body != null && body.length > 0) {
			bodyLen = body.length;
			len += bodyLen;
		}

		// 生成byte数组
		re = new byte[len];

		// 头部
		re[next++] = HEAD;

		// 命令类型
		re[next++] = cmdType;

		// 数据体长度
		byte[] bodyLenBytes = TextTools.intToByte2(bodyLen);
		System.arraycopy(bodyLenBytes, 0, re, next, bodyLenBytes.length);
		next += bodyLenBytes.length;

		// 数据体
		if (bodyLen > 0) {
			System.arraycopy(body, 0, re, next, bodyLen);
			next += bodyLen;
		}

		// 校验位
		byte[] crcBytes = getCRC16(re);
		System.arraycopy(crcBytes, 0, re, next, crcBytes.length);

		return re;
	}

	private byte[] getCRC16(byte[] data) {
		byte[] re = new byte[2];

		int crc16 = calcCrc16(data, 0, data.length - 2);
		re = TextTools.intToByte2(crc16);

		return re;
	}

	private byte[] crc16_tab_h = { (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
			(byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
			(byte) 0x81, (byte) 0x40 };

	private byte[] crc16_tab_l = { (byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03,
			(byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5,
			(byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF,
			(byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09,
			(byte) 0x08, (byte) 0xC8, (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB,
			(byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D,
			(byte) 0x1C, (byte) 0xDC, (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17,
			(byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12, (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1,
			(byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3,
			(byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35,
			(byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F,
			(byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9,
			(byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B,
			(byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED,
			(byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
			(byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21,
			(byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3,
			(byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65,
			(byte) 0x64, (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F,
			(byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9,
			(byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B,
			(byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD,
			(byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74, (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7,
			(byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71,
			(byte) 0x70, (byte) 0xB0, (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53,
			(byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95,
			(byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F,
			(byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59,
			(byte) 0x58, (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B,
			(byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
			(byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47,
			(byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81,
			(byte) 0x80, (byte) 0x40 };

	/**
	 * 计算CRC16校验
	 * 
	 * @param data
	 *            需要计算的数组
	 * @param offset
	 *            起始位置
	 * @param len
	 *            长度
	 * @return CRC16校验值
	 */

	public int calcCrc16(byte[] data, int offset, int len) {
		return calcCrc16(data, offset, len, 0xffff);
	}

	/**
	 * 计算CRC16校验
	 * 
	 * @param data
	 *            需要计算的数组
	 * @param offset
	 *            起始位置
	 * @param len
	 *            长度
	 * @param preval
	 *            之前的校验值
	 * @return CRC16校验值
	 */
	public int calcCrc16(byte[] data, int offset, int len, int preval) {
		int ucCRCHi = (preval & 0xff00) >> 8;
		int ucCRCLo = preval & 0x00ff;
		int iIndex;
		for (int i = 0; i < len; ++i) {
			iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
			ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
			ucCRCHi = crc16_tab_l[iIndex];
		}
		return ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
	}

	public ProtocelKit() {

	}

	public ReObject getReObject(byte[] info) {
		ReObject re = new ReObject();

		try {
			if (info == null || info.length == 0) {
				re.setCheckPass(false);
				re.setExpCode(ReObject.expCodeType.DATA_IS_NULL);
				re.setExpMsg(ReObject.expMsgType.DATA_IS_NULL);
			} else {
				re.setHead(info[0]);
				if (re.getHead() != (byte) 0xAA) {
					re.setCheckPass(false);
					re.setExpCode(ReObject.expCodeType.DATA_HEAD_FAIL);
					re.setExpMsg(ReObject.expMsgType.DATA_HEAD_FAIL);
				} else {
					re.setTotalLength(info.length);
					if (info.length < 6) {
						re.setCheckPass(false);
						re.setExpCode(ReObject.expCodeType.DATA_NO_COMPLETE);
						re.setExpMsg(ReObject.expMsgType.DATA_NO_COMPLETE);
					} else {
						byte[] crc = new byte[2];
						crc[0] = info[info.length - 2];
						crc[1] = info[info.length - 1];
						re.setCrc(crc);
						
						int checkCrc = calcCrc16(info, 0, info.length - crc.length);
						byte[] checkCrcBytes = TextTools.intToByte2(checkCrc);
						System.out.println(TextTools.byteToHexString(checkCrcBytes));
						if (checkCrcBytes[0] != crc[0] || checkCrcBytes[1] != crc[1]) {
							re.setCheckPass(false);
							re.setExpCode(ReObject.expCodeType.DATA_CRC_FAIL);
							re.setExpMsg(ReObject.expMsgType.DATA_CRC_FAIL);
						} else {
							// 比较数据体长度是否正确
							byte[] bodyLenBytes = new byte[2];
							System.arraycopy(info, 2, bodyLenBytes, 0, bodyLenBytes.length);
							int bodyLen = TextTools.byteToInt2(bodyLenBytes);
							re.setBodyLength(bodyLen);
							if (bodyLen != info.length - 1 - 1 - 2 - 2) {
								re.setCheckPass(false);
								re.setExpCode(ReObject.expCodeType.DATA_BODYLEN_FAIL);
								re.setExpMsg(ReObject.expMsgType.DATA_BODYLEN_FAIL);
							} else {
								// 全部校验通过
								re.setCheckPass(true);

								// 获取命令类型
								byte cmdType = info[1];
								re.setCmdType(cmdType);

								if (bodyLen > 0) {
									// 解析数据体
									byte[] body = new byte[bodyLen];
									System.arraycopy(info, 4, body, 0, body.length);
									if (cmdType == (byte) 0x00) {// 模块登录连接请求
										if (bodyLen != 20) {
											re.setCheckPass(false);
											re.setExpCode(ReObject.expCodeType.DATA_CMD00_FAIL);
											re.setExpMsg(ReObject.expMsgType.DATA_CMD00_FAIL);
										} else {
											int next = 0;
											// 设备号
											byte[] devNo = new byte[4];
											System.arraycopy(body, next, devNo, 0, devNo.length);
											next += devNo.length;
											re.setDevNo(devNo);

											// 版本号
											byte[] version = new byte[2];
											System.arraycopy(body, next, version, 0, version.length);
											next += version.length;
											re.setVersion(version);

											// IP
											byte[] ip = new byte[4];
											System.arraycopy(body, next, ip, 0, ip.length);
											next += ip.length;
											re.setIp(ip);

											// 电话号码
											byte[] phone = new byte[10];
											System.arraycopy(body, next, phone, 0, phone.length);
											next += phone.length;
											re.setPhone(phone);
										}
									} else if (cmdType == (byte) 0x01) {// 模块向服务发的心跳
										byte heartbeat = body[0];
										if (heartbeat != (byte) 0xFF) {
											re.setCheckPass(false);
											re.setExpCode(ReObject.expCodeType.DATA_HARTBEAT_FAIL);
											re.setExpMsg(ReObject.expMsgType.DATA_HARTBEAT_FAIL);
										} else {
											re.setHeartbeat(heartbeat);
										}
									} else if (cmdType == (byte) 0x02) {// 模块向服务上传标签号
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);

										// 卡总数
										byte codeCount = body[4];
										re.setCodeCount(codeCount);

										// 卡号列表
										List<byte[]> codeList = new ArrayList<byte[]>();
										byte[] codeListBytes = new byte[body.length - 5];
										System.arraycopy(body, 5, codeListBytes, 0, codeListBytes.length);
										for (int i = 0; i < codeListBytes.length;) {
											byte[] tmp = new byte[5];
											System.arraycopy(codeListBytes, i, tmp, 0, tmp.length);
											codeList.add(tmp);
											i += 5;
										}
										re.setCodeList(codeList);
									} else if (cmdType == (byte) 0xF3) {// 模块收到设置命令后的返回
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);

										// 状态
										byte state = body[next++];
										re.setReState(state);

										// 参数类型
										byte parmType = body[next++];
										re.setParmType(parmType);
									} else if (cmdType == (byte) 0xF4) {// 模块收到升级引导包命令后的返回
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);

										// 版本号
										byte[] version = new byte[2];
										System.arraycopy(body, next, version, 0, version.length);
										next += version.length;
										re.setVersion(version);
									} else if (cmdType == (byte) 0xF5) {// 模块收到升级包命令后的返回
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);

										// 返回状态
										byte state = body[next++];
										re.setReState(state);
									} else if (cmdType == (byte) 0x02) {// 模块向服务端发状态字节
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);

										// 模块状态字节
										byte stateByte = body[next++];
										re.setStateByte(stateByte);
									} else if (cmdType == (byte) 0xF7) {
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);
										byte stateByte = body[next++];
										re.setStateType(stateByte);
										byte statevalueByte = body[next++];
										re.setStateValue(statevalueByte);
									} else if (cmdType == (byte) 0x08) {
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);
										
										byte[] typeValue = new byte[2];
										System.arraycopy(body, next, typeValue, 0, typeValue.length);
										next += typeValue.length;
										re.setTypeValue(typeValue);
										
										byte uuidnum = body[next++];
										re.setUuidNum(uuidnum);
										int IbuuidNumber=uuidnum;
										
										int alluuidlen=body.length-7;
										int uuidlen=alluuidlen/IbuuidNumber;
										List<byte[]> uuidlist =new ArrayList<byte[]>();
										for(int i=0;i<IbuuidNumber;i++){
											byte[] oneuuid =new byte[uuidlen];
											System.arraycopy(body, next, oneuuid, 0, oneuuid.length);
											next += oneuuid.length;
											uuidlist.add(oneuuid);
										}
										re.setUuidList(uuidlist);
										
									}else if (cmdType == (byte) 0x0A) {
										int next = 0;

										// key
										byte[] key = new byte[4];
										System.arraycopy(body, next, key, 0, key.length);
										next += key.length;
										re.setKey(key);
										
										byte[] typeValue = new byte[2];
										System.arraycopy(body, next, typeValue, 0, typeValue.length);
										next += typeValue.length;
										re.setTypeValue(typeValue);
										
										byte uuidnum = body[next++];
										re.setUuidNum(uuidnum);
										int IbuuidNumber=uuidnum;
										
										int alluuidlen=body.length-7;
										int uuidlen=alluuidlen/IbuuidNumber;
										List<byte[]> uuidlist =new ArrayList<byte[]>();
										for(int i=0;i<IbuuidNumber;i++){
											byte[] oneuuid =new byte[uuidlen];
											System.arraycopy(body, next, oneuuid, 0, oneuuid.length);
											next += oneuuid.length;
											uuidlist.add(oneuuid);
										}
										re.setUuidList(uuidlist);
										
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			re.setCheckPass(false);
			re.setExpCode(ReObject.expCodeType.DATA_FAIL);
			re.setExpMsg(ReObject.expMsgType.DATA_FAIL + (e == null ? "" : e.getMessage()));
		}

		return re;
	}
	
	public static void main(String[] args) {

			byte[] info = TextTools.hexStringToByteArray("A6FF");
			ReObject reObject = new ProtocelKit().getReObject(TextTools.hexString2Bytes("AA080020060491280000014170706C652050656E63696C000000000000000000000000009755"));
		 
			NettyServerHandler nettyServerHandler = new NettyServerHandler();
			List<byte[]> uuidList = reObject.getUuidList();
			byte[] typeValue = reObject.getTypeValue();
			String type = TextTools.byteToHexString(typeValue);
			byte uuidNum = reObject.getUuidNum();
			if ("0001".endsWith(type)) {
				 
				nettyServerHandler.uuidchuliA(uuidList, typeValue, uuidNum);
			} else { 
				nettyServerHandler.uuidchuli(uuidList, typeValue, uuidNum);
			}
					
	}

	// 获取升级包信息
	public UpgradePackage getUpgradePackage(byte[] key, byte[] version, String filePathAndName) {
		UpgradePackage re = null;

		int maxLen = 1000;

		if (key != null && key.length > 0 && version != null && version.length > 0
				&& !TextTools.isEmpty(filePathAndName)) {
			File source = new File(filePathAndName);
			if (source != null && source.exists()) {
				try {
					re = new UpgradePackage();

					FileInputStream fis = new FileInputStream(source);
					ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
					byte[] b = new byte[1000];
					int n;
					while ((n = fis.read(b)) != -1) {
						bos.write(b, 0, n);
					}
					fis.close();
					bos.close();
					byte[] buffer = bos.toByteArray();
					int bufLen = buffer.length;
					int num = 0;
					int cha = 0;
					if (bufLen % maxLen == 0) {
						num = bufLen / maxLen;
					} else {
						num = bufLen / maxLen + 1;
						cha = bufLen - (num - 1) * maxLen;
					}

					byte[] tmp = null;
					int idx = 0;
					List<byte[]> upgradePackageList = new ArrayList<byte[]>();
					for (int i = 0; i < num; i++) {
						if (idx + maxLen <= bufLen) {
							tmp = new byte[maxLen];
						} else {
							tmp = new byte[cha];
						}

						System.arraycopy(buffer, idx, tmp, 0, tmp.length);
						idx += tmp.length;

						int len = 4 + 1 + tmp.length;
						byte[] body = new byte[len];
						int next = 0;

						// key
						System.arraycopy(key, 0, body, next, key.length);
						next += key.length;

						// 包号
						body[next++] = (byte) (i + 1);

						// 分解的数据包之一
						System.arraycopy(tmp, 0, body, next, tmp.length);
						next += tmp.length;

						byte[] upgradePackage = getCmd((byte) 0x05, body);
						upgradePackageList.add(upgradePackage);
					}
					// 将数据包列表保存入返回对象
					re.setUpgradePackageList(upgradePackageList);

					// 拼引导包
					int bootBodyLen = key.length + version.length + 1 + 2 + 2;
					byte[] bootBody = new byte[bootBodyLen];
					int next = 0;

					// key
					System.arraycopy(key, 0, bootBody, next, key.length);
					next += key.length;

					// version
					System.arraycopy(version, 0, bootBody, next, version.length);
					next += version.length;

					// 总包数
					bootBody[next++] = (byte) num;

					// 最后包的总长度
					byte[] endPackageLen = TextTools.intToByte2(cha);
					System.arraycopy(endPackageLen, 0, bootBody, next, endPackageLen.length);
					next += endPackageLen.length;

					// 文件总CRC校验
					int crc = calcCrc16(buffer, 0, buffer.length);
					byte[] crcBytes = TextTools.intToByte2(crc);
					System.arraycopy(crcBytes, 0, bootBody, next, crcBytes.length);
					next += crcBytes.length;

					byte[] bootInfo = getCmd((byte) 0x04, bootBody);
					re.setBootInfo(bootInfo);
				} catch (Exception e) {
					e.printStackTrace();
					re = null;
				}
			}
		}

		return re;
	}

	public byte[] reDevConnectInfo(boolean isConnected, byte[] key, byte[] devHartbeatCode) {
		byte[] re = null;

		if (key != null && key.length > 0 && devHartbeatCode != null && devHartbeatCode.length > 0) {
			int len = 1 + 4 + 4;
			byte[] body = new byte[len];
			int next = 0;

			// 状态
			if (isConnected) {
				body[next++] = (byte) 0x01;
			} else {
				body[next++] = (byte) 0x00;
			}

			// key
			System.arraycopy(key, 0, body, next, key.length);
			next += key.length;

			// 心跳标签号
			System.arraycopy(devHartbeatCode, 0, body, next, devHartbeatCode.length);
			next += devHartbeatCode.length;

			re = getCmd((byte) 0xF0, body);
		}

		return re;
	}

	public byte[] setDevParam(byte[] key, byte paramType, byte[] paramValue) {
		byte[] re = null;

		if (key != null && key.length > 0 && paramValue != null && paramValue.length > 0) {
			int len = key.length + 1 + paramValue.length;
			byte[] body = new byte[len];
			int next = 0;

			// key
			System.arraycopy(key, 0, body, next, key.length);
			next += key.length;

			// 参数类型
			body[next++] = paramType;

			// 参数值
			System.arraycopy(paramValue, 0, body, next, paramValue.length);
			next += paramValue.length;

			re = getCmd((byte) 0x03, body);
		}

		return re;
	}

	private byte[] reRecieveInfo(byte type, byte[] key, byte state) {
		byte[] re = null;

		if (key != null && key.length > 0) {
			int len = 4 + 1;
			byte[] body = new byte[len];
			int next = 0;

			// key
			System.arraycopy(key, 0, body, next, key.length);
			next += key.length;

			// 状态
			body[next++] = state;

			re = getCmd(type, body);
		}

		return re;
	}

	public byte[] reRecieveCodeListInfo(byte[] key, byte state) {
		return reRecieveInfo((byte) 0xF2, key, state);
	}
	public byte[] btreRecieveCodeListInfo(byte[] key, byte state) {
		return reRecieveInfo((byte) 0xF8, key, state);
	}

	public byte[] reRecieveDevStateByteInfo(byte[] key, byte state) {
		return reRecieveInfo((byte) 0xF6, key, state);
	}

	public byte[] sendHartbeat() {
		byte[] re = null;

		re = getCmd((byte) 0x21, new byte[] { (byte) 0xff });

		return re;
	}

}
