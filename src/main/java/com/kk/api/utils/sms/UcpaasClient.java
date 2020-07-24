
package com.kk.api.utils.sms;

import com.alibaba.fastjson.JSONObject;
import com.kk.api.config.Config;
import com.kk.api.utils.HttpClientUtil;

/**
 * 云之讯 短信
 */
public class UcpaasClient{

	public String sendSms(String templateid, String param, String mobile, String uid) {
		String result = "";
		try {
			String url = getStringBuffer().append("/sendsms").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("param", param);
			jsonObject.put("mobile", mobile);
			jsonObject.put("uid", uid);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String sendSms(String param, String mobile, String uid) {
		String result = "";
		try {
			String url = getStringBuffer().append("/sendsms").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("param", param);
			jsonObject.put("mobile", mobile);
			jsonObject.put("uid", uid);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String sendSmsBatch(String templateid, String param, String mobile, String uid) {
		String result = "";
		try {
			String url = getStringBuffer().append("/sendsms_batch").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("param", param);
			jsonObject.put("mobile", mobile);
			jsonObject.put("uid", uid);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String addSmsTemplate(String type, String template_name, String autograph, String content) {
		String result = "";
		try {
			String url = getStringBuffer().append("/addsmstemplate").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("type", type);
			jsonObject.put("template_name", template_name);
			jsonObject.put("autograph", autograph);
			jsonObject.put("content", content);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getSmsTemplate(String templateid, String page_num, String page_size) {
		String result = "";
		try {
			String url = getStringBuffer().append("/getsmstemplate").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("page_num", page_num);
			jsonObject.put("page_size", page_size);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String editSmsTemplate(String templateid, String type, String template_name, String autograph, String content) {
		String result = "";
		try {
			String url = getStringBuffer().append("/editsmstemplate").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("type", type);
			jsonObject.put("template_name", template_name);
			jsonObject.put("autograph", autograph);
			jsonObject.put("content", content);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String deleteSmsTemplate(String templateid) {
		String result = "";
		try {
			String url = getStringBuffer().append("/deletesmstemplate").toString();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);

			String body = jsonObject.toJSONString();

			System.out.println("body = " + body);

			result = HttpClientUtil.post(url, body);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public StringBuffer getStringBuffer() {
		StringBuffer sb = new StringBuffer();
		sb.append(server);
		return sb;
	}

	private final String server = Config.getInstance().getProperty("sms.ucpaas.server");
	private final String appid = Config.getInstance().getProperty("sms.ucpaas.appid");
	private final String sid = Config.getInstance().getProperty("sms.ucpaas.sid");
	private final String token = Config.getInstance().getProperty("sms.ucpaas.token");
	private final String templateid = Config.getInstance().getProperty("sms.ucpaas.templateid");
}
