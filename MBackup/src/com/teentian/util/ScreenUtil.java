package com.teentian.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import com.teentian.mbackup.MainActivity;


public class ScreenUtil {

	
	public static void screenWakeUpAndUnlock(Context context){  
        KeyguardManager km= (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);  
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");  
        //解锁  
        kl.disableKeyguard();  
        //获取电源管理器对象  
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);  
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK , "bright");  
        
        //WindowManager
        MainActivity.window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //点亮屏幕  
        wl.acquire();  
        //释放  
        wl.release();  
    }  	
	
	
}




