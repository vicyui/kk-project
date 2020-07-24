package com.okboss.api.utils;


import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段验证工具类
 */
public class RegexUtil {

	private static final String MOBILE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$"; // 移动手机号码

	private static final String ID_CARD_REG = "^(\\d{6})()?(\\d{4})(\\d{2})(\\d{2})(\\d{3})(\\w)$"; // 身份证号码

	/**
	 * 匹配URL地址
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:28:53
	 */
	public final static boolean isUrl(String str) {
		return match(str, "^(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$");
	}

	/**
	 * 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:28:57
	 */
	public final static boolean isPwd(String str) {
		return match(str, "^[a-zA-Z]\\w{6,12}$");
	}

	/**
	 * 验证字符，只能包含中文、英文、数字、下划线等字符。
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:02
	 */
	public final static boolean stringCheck(String str) {
		return match(str, "^[a-zA-Z0-9\u4e00-\u9fa5-_]+$");
	}

	/**
	 * 验证字符，只能包含中文、英文、数字。
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:02
	 */
	public final static boolean stringCheckUsername(String str) {
		return match(str, "^[a-zA-Z0-9\u4e00-\u9fa5]+$");
	}

	/**
	 * 匹配Email地址
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:08
	 */
	public final static boolean isEmail(String str) {
		return match(str, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}

	/**
	 * 匹配非负整数（正整数+0）
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:12
	 */
	public final static boolean isInteger(String str) {
		return match(str, "^[+]?\\d+$");
	}

	/**
	 * 判断数值类型，包括整数和浮点数
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:16
	 */
	public final static boolean isNumeric(String str) {
		if (isFloat(str) || isInteger(str))
			return true;
		return false;
	}

	/**
	 * 只能输入数字
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:20
	 */
	public final static boolean isDigits(String str) {
		return match(str, "^[0-9]*$");
	}

	/**
	 * 匹配正浮点数
	 *
	 * @param str
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:23
	 */
	public final static boolean isFloat(String str) {
		return match(str, "^[-\\+]?\\d+(\\.\\d+)?$");
	}

	/**
	 * 手机号码验证
	 *
	 * @param text
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:33
	 */
	public final static boolean isMobile(String text) {
		if (text.length() != 11)
			return false;
		return match(text, MOBILE_NUMBER_REG);
	}

	/**
	 * 身份证号码验证
	 *
	 * @param text
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:37
	 */
	public final static boolean isIdCardNo(String text) {
		return match(text, ID_CARD_REG);
	}

	/**
	 * 邮政编码验证
	 *
	 * @param text
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:29:40
	 */
	public final static boolean isZipCode(String text) {
		return match(text, "^[0-9]{6}$");
	}

	/**
	 * 是否包含中英文特殊字符，除英文"-_"字符外
	 *
	 * @param text
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:30:19
	 */
	public static boolean isContainsSpecialChar(String text) {
		if (StringUtils.isEmpty(text))
			return false;
		String[] chars = {"[", "`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+", "=", "|", "{", "}", "'", ":", ";", "'", ",", "[",
				"]", ".", "<", ">", "/", "?", "~", "！", "@", "#", "￥", "%", "…", "&", "*", "（", "）", "—", "+", "|", "{", "}", "【", "】", "‘", "；", "：",
				"”", "“", "’", "。", "，", "、", "？", "]"};
		for (String ch : chars) {
			if (text.contains(ch))
				return true;
		}
		return false;
	}

	/**
	 * 过滤中英文特殊字符，除英文"-_"字符外
	 *
	 * @param text
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:30:23
	 */
	public static String stringFilter(String text) {
		String regExpr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regExpr);
		Matcher m = p.matcher(text);
		return m.replaceAll("").trim();
	}

	/**
	 * 过滤html代码
	 *
	 * @param inputString 含html标签的字符串
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:30:27
	 */
	public static String htmlFilter(String inputString) {
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_ba;
		java.util.regex.Matcher m_ba;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String patternStr = "\\s+";

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_ba = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
			m_ba = p_ba.matcher(htmlStr);
			htmlStr = m_ba.replaceAll(""); // 过滤空格

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 正则表达式匹配
	 *
	 * @param text 待匹配的文本
	 * @param reg  正则表达式
	 * @return
	 * @auther kk
	 * @date 2018年1月25日 下午2:30:32
	 */
	public final static boolean match(String text, String reg) {
		if (StringUtils.isEmpty(text) || StringUtils.isEmpty(reg))
			return false;
		return Pattern.compile(reg).matcher(text).matches();
	}
}
