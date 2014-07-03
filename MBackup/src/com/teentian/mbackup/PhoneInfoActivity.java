package com.teentian.mbackup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PhoneInfoActivity extends Activity {

	private TextView useTimesText;
	private TextView phoneModelText;
	private TextView imeiText;
	private TextView countryText;
	private TextView phoneNumText;
	private TextView screenSizeText;
	private TextView screenDPIText;
	private TextView sizeText;
	private TextView macText;
	private TextView ipText;
	private TextView operatorsText;
	private TextView sdkText;
	private TextView versionText;
	private TextView manufacturerText;
	private TextView kernelText;
	private TextView osText;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private int count;
	private String kernelVersion;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneinfo_layout);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.skin_light));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.actionbar_light_bg);
		
		sp = getSharedPreferences("name", MODE_PRIVATE);
		editor = sp.edit();
		count = sp.getInt("count", 0);
		editor.putInt("count", ++count);
		editor.commit();
		
		useTimesText = (TextView)findViewById(R.id.phone_info_times_text);
		phoneModelText = (TextView)findViewById(R.id.phone_info_model_text);
		imeiText = (TextView)findViewById(R.id.phone_info_imei_text);
		countryText = (TextView)findViewById(R.id.phone_info_country_text);
		phoneNumText = (TextView)findViewById(R.id.phone_info_num_text);
		screenSizeText = (TextView)findViewById(R.id.phone_info_screen_text);
		screenDPIText = (TextView)findViewById(R.id.phone_info_screen_dpi_text);
		sizeText = (TextView)findViewById(R.id.phone_info_size_text);
		macText = (TextView)findViewById(R.id.phone_info_mac_text);
		ipText = (TextView)findViewById(R.id.phone_info_ip_text);
		operatorsText = (TextView)findViewById(R.id.phone_info_operators_text);
		sdkText = (TextView)findViewById(R.id.phone_info_sdk_text);
		versionText = (TextView)findViewById(R.id.phone_info_version_text);
		manufacturerText = (TextView)findViewById(R.id.phone_info_manufacturer_text);
		osText = (TextView)findViewById(R.id.phone_info_os_text);
		kernelText = (TextView)findViewById(R.id.phone_info_kernel_text);
		
		//******************************************************************************************************
		//获取设备信息
