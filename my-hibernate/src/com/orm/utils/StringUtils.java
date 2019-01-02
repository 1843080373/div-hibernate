package com.orm.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.orm.bean.HandleHQLResult;
import com.orm.bean.Param;

public class StringUtils {

	public static String fistCharUpperCase(String src) {
		return src.substring(0, 1).toUpperCase() + src.substring(1);
	}

	public static String str2Package(String src) {
		return src.substring(0, src.length() - ".xml".length()).replaceAll("/", ".");
	}

	public static String get$StrValue(String src) {
		Pattern p = Pattern.compile("\\{[^\\}]*\\}");
		Matcher m = p.matcher(src);
		String rs = null;
		while (m.find()) {
			String item = src.substring(m.start(), m.end());
			rs = item.substring(1, item.length() - 1);
		}
		return rs;
	}

	public static String getSubUtilSimple(String soap, String rgex) {
		Pattern pattern = Pattern.compile(rgex);// Æ¥ÅäµÄÄ£Ê½
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1).replaceAll(" ", "");
		}
		return "";
	}

	public static void main(String[] args) {
		HandleHQLResult map = handleHQL("from User where phone=:phone and userName=:name");
		System.out.println(JSONObject.toJSON(map));
	}

	public static HandleHQLResult handleHQL(String hql) {
		try {
			HandleHQLResult handleHQLResult = new HandleHQLResult();
			Pattern p1 = Pattern.compile("(?<=\\s)[^\\s\\:]+(?=\\:)");
			Pattern p2 = Pattern.compile(":[\\S]+");
			Matcher m1 = p1.matcher(hql);
			String ps = hql;
			int i = 1;
			LinkedList<Param> params = handleHQLResult.getParams();
			while (m1.find()) {
				String item = hql.substring(m1.start(), m1.end());
				Param param = new Param();
				param.setName(item.substring(0, item.length() - 1));
				param.setPos(i);
				i++;
				params.add(param);
			}
			Matcher m2 = p2.matcher(hql);
			i=0;
			while (m2.find()) {
				String properties = hql.substring(m2.start(), m2.end());
				params.get(i).setProperties(properties);
				i++;
			}
			String rgex = "from(.*?)where";
			handleHQLResult.setTableName(getSubUtilSimple(hql, rgex));
			return handleHQLResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
