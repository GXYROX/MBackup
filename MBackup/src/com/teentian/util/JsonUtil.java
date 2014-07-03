package com.teentian.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;


public class JsonUtil {

	private static List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
	private static Map<String, Object> dataItem = new HashMap<String, Object>();
	
	
	public static List<HashMap<String, Object>> parseJSON(Context context, String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONArray array = jsonObject.getJSONArray("dataInfo");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String article_id = obj.getString("article_id");
				String title = obj.getString("title");
				String add_time = obj.getString("add_time");
				String content = obj.getString("content");
				String link = obj.getString("link");
				String shop_code = obj.getString("shop_code");
				String shop_id = obj.getString("shop_id");
				String art_image = obj.getString("art_image");
				String description = obj.getString("description");

				System.out.println("JsonUtil---->article_id---->" + article_id);
				System.out.println("JsonUtil---->title---->" + title);
				System.out.println("JsonUtil---->shop_code---->" + shop_code);
				System.out.println("JsonUtil---->shop_id---->" + shop_id);
				System.out.println("JsonUtil---->link---->" + link);
				System.out.println("JsonUtil---->art_image---->" + art_image);
				System.out.println("JsonUtil---->description---->" + description);
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("article_id", article_id);
				map.put("title", title);
				map.put("content", content);
				map.put("add_time", add_time);
				map.put("link", link);
				map.put("shop_code", shop_code);
				map.put("shop_id", shop_id);
				map.put("art_image", art_image);
				map.put("description", description);
				dataList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			Looper.prepare();
//			progressDialog.dismiss();
//			isVibrate = false;
//			isRunning = false;
			Toast toast = Toast.makeText(context, "商户信息错误或连接超时,请重试！", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			Looper.loop();
		}
		return dataList;
	}
	
	public static Map<String, Object> parseItemJSON(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject jsonItemObject = new JSONObject(jsonObject.getJSONObject("dataInfo").toString());
			String title = jsonItemObject.getString("title");
			String content = jsonItemObject.getString("content");
			String add_time = jsonItemObject.getString("add_time");
			String shop_code = jsonItemObject.getString("shop_code");
			String shop_id = jsonItemObject.getString("shop_id");
			String link = jsonItemObject.getString("link");
			String art_image = jsonItemObject.getString("art_image");
			String description = jsonItemObject.getString("description");
			
			dataItem.put("title", title);
			dataItem.put("content", content);
			dataItem.put("add_time", add_time);
			dataItem.put("link", link);
			dataItem.put("shop_code", shop_code);
			dataItem.put("shop_id", shop_id);
			dataItem.put("art_image", art_image);
			dataItem.put("description", description);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataItem;
	}
	
	public static int getMax(int[] values) {
		int tmp = Integer.MIN_VALUE;
		if (null != values) {
			tmp = values[0];
			for (int i = 0; i < values.length; i++) {
				if (tmp < values[i]) {
					tmp = values[i];
				}
			}
		}
		return tmp;
	}

	public static int getMin(int[] values) {
		int tmp = Integer.MIN_VALUE;
		if (null != values) {
			tmp = values[0];
			for (int i = 0; i < values.length; i++) {
				if (tmp > values[i]) {
					tmp = values[i];
				}
			}
		}
		return tmp;
	}
	
}



