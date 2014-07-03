package com.teentian.mbackup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.teentian.util.ContactsUtil;
import com.teentian.util.FileUtil;
import com.teentian.util.SMSUtil;


/**
 * @author Ljrfox
 */
public class MainActivity extends SwipeBackActivity {

	private static final String TAG = "MainActivity";
	public static final int SENSOR_SHAKE = 10;
	public static final String defaultADsID = "000000";
	
	private SwipeBackLayout mSwipeBackLayout;
	public static Window window;
	public static boolean isWindowFoucs = false;
	public static boolean isVibrate = false;
	public static boolean isOnlyPopupWinodw = false;
	public static Intent vibrateAppPopupIntent;
	public static ProgressDialog progressDialog;  
	public static ProgressDialog progressExportDialog;
	private String oneFilePath;
	private boolean isRunning = false;
	private List<HashMap<String, Object>> datalist = new ArrayList<HashMap<String, Object>>();
	private Map<String, Object> dataitem = new HashMap<String, Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		// Create File Path
//		File saveFileDir = FileUtil.saveSDCardFolder("Video");
//		if (!saveFileDir.exists()) {
//			saveFileDir.mkdirs();
//		}
		// Create File
		// String file = filePath.getAbsolutePath() + File.separator + fileName;
		// File f = new File(file);
		// if(!f.exists()) {
		// f.createNewFile();
		// }
		
		//default skin
		getActionBar().setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.skin_meizublue));
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(true);
//		}
//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setNavigationBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.color.statusbar_blue_bg);
		
		this.mSwipeBackLayout = getSwipeBackLayout();
		this.mSwipeBackLayout.setEdgeTrackingEnabled(1);
		
		//in Activity's onCreate() for instance
		window = MainActivity.this.getWindow(); 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
		 case R.id.action_file:
			 Intent settingsFileInfo = new Intent(MainActivity.this, SDFileActivity.class);
			 startActivity(settingsFileInfo);
			 return true;
		 case R.id.action_export_contact:
			 Builder builder = new AlertDialog.Builder(MainActivity.this);
			 builder.setIcon(R.drawable.ic_alert_export);
			 builder.setTitle(getResources().getString(R.string.contacts));
			 builder.setMessage(getResources().getString(R.string.contacts_info));
			 // Cancel
			 builder.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int which) {

				 }
			 });
			 // Setting
			 builder.setPositiveButton(getResources().getString(R.string.export), new OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int which) {
					 isExporting();
				 }
			 });
			 builder.create();
			 builder.show();
			 return true;
		 case R.id.action_export_sms:
			 Builder builderSMS = new AlertDialog.Builder(MainActivity.this);
			 builderSMS.setIcon(R.drawable.ic_alert_sms);
			 builderSMS.setTitle(getResources().getString(R.string.sms));
			 builderSMS.setMessage(getResources().getString(R.string.sms_info));
			 // Cancel
			 builderSMS.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int which) {
					 
				 }
			 });
			 // Setting
			 builderSMS.setPositiveButton(getResources().getString(R.string.export), new OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int which) {
					 isExportingSMS();
				 }
			 });
			 builderSMS.create();
			 builderSMS.show();
			 return true;
		 case R.id.action_package_info:
			 Intent settingsPackageInfo = new Intent(MainActivity.this, PackageInfoActivity.class);
			 startActivity(settingsPackageInfo);
			 return true;
		 case R.id.action_phone_info:
			 Intent settingsPhoneInfo = new Intent(MainActivity.this, PhoneInfoActivity.class);
			 startActivity(settingsPhoneInfo);
			 return true;
		 case R.id.action_settings:
			 Intent settingsIntent = new Intent(MainActivity.this, SettingsPreferencesActivity.class);
			 startActivity(settingsIntent);
			 return true;
		 default:
			 return super.onOptionsItemSelected(item);
		 }
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(0, R.anim.activity_scroll_to_right);
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public void isExporting() {
		progressExportDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.contacts), 
				getResources().getString(R.string.exporting));
		new Thread(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()) ; 
				String datetime = dateformat.format(new Date()) ; // 灏嗗綋鍓嶆棩鏈熻繘琛屾牸寮忓寲鎿嶄綔 
				
				String strFile = ContactsUtil.exportPhoneContacts(MainActivity.this);
				System.out.println("strFile---->" + strFile);
//				byte[] fileByte = Base64.decode(strFile, Base64.DEFAULT);
				byte[] fileByte = null;
				try {
					fileByte = strFile.getBytes("GBK");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String fileName = datetime + "-phone_info.txt";
				oneFilePath = FileUtil.getSDCardFile("Info", fileName);
				try {
					FileUtil.saveFile2SD(MainActivity.this, fileByte, "CONTACT", fileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				exportingHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	public void isExportingSMS() {
		progressExportDialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.sms), 
				getResources().getString(R.string.exporting));
		new Thread(new Runnable() {
			@Override
			public void run() {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()) ; 
				String datetime = dateformat.format(new Date()) ;
				
				String strFile = SMSUtil.getSMSInfo(MainActivity.this);
				System.out.println("strFile---->" + strFile);
//				byte[] fileByte = Base64.decode(strFile, Base64.DEFAULT);
				byte[] fileByte = null;
				try {
					fileByte = strFile.getBytes("GBK");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String fileName = datetime + "-all_sms.txt";
				oneFilePath = FileUtil.getSDCardFile("SMS", fileName);
				try {
					FileUtil.saveFile2SD(MainActivity.this, fileByte, "SMS", fileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				exportingHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	
	Handler exportingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			progressExportDialog.dismiss();
			Toast.makeText(MainActivity.this, getResources().getString(R.string.export_info) + oneFilePath, Toast.LENGTH_LONG).show();
		}
	};
	

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus) {
			isWindowFoucs = true;
		}
		else {
			isWindowFoucs = false;
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
//	@TargetApi(19) 
//	private void setTranslucentStatus(boolean on) {
//		Window win = getWindow();
//		WindowManager.LayoutParams winParams = win.getAttributes();
//		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//		if (on) {
//			winParams.flags |= bits;
//		} else {
//			winParams.flags &= ~bits;
//		}
//		win.setAttributes(winParams);
//	}
	
}




