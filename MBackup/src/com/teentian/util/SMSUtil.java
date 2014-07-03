package com.teentian.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class SMSUtil {

	// 所有的短信
	public static final String SMS_URI_ALL = "content://sms/";
	// 收件箱短信
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	// 发件箱短信
	public static final String SMS_URI_SEND = "content://sms/sent";
	// 草稿箱短信
	public static final String SMS_URI_DRAFT = "content://sms/draft";

	/*
    _id：短信序号，如100 　　
 　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的 　　
 　　address：发件人地址，即手机号，如+8613811810000 　　
 　　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null 　　
 　　date：日期，long型，如1256539465022，可以对日期显示格式进行设置 　　
 　　protocol：协议 0 SMS_RPOTO 短信，1 MMS_PROTO 彩信 　　 　　
 	read：是否阅读 0 未读，1 已读 　　
　　 status：短信状态 -1 接收，0 complete, 64 pending, 128 failed 　　
　　 type：短信类型1是接收到的，2是已发出 　　 　　
	body：短信具体内容 　　
 　　service_center：短信服务中心号码编号，如+8613800755500
	*/
	
    private static Activity activity;
    private static Uri uri;  
    private static List<SMSInfo> smsInfo;  
    
    public SMSUtil(Activity activity, Uri uri) {  
    	smsInfo = new ArrayList<SMSInfo>();  
        this.activity = activity;  
        this.uri = uri;  
    } 
    
    public static List<SMSInfo> getSMSInfo() {  
        String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };  
        Cursor cusor = activity.managedQuery(uri, projection, null, null, "date desc");  
        int personColumn = cusor.getColumnIndex("person");  
        int phoneNumberColumn = cusor.getColumnIndex("address");  
        int smsbodyColumn = cusor.getColumnIndex("body");  
        int dateColumn = cusor.getColumnIndex("date");  
        int typeColumn = cusor.getColumnIndex("type");  
        if (cusor != null) {  
            while (cusor.moveToNext()) {  
            	SMSInfo info = new SMSInfo();  
            	info.setPerson(cusor.getString(personColumn));  
            	info.setPhoneNumber(cusor.getString(phoneNumberColumn));  
            	info.setSmsbody(cusor.getString(smsbodyColumn));  
            	info.setDate(cusor.getString(dateColumn));  
            	info.setType(cusor.getString(typeColumn));  
            	smsInfo.add(info);  
            }  
            cusor.close();  
        }  
        return smsInfo;  
    }  
    
    public static String getSMSInfo(Context context) {  
        StringBuffer stringbuffer = new StringBuffer();
    	String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
    	Uri uri = Uri.parse(SMS_URI_INBOX);
    	Cursor cusor = context.getContentResolver().query(uri, projection, null, null, null);
    	int personColumn = cusor.getColumnIndex("person");  
    	int phoneNumberColumn = cusor.getColumnIndex("address");  
    	int smsBodyColumn = cusor.getColumnIndex("body");  
    	int dateColumn = cusor.getColumnIndex("date");  
    	if (cusor != null) {  
    		while (cusor.moveToNext()) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault()) ; 
//				String smsName = cusor.getString(personColumn);
				String smsPhoneNumber = cusor.getString(phoneNumberColumn);  
    			String smsName = getPeopleNameFromPerson(context, cusor.getString(phoneNumberColumn));
    			String smsBody = cusor.getString(smsBodyColumn);  
    			String smsDate = dateformat.format(Long.parseLong(cusor.getString(dateColumn)));
    			stringbuffer.append(smsName).append(" : ").append(smsPhoneNumber).append("\r\n");
    			stringbuffer.append(smsDate).append(" : ").append(smsBody).append("\r\n").append("\r\n");
    		}  
    		cusor.close();  
    	}  
    	System.out.println("stringbuffer---->" + stringbuffer);
    	return stringbuffer.toString();  
    }  
	
    //sample demo
    // Uri uri = Uri.parse(SMSInfo.SMS_URI_INBOX);  
    // SMSUtil smsUtil = new SMSUtil(this, uri);  
    // infos = smsUtil.getSMSInfo();  
    
    // 通过address手机号关联Contacts联系人的显示名字  
    public static String getPeopleNameFromPerson(Context context, String address){  
        if(address == null || address == "") {  
            return "NULL";  
        }  
          
        String strPerson = "NULL";  
        String[] projection = new String[] {Phone.DISPLAY_NAME, Phone.NUMBER};  
         
        // address 手机号过滤
        Uri uri_Person = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, address);    
//        Uri uri_Person = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);    
        Cursor cursor = context.getContentResolver().query(uri_Person, projection, null, null, null);  
          
        if(cursor.moveToFirst()){  
            int index_PeopleName = cursor.getColumnIndex(Phone.DISPLAY_NAME);  
//            int index_PeopleName = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);  
            String strPeopleName = cursor.getString(index_PeopleName);  
            strPerson = strPeopleName;  
        }  
        cursor.close();  
        return strPerson;  
    } 
    
}



