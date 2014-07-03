package com.teentian.util;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactsUtil {

	//Phone ContactsUtil
	public static String exportPhoneContacts(Context context) {
		System.out.println("exportPhoneContacts---->start");
		// 获得所有的联系人
		StringBuffer stringbuffer = new StringBuffer();
		Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);

				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if(disPlayName != null) {
				    Log.d("username", disPlayName); 
				}
				
				stringbuffer.append(disPlayName).append(":");
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							String phoneType = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							Log.i("phoneNumber", phoneNumber);
							Log.i("phoneType", phoneType);
							stringbuffer.append(phoneNumber).append(",");
						} while (phones.moveToNext());
						stringbuffer.replace(stringbuffer.length()-1, stringbuffer.length(), ";");
						stringbuffer.append("\r\n");
					}
				}

				// 获取该联系人邮箱
				Cursor emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				if (emails.moveToFirst()) {
					do {
						// 遍历所有的电话号码
						String emailType = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
						String emailValue = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

						Log.i("emailType", emailType);
						Log.i("emailValue", emailValue);
					} while (emails.moveToNext());
				}

				/*
				// 获取该联系人IM
				Cursor IMs = getContentResolver().query(
						Data.CONTENT_URI,
						new String[] { Data._ID, Im.PROTOCOL, Im.DATA },
						Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Im.CONTENT_ITEM_TYPE + "'",
						new String[] { contactId }, null);
				if (IMs.moveToFirst()) {
					do {
						String protocol = IMs.getString(IMs.getColumnIndex(Im.PROTOCOL));
						String date = IMs.getString(IMs.getColumnIndex(Im.DATA));
						Log.i("protocol", protocol);
						Log.i("date", date);
					} while (IMs.moveToNext());
				}

				// 获取该联系人地址
				Cursor address = getContentResolver()
						.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
								null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				if (address.moveToFirst()) {
					do {
						// 遍历所有的地址
						String street = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
						String city = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
						String region = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
						String postCode = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
						String formatAddress = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
						Log.i("street", street);
						Log.i("city", city);
						Log.i("region", region);
						Log.i("postCode", postCode);
						Log.i("formatAddress", formatAddress);
					} while (address.moveToNext());
				}

				// 获取该联系人组织
				Cursor organizations = getContentResolver().query(
						Data.CONTENT_URI,
						new String[] { Data._ID, Organization.COMPANY,Organization.TITLE },
						Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Organization.CONTENT_ITEM_TYPE + "'",
						new String[] { contactId }, null);
				if (organizations.moveToFirst()) {
					do {
						String company = organizations.getString(organizations.getColumnIndex(Organization.COMPANY));
						String title = organizations.getString(organizations.getColumnIndex(Organization.TITLE));
						Log.i("company", company);
						Log.i("title", title);
					} while (organizations.moveToNext());
				}

				// 获取备注信息
				Cursor notes = getContentResolver().query(
						Data.CONTENT_URI,
						new String[] { Data._ID, Note.NOTE },
						Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Note.CONTENT_ITEM_TYPE + "'",
						new String[] { contactId }, null);
				if (notes.moveToFirst()) {
					do {
						String noteinfo = notes.getString(notes.getColumnIndex(Note.NOTE));
						Log.i("noteinfo", noteinfo);
					} while (notes.moveToNext());
				}

				// 获取nickname信息
				Cursor nicknames = getContentResolver().query(
						Data.CONTENT_URI,
						new String[] { Data._ID, Nickname.NAME },
						Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Nickname.CONTENT_ITEM_TYPE + "'",
						new String[] { contactId }, null);
				if (nicknames.moveToFirst()) {
					do {
						String nickname_ = nicknames.getString(nicknames.getColumnIndex(Nickname.NAME));
						Log.i("nickname_", nickname_);
					} while (nicknames.moveToNext());
				}
				*/
				
			} while (cur.moveToNext());
		}
		System.out.println("stringbuffer---->" + stringbuffer);
//		String string = null;
//		try {
//			string = new String(stringbuffer.toString().getBytes("GBK"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return stringbuffer.toString();
	}

	
	//SIM ContactsUtil
	public static void exportSimContacts(Context context) {
		System.out.println("exportSimContacts---->start");
	}
	
	
}




