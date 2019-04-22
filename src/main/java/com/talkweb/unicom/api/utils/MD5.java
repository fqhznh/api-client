package com.talkweb.unicom.api.utils;


import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5加密
 * @author fqh
 */
public class MD5 {

	private static char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};


	/**
	 * 对字符串md5加密(大写+数字)
	 *
	 * @param value 传入要加密的字符串
	 * @return  MD5加密后的字符串
	 */

	public static String getMD5(String value) {

		try {
			byte[] btInput = value.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(getMD5("123456"));
		System.out.println(getMD5("kc&1526653701&UWtRU8CRSeErVMmVCPNskbt5KR/gZ2Y2S/kZYXr0i8U=&5ec017c185f24ecca72fcbb9"));
	}


}