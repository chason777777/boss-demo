package com.chason.bossdemo.common.util;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

@SuppressWarnings("restriction")
public class Base64Util {

	public static String base64EncodeString(String s) {
		if (StringUtils.isEmpty(s))
			return null;
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(s.getBytes());
	}
	
	public static String base64EncodeByte(byte[] s) {
		if (null == s)
			return null;
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(s);
	}

	public static String base64DecodeString(String s) {
		if (StringUtils.isEmpty(s))
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return new String(decoder.decodeBuffer(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] base64DecodeByte(String s) {
		if (StringUtils.isEmpty(s))
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
	}
}
