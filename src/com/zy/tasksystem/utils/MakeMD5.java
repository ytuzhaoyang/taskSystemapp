package com.zy.tasksystem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MakeMD5 {

	public static String md5(String password) {

		MessageDigest digest;
		byte[] result = null;
		try {
			digest = MessageDigest.getInstance("md5");
			result = digest.digest(password.getBytes());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		StringBuffer buffer = new StringBuffer();
		for (byte b : result) {
			// 0xff��ʮ����ƣ�ʮ����Ϊ255
			int nuber = b & 0xff;
			String str = Integer.toHexString(nuber);
			if (str.length() == 1) {
				buffer.append("0");
			}
			buffer.append(str);
		}
		return buffer.toString();
	}
}
