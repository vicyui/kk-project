package com.okboss.api.utils.sms;

import com.okboss.api.config.Config;
import com.okboss.api.utils.CommonUtil;
import com.okboss.api.utils.HttpClientUtil;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * 美唐云 短信
 */
public class MeitangyunClient {

	public String sendSms(String mobile, String code) {
		String result = "";
		Map<String,String> param = new HashMap<>();
		param.put("account", account);
//		param.put("password", password);
		String timpstamp = Long.toString(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		param.put("timestamp", timpstamp);
		param.put("receiver", mobile);
		param.put("extcode", "8");

		try {
//			System.out.println(MessageFormat.format(smscontent, code));
			param.put("smscontent", MessageFormat.format(smscontent, code));
			param.put("access_token", CommonUtil.md5(timpstamp + password));
			result = HttpClientUtil.post(server, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private final String server = Config.getInstance().getProperty("sms.mty.server");
	private final String account = Config.getInstance().getProperty("sms.mty.account");
	private final String password = Config.getInstance().getProperty("sms.mty.password");
	private final String smscontent = Config.getInstance().getProperty("sms.mty.smscontent");
}