//		Build build = new Build();
		
		String model = Build.MODEL;
		String brand = Build.BRAND;
		String device = Build.DEVICE;
		String display = Build.DISPLAY;
		String manufacturer = Build.MANUFACTURER;
		String cpu = Build.CPU_ABI2;
		String serial = Build.SERIAL;
		String hardware = Build.HARDWARE;
		String sdk = Build.VERSION.SDK;
		String version = Build.VERSION.RELEASE;
		
		System.out.println("model----> " + model);
		System.out.println("brand----> " + brand);
		System.out.println("device----> " + device);
		System.out.println("display----> " + display);
		System.out.println("manufacturer----> " + manufacturer);
		System.out.println("cpu----> " + cpu);
		System.out.println("serial----> " + serial);
		System.out.println("hardware----> " + hardware);
		System.out.println("sdk----> " + sdk);
		System.out.println("version----> " + version);
		
		TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		WifiManager wm = (WifiManager)getSystemService(WIFI_SERVICE);
		
		String phoneNum = tm.getLine1Number();
		String deviceId = tm.getDeviceId();
		String networkCountryIso = tm.getNetworkCountryIso();
		String networkOperator = tm.getNetworkOperator();
		String networkOperatorName = tm.getNetworkOperatorName();
		String simCountryIso = tm.getSimCountryIso();
		String simOperator = tm.getSimOperator();
		String simOperatorName = tm.getSimOperatorName();
		String simSerialNumber = tm.getSimSerialNumber();
		int networkType = tm.getNetworkType();
		int phoneType = tm.getPhoneType();
		int simState = tm.getSimState();
		String subscriberId = tm.getSubscriberId();
		String voiceMailNumber = tm.getVoiceMailNumber();
		String android = tm.getDeviceSoftwareVersion();
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		String wifiR3g = null;
		String mac = null;
		String ipAddress = null;
		float floatSize = 0;
		String size = null;
		
		if (networkInfo != null && networkInfo.isConnected()) //是否连接网络
		{
		//联网方式
		if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
			wifiR3g = "3G";
		else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
			wifiR3g = "wifi";
			
			WifiInfo wifiInfo = wm.getConnectionInfo();
			int ipInt = wifiInfo.getIpAddress();
			ipAddress = intToIp(ipInt);
			mac = wifiInfo.getMacAddress();
		}
		else {
			wifiR3g = "未连接wifi网络";
			mac = "未连接wifi网络";
			ipAddress = "未连接wifi网络";
		}
		
		DisplayMetrics dm = getResources().getDisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int dpi = dm.densityDpi;
		float density = dm.density;
		System.out.println("dpi----> " + dpi);
		System.out.println("density----> " + density);

		double doubleSize = Math.sqrt( ((width*width) + (height*height)) );
		floatSize = (float)(doubleSize/(density*160));
		DecimalFormat format = new DecimalFormat("#0.0"); //java.text包里面的DecimalFormat 类
		size = format.format(floatSize).toString(); 
		System.out.println("size----> " + size);

		Display display2 = getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
		Point size2 = new Point();
		display2.getSize(size2);
		int width2 = size2.x;
		int height2 = size2.y;
		System.out.println("width2----> " + width2);
		System.out.println("height2----> " + height2);
		
		String cpuInfo = getCpuInfo();
		System.out.println("cpuInfo----> " + cpuInfo);
		
		kernelVersion = getKernelVersion();
		
		System.out.println("***************************************");
		System.out.println("phoneNum----> " + phoneNum);
		System.out.println("deviceId----> " + deviceId);
		System.out.println("networkCountryIso----> " + networkCountryIso);
		System.out.println("networkOperator----> " + networkOperator);
		System.out.println("networkOperatorName----> " + networkOperatorName);
		System.out.println("simCountryIso----> " + simCountryIso);
		System.out.println("simOperator----> " + simOperator);
		System.out.println("simOperatorName----> " + simOperatorName);
		System.out.println("simSerialNumber----> " + simSerialNumber);
		System.out.println("networkType----> " + String.valueOf(networkType));
		System.out.println("phoneType----> " + String.valueOf(phoneType));
		System.out.println("simState----> " + String.valueOf(simState));
		System.out.println("subscriberId----> " + subscriberId);
		System.out.println("voiceMailNumber----> " + voiceMailNumber);
		System.out.println("android----> " + android);
		System.out.println("***************************************");
		//******************************************************************************************************
		
		//UI 界面显示
		useTimesText.setText(String.valueOf(count));
		phoneModelText.setText(brand + " " + device + " (" + model + ")");
		imeiText.setText(deviceId);
		countryText.setText(networkCountryIso);
		phoneNumText.setText(phoneNum);
		screenSizeText.setText(String.valueOf(height) + "*" + String.valueOf(width));
		screenDPIText.setText(String.valueOf(dpi));
		sizeText.setText(size);
		macText.setText(mac);
		ipText.setText(ipAddress);
		operatorsText.setText(networkOperatorName);
		sdkText.setText(sdk);
		versionText.setText(version);
		manufacturerText.setText(manufacturer);
		kernelText.setText(kernelVersion);
		osText.setText(display);
		
	}

	private String intToIp(int i) {  
	    return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)  
	            + "." + (i >> 24 & 0xFF);  
	}  

	// 获取手机CPU信息  
	private String getCpuInfo() {  
	    String str1 = "/proc/cpuinfo";  
	    String str2 = "";  
	    String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率  
	    String[] arrayOfString;  
	    try {  
	        FileReader fr = new FileReader(str1);  
	        BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
	        str2 = localBufferedReader.readLine();  
	        arrayOfString = str2.split("\\s+");  
	        for (int i = 2; i < arrayOfString.length; i++) {  
	            cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";  
	        }  
	        str2 = localBufferedReader.readLine();  
	        arrayOfString = str2.split("\\s+");  
	        cpuInfo[1] += arrayOfString[2];  
	        localBufferedReader.close();  
	    } catch (IOException e) {  
	    }  
	    // Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);  
	    return "1-cpu型号:" + cpuInfo[0] + "2-cpu频率:" + cpuInfo[1];  
	}// 和内存信息同理，cpu信息可通过读取/proc/cpuinfo文件来得到，其中第一行为cpu型号，第二行为cpu频率。 
	
	
	//获取Linux内核版本
    public static String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (info != "") {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        
        return kernelVersion;
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(0, R.anim.activity_scroll_to_right);
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
    
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
}



