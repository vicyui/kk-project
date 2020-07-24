package com.okboss.api.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * 通用工具类
 */
public class CommonUtil {

	/**
	 * 生成6位随机码
	 *
	 * @param length
	 * @return 邀请码
	 */
	public static String getRandomCode(int length) {
		String val = "";
		Random random = new Random();

		//参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {
			//随机数由0-9，a-z,A-Z组成，数字占10个，字母占52个，数字、字母占比1:5（标准的应该是10:52）
			//random.nextInt(6) 0-5中6个数取一个
			String charOrNum = (random.nextInt(6) + 6) % 6 >= 1 ? "char" : "num";
			//输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				//输出是大写字母还是小写字母，输出比例为1:1
				int temp = random.nextInt(2) % 2 == 0 ? 97 : 65;
				//char（65）-char(90) 为大写字母A-Z；char(97)-char(122)为小写字母a-z
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static String md5(String str) throws Exception {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}
}
