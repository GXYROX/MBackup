package com.teentian.util;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

public class APPsUtil {

    public static boolean detectPackage(Context context, String packageName) {
    	PackageInfo packageInfo;
    	try { 
    		packageInfo = context.getPackageManager().getPackageInfo(packageName, 0); 
    	} catch (NameNotFoundException e) { 
    		packageInfo = null; 
    		e.printStackTrace(); 
    	} 
    	
    	if(packageInfo == null){ 
//    		System.out.println("not installed"); 
    		return false;
    	}
    	else{ 
//    		System.out.println("is installed"); 
    		return true;
    	}
    }
    
    public static void openAPP(Context context, String packageName) {
    	PackageInfo packageInfo = null;
    	PackageManager pm = context.getPackageManager();
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
    	resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	resolveIntent.setPackage(packageInfo.packageName);

    	List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

    	ResolveInfo resolveInfo = apps.iterator().next();
    	if (resolveInfo != null ) {
	    	String mPackageName = resolveInfo.activityInfo.packageName;
	    	String mClassName = resolveInfo.activityInfo.name;
	
	    	Intent intent = new Intent(Intent.ACTION_MAIN);
	    	intent.addCategory(Intent.CATEGORY_LAUNCHER);
	    	ComponentName componentName = new ComponentName(mPackageName, mClassName);
	    	intent.setComponent(componentName);
	    	context.startActivity(intent);
    	}
    }
	
}



