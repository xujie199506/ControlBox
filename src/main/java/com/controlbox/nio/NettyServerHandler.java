package com.controlbox.nio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.controlbox.controller.Content;
import com.controlbox.protocol.ProtocelKit;
import com.controlbox.protocol.utlis;
import com.controlbox.protocol.bean.ReObject;
import com.controlbox.protocol.bean.UpgradePackage;
import com.sun.corba.se.impl.orbutil.closure.Constant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class NettyServerHandler extends SimpleChannelInboundHandler<byte[]> {

	public static byte[] key = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		String remoteAddress = getRemoteAddress(ctx);
		logger.error("   CLIENT" + remoteAddress + "park连入");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// channel失效，从Map中移除
		NettyChannelMap.remove((SocketChannel) ctx.channel());
		logger.info("   CLIENT" + getRemoteAddress(ctx) + "park断开,此时大小:" + NettyChannelMap.getmap().size());
	}

	List<byte[]> upgradePackageList = null;
	Map<String, Integer> chchcch = new HashMap();

	private byte[] cache = null;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, byte[] msg) throws Exception {

		String byteToHexString = TextTools.byteToHexString(msg);
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------------");

		logger.info("    接收" + NettyChannelMap.geteqkey((SocketChannel) ctx.channel()) + "       " + msg.length
				+ "            " + byteToHexString + "       ");
		if (true) {
			main(ctx,msg);
			return;
		}
		ProtocelKit protocelKit = new ProtocelKit();
		ReObject reObject = protocelKit.getReObject(msg);
		if (!reObject.isCheckPass()) {
			System.out.println(reObject.getExpMsg());

			if (cache == null) {
				cache = msg;
				return;
			} else {
				byte[] cachelinshi = new byte[cache.length + msg.length];
				System.arraycopy(cache, 0, cachelinshi, 0, cache.length);
				System.arraycopy(msg, 0, cachelinshi, cache.length, msg.length);
				ProtocelKit protocelKit2 = new ProtocelKit();
				ReObject rb = protocelKit2.getReObject(cachelinshi);
				if (rb.isCheckPass()) {
					reObject = rb;
					cache = null;
					System.out.println("拼成功");
				} else {
					System.out.println("拼失败");
					cache = cachelinshi;
					return;
				}
			}
		} else {
			cache = null;
		}

		CMDHandle(ctx, protocelKit, reObject);

	}

	private byte[] cache2 = null;

	public void main(ChannelHandlerContext ctx, byte[] src) {
		if (cache2 != null) {
			byte[] transfer = new byte[cache2.length + src.length];
			System.arraycopy(cache2, 0, transfer, 0, cache2.length);
			System.arraycopy(src, 0, transfer, cache2.length, src.length);
			src = transfer;
			cache2 = null;
		}

		ProtocelKit protocelKit = new ProtocelKit();
		ReObject reObject = protocelKit.getReObject(src);
		if (reObject.isCheckPass()) {
			CMDHandle(ctx, protocelKit, reObject);
			// todo 处理
			System.out.println("3");
			return;
		}

		List<Integer> headIndexs = getHeadIndexs(src);
		if (headIndexs.size() > 1) {
			byte[] cachelinshi = new byte[headIndexs.get(1)];
			System.arraycopy(src, 0, cachelinshi, 0, cachelinshi.length);
		}
		if (headIndexs.size() < 1) {
			cache2 = src;
			return;
		}

		int count = -1;

		for (int i = 0; i < headIndexs.size(); i++) {
			byte[] cachelinshi = null;
			int index = headIndexs.get(i);
			if ((index + 6) < src.length) {
				byte[] bodlen_bytes = new byte[2];
				System.arraycopy(src, index + 2, bodlen_bytes, 0, bodlen_bytes.length);
				int bodlen = TextTools.byte2ToUnsignedShort(bodlen_bytes);
				int cmdLen = 6 + bodlen;
				if (index + cmdLen <= src.length) {
					cachelinshi = new byte[cmdLen];
					System.arraycopy(src, index, cachelinshi, 0, cachelinshi.length);
					System.out.println(TextTools.byteToHexString(cachelinshi));
					ReObject reObject2 = protocelKit.getReObject(cachelinshi);
					if (reObject2.isCheckPass()) {
						count = (index + cmdLen);
						CMDHandle(ctx, protocelKit, reObject2);
					}
				}
			}
		}

		if (count >= 0) {
			byte[] cache = new byte[src.length - count];
			System.arraycopy(src, count, cache, 0, cache.length);
			System.out.println(TextTools.byteToHexString(cache));
			if (count != src.length) {
				cache2 = cache;
			}else{
				cache2=null;
				System.out.println("1");
			}
		} else {
			cache2 = src;
			System.out.println("2");
		}

	}

	private static List<Integer> getHeadIndexs(byte[] msg) {
		List<Integer> indexs = new ArrayList<Integer>();

		for (int i = 0; i < msg.length; i++) {
			if (((byte) 0xAA) == msg[i]) {
				indexs.add(i);
			}

		}
		return indexs;
	}

	private void CMDHandle(ChannelHandlerContext ctx, ProtocelKit protocelKit, ReObject reObject) {
		byte[] reData = null;
		if (reObject != null) {
			byte cmdType = reObject.getCmdType();

			switch (cmdType) {
			case (byte) 0x00:
				logger.info("命令类型    模块登陆 ");

				reData = protocelKit.reDevConnectInfo(true, key, Content.devHartbeatCode);
				byte[] devNo = reObject.getDevNo();
				String devnumber = TextTools.byteToHexString(devNo);
				logger.error(devnumber + "------- park link");
				NettyChannelMap.add(devnumber, (SocketChannel) ctx.channel());
				break;
			case (byte) 0x01:
				logger.info("命令类型   心跳 ");
				reData = protocelKit.sendHartbeat();
				// reData=TextTools.hexString2Bytes("AA");
				break;
			case (byte) 0x02:
				// System.out.println("命令类型 卡号上传 ");
				List<byte[]> codeList = reObject.getCodeList();
				String codeli = "";
				List<String> codeliststr = new ArrayList<String>();
				for (int i = 0; i < codeList.size(); i++) {

					codeli += ("    " + TextTools.byteToHexString(codeList.get(i)));
					codeliststr.add(TextTools.byteToHexString(codeList.get(i)));
				}
				System.out.println("卡号：" + codeli);
				utlis.num += codeList.size();
				System.out.println("卡数量：    " + utlis.num);
				reData = protocelKit.reRecieveCodeListInfo(key, (byte) 0x01);
				break;
			case (byte) 0xF3:
				// System.out.println("命令类型 设置命令");
				// byte parmType = reObject.getParmType();
				// String sparmType = TextTools.byteToHexString(new
				// byte[]{parmType});
				byte reState2 = reObject.getReState();
				String sya = "";
				if (((byte) 0x01 == reState2)) {
					sya = "成功";
				} else {
					sya = "失败";
				}
				// System.out.println(" 设置"+sya);
				break;
			case (byte) 0x04:

				break;
			case (byte) 0xF5:
				logger.info("命令类型   升级状态");
				byte reState = reObject.getReState();
				int v = chchcch.get(getRemoteAddress(ctx));
				if (reState == ((byte) 0x01)) {
					int uu = v + 1;
					chchcch.put(getRemoteAddress(ctx), uu);
				}
				v = chchcch.get(getRemoteAddress(ctx));
				if (v < upgradePackageList.size()) {
					reData = upgradePackageList.get(v);
				} else {
					logger.info("升级结束" + v);

				}
				logger.info("数据包总长度" + (upgradePackageList.size() - 1) + "   当前数据包" + v);
				break;
			case (byte) 0x06:
				byte reState1 = reObject.getReState();
				if (((byte) 0x01 == reState1)) {
					sya = "正常";
				} else {
					sya = "异常";
				}
				logger.info("命令类型   模块状态" + sya);
				// reData =
				// protocelKit.reRecieveDevStateByteInfo(Content.key,(byte)0x01);
				break;
			case (byte) 0x21:

				break;
			case (byte) 0xF4:
				byte[] version = reObject.getVersion();
				UpgradePackage upgradePackage = Content.upgradePackage;
				upgradePackageList = upgradePackage.getUpgradePackageList();
				reData = upgradePackageList.get(0);
				chchcch.put(getRemoteAddress(ctx), 0);
				break;
			case (byte) 0xF7:
				byte stateType = reObject.getStateType();
				byte stateValue = reObject.getStateValue();
				// byte[] state=new byte[]{stateValue};
				// String statehax = TextTools.byteToHexString(state);
				int iii = stateValue;
				if ((byte) 0x00 == stateType) {
					System.out.println("信号强度：-" + iii);
				} else if ((byte) 0x01 == stateType) {
					int height4 = getHeight4(stateValue);
					int Low4 = getLow4(stateValue);
					String wllx = "";
					switch (height4) {
					case (byte) 0x00:
						wllx = "No Service";
						break;
					case (byte) 0x01:
						wllx = "Limited Service";
						break;
					case (byte) 0x02:
						wllx = "GSM";
						break;
					case (byte) 0x03:
						wllx = "GPRS";
						break;
					case (byte) 0x04:
						wllx = "CDMA";
						break;
					case (byte) 0x05:
						wllx = "EVDO";
						break;
					case (byte) 0x06:
						wllx = "EHRPD";
						break;
					case (byte) 0x07:
						wllx = "UMTS";
						break;
					case (byte) 0x08:
						wllx = "HSDPA";
						break;
					case (byte) 0x09:
						wllx = "HSUPA";
						break;
					case (byte) 0x0A:
						wllx = "HSPA";
						break;
					case (byte) 0x0B:
						wllx = "HSPA+";
						break;
					case (byte) 0x0C:
						wllx = "LTE";
						break;
					case (byte) 0x0D:
						wllx = "TD-SCDMA";
						break;
					case (byte) 0x0E:
						wllx = "未知";
						break;
					default:
						wllx = "未知类型";
						break;

					}

					String fwlx = "";
					switch (Low4) {
					case (byte) 0x00:
						fwlx = "CS_ONLY（CS domain service available.）";

						break;
					case (byte) 0x01:
						fwlx = "PS_ONLY（PS domain service available.）";
						break;
					case (byte) 0x02:
						fwlx = "CS_PS（CS&PS domain service available.）";
						break;
					case (byte) 0x03:
						fwlx = "CAMPED（camped in a cell.）";
						break;
					default:
						fwlx = "未知类型";
						break;
					}

					System.out.println("网络类型：" + wllx + "           服务类型" + fwlx);
				}
				break;

			case (byte) 0x08:
				List<byte[]> uuidList = reObject.getUuidList();
				byte[] typeValue = reObject.getTypeValue();
				String type = TextTools.byteToHexString(typeValue);
				byte uuidNum = reObject.getUuidNum();
				if ("0001".endsWith(type)) {
					logger.warn("------------------v2标签-----------------------");
					uuidchuliA(uuidList, typeValue, uuidNum);
				} else {
					logger.warn("------------------v1标签-----------------------");
					uuidchuli(uuidList, typeValue, uuidNum);
				}
				reData = protocelKit.btreRecieveCodeListInfo(key, (byte) 0x01);
				break;
			case (byte) 0x0A:
				List<byte[]> AuuidList = reObject.getUuidList();
				byte[] AtypeValue = reObject.getTypeValue();
				byte AuuidNum = reObject.getUuidNum();
				uuidchuliA(AuuidList, AtypeValue, AuuidNum);
				reData = protocelKit.btreRecieveCodeListInfo(key, (byte) 0x01);
				break;
			case (byte) 0xFA:
				List<byte[]> uuidList2 = reObject.getUuidList();
				byte[] typeValue2 = reObject.getTypeValue();
				byte uuidNum2 = reObject.getUuidNum();
				logger.info("-----------------------统计开始----------------");
				String type2 = TextTools.byteToHexString(typeValue2);
				if ("0001".endsWith(type2)) {
					logger.warn("------------------v2标签-----------------------");
					uuidchuliA(uuidList2, typeValue2, uuidNum2);
				} else {
					logger.warn("------------------v1标签-----------------------");
					uuidchuli(uuidList2, typeValue2, uuidNum2);
				}
				logger.info("-----------------------统计结束----------------");
				break;
			default:
				break;
			}

			if (reData != null) {
				String rerere = TextTools.byteToHexString(reData);

				logger.info(getRemoteAddress(ctx) + "    " + rerere);
				System.out.println();
				ctx.channel().writeAndFlush(reData);
			}
		}
	}

	public void uuidchuli(List<byte[]> uuidList, byte[] typeValue, byte uuidNum) {

		System.out.println("---------------------------------------");

		logger.info("ibeacon总数：" + uuidList.size());
		for (int i = 0; i < uuidList.size(); i++) {
			byte[] body = uuidList.get(i);
			// System.out.println(TextTools.byteToHexString(body));
			try {
				int next = 0;
				String msg = "";
				byte[] CompanyID = new byte[2];
				System.arraycopy(body, next, CompanyID, 0, CompanyID.length);
				next += CompanyID.length;
				byte device_type = body[next++];

				byte lenth = body[next++];
				byte[] uuid = new byte[16];
				System.arraycopy(body, next, uuid, 0, uuid.length);
				next += uuid.length;
				msg += "" + TextTools.byteToHexString(uuid) + "----uuid              ";
				msg += "----公司id" + TextTools.byteToHexString(CompanyID);
				msg += "----长度" + TextTools.byteToHexString(new byte[] { lenth });
				msg += "----设备类型" + TextTools.byteToHexString(new byte[] { device_type });
				byte[] major = new byte[2];
				System.arraycopy(body, next, major, 0, major.length);
				next += major.length;
				msg += "----站点编号" + TextTools.byteToHexString(major);
				msg += "----长度" + TextTools.byteToHexString(new byte[] { lenth });
				byte power = body[next++];
				msg += "----power" + TextTools.byteToHexString(new byte[] { power });
				byte outin = body[next++];
				msg += "----进出场" + TextTools.byteToHexString(new byte[] { outin });
				byte rssi = body[next++];
				msg += "----rssi" + TextTools.byteToHexString(new byte[] { rssi });

				if (TextTools.byteToHexString(new byte[] { outin }).equals("01")) {
					utlis.set.add(TextTools.byteToHexString(uuid) + TextTools.byteToHexString(new byte[] { outin }));
				} else {
					utlis.setout.add(TextTools.byteToHexString(uuid) + TextTools.byteToHexString(new byte[] { outin }));
				}
				logger.warn(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		logger.warn("v1进场：" + utlis.set.size());

		logger.warn("v1出场：" + utlis.setout.size());
		System.out.println("---------------------------------------");
	}

	public void uuidchuliA(List<byte[]> uuidList, byte[] typeValue, byte uuidNum) {

		System.out.println("---------------------------------------");

		logger.info("beacon总数：" + uuidList.size());
		for (int i = 0; i < uuidList.size(); i++) {
			byte[] body = uuidList.get(i);
			// System.out.println(TextTools.byteToHexString(body));
			try {
				int next = 0;
				String msg = "";
				byte[] CompanyID = new byte[2];
				System.arraycopy(body, next, CompanyID, 0, CompanyID.length);
				next += CompanyID.length;
				byte device_type = body[next++];

				byte lenth = body[next++];
				byte[] uuid = new byte[8];
				System.arraycopy(body, next, uuid, 0, uuid.length);
				next += uuid.length;

				byte[] filter = new byte[2];
				System.arraycopy(body, next, filter, 0, filter.length);
				next += filter.length;

				byte[] bcnpagcnt = new byte[2];
				System.arraycopy(body, next, bcnpagcnt, 0, bcnpagcnt.length);
				next += bcnpagcnt.length;

				msg += "" + TextTools.byteToHexString(uuid) + "----uuid              ";
				msg += "----公司id" + TextTools.byteToHexString(CompanyID);
				msg += "----长度" + TextTools.byteToHexString(new byte[] { lenth });
				msg += "----设备类型" + TextTools.byteToHexString(new byte[] { device_type });
				byte[] major = new byte[2];
				System.arraycopy(body, next, major, 0, major.length);
				next += major.length;
				msg += "----站点编号" + TextTools.byteToHexString(major);

				msg += "----过滤字节" + TextTools.byteToHexString(filter);
				msg += "----滚动码" + TextTools.byteToHexString(bcnpagcnt);

				msg += "----长度" + TextTools.byteToHexString(new byte[] { lenth });
				byte outin = body[next++];
				msg += "----进出场" + TextTools.byteToHexString(new byte[] { outin });
				byte power = body[next++];
				msg += "----power" + TextTools.byteToHexString(new byte[] { power });
				byte rssi = body[next++];
				msg += "----rssi" + TextTools.byteToHexString(new byte[] { rssi });

				if (TextTools.byteToHexString(new byte[] { outin }).equals("01")) {
					utlis.setv2.add(TextTools.byteToHexString(uuid) + TextTools.byteToHexString(new byte[] { outin }));
					Integer integer = utlis.mapinv2.get(TextTools.byteToHexString(uuid));
					if(integer==null){
						integer=1;
					}else{
					integer=integer+1;
					} 
					utlis.mapinv2.put(TextTools.byteToHexString(uuid),integer);
					
				} else {
					utlis.setoutv2
							.add(TextTools.byteToHexString(uuid) + TextTools.byteToHexString(new byte[] { outin }));
					Integer integer = utlis.mapoutv2.get(TextTools.byteToHexString(uuid));
					if(integer==null){
						integer=1;
					}else{
					integer=integer+1;
					}
					utlis.mapoutv2.put(TextTools.byteToHexString(uuid),integer);
				}
				logger.warn(msg);
			} catch (Exception e) {

			}

		}

		logger.warn("v2进场：" + utlis.setv2.size());

		logger.warn("v2出场：" + utlis.setoutv2.size());

		System.out.println("---------------------------------------");

		System.out.println("------------------------出场---------------");
		 for (String o : utlis.mapoutv2.keySet()) {
			   System.out.println("标签=" + o + " 次数=" + utlis.mapoutv2.get(o));
			  }
		System.out.println("------------------进场---------------------");

		 for (String o : utlis.mapinv2.keySet()) {
			   System.out.println("标签=" + o + " 次数=" + utlis.mapinv2.get(o));
			  }

		
		System.out.println("---------------------------------------");
	}

	public static String getRemoteAddress(ChannelHandlerContext ctx) {
		String socketString = "";
		socketString = ctx.channel().remoteAddress().toString();
		return socketString;
	}

	public static byte getHeight4(byte data) {// 获取高四位
		byte height;
		height = (byte) ((data & 0xf0) >> 4);
		return height;
	}

	public static byte getLow4(byte data) {// 获取低四位
		byte low;
		low = (byte) (data & 0x0f);
		return low;
	}

}