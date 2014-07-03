package com.teentian.mbackup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * @author Log in:Ljrfgeorry 
 * @code Artisan:Tornado.LJ
 * @date 2013-7-28
 * @time ����2:37:05
 * @version V1.0.0(T9999)
 */
public class PackageInfoActivity extends Activity {  
	

	private ListView lv;  
    private MyAdapter adapter;  
    ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();  
    private static final String BOOT_START_PERMISSION = "android.permission.RECEIVE_BOOT_COMPLETED";  
 
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.packgeinfo_layout);  
          
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.skin_meizublue));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.actionbar_meizu_bg);
		
        lv = (ListView)findViewById(R.id.lv);  
        
     	PackageManager pm = getPackageManager();  
//     	PackageInfo packageInfo = null;
//		try {
//			packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
      	List<ApplicationInfo> listApps = pm.getInstalledApplications(0); 
      	Collections.sort(listApps, new ApplicationInfo.DisplayNameComparator(pm));
      	Iterator<ApplicationInfo> appInfoIterator = listApps.iterator();  
      	List<Map<String, Object>> appList = new ArrayList<Map<String, Object>>(listApps.size());
      	while (appInfoIterator.hasNext()) {
      		ApplicationInfo app = appInfoIterator.next();  
      		int flag = pm.checkPermission(BOOT_START_PERMISSION, app.packageName);  
//      		if (flag == PackageManager.PERMISSION_GRANTED) {  
      		if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {  
      			Map<String, Object> appMap = new HashMap<String, Object>();  
      			String appName = pm.getApplicationLabel(app).toString();  
      			Drawable icon = pm.getApplicationIcon(app); 
//      			String appVersion = packageInfo.versionName;
      			String packageName = app.packageName;  
      			appMap.put("appName", appName);  
      			appMap.put("icon", icon);  
      			appMap.put("packageName", packageName);  
//      			appMap.put("appVersion", "  V" + appVersion);
      			
      			appList.add(appMap);  
      			}  
      	}  
        adapter = new MyAdapter(this, appList, R.layout.packgeinfo_list_item,   
                new String[]{"icon", "appName", "packageName"},  
                new int[]{R.id.icon, R.id.appName, R.id.packageName});  
        
        lv.setAdapter(adapter);  
      	
      	
//        PackageManager pm = getPackageManager();  
//        //List<ApplicationInfo> packs = pm.getInstalledApplications(0);  
//        List<PackageInfo> packs = pm.getInstalledPackages(0);  
//          
//        for(PackageInfo pi:packs){  
//            HashMap<String, Object> map = new HashMap<String, Object>();  
////          if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0&&  
////                  (pi.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0){  
////              map.put("icon", pi.applicationInfo.loadIcon(pm));
////              map.put("appName", pi.applicationInfo.loadLabel(pm));
////              map.put("packageName", pi.applicationInfo.packageName);
////              
////              //ѭ����ȡ���浽HashMap�У������ӵ�ArrayList�ϣ�һ��HashMap����һ��  
////              items.add(map);  
////          }  
//            
//            map.put("icon", pi.applicationInfo.loadIcon(pm));//ͼ��  
//            map.put("appName", pi.applicationInfo.loadLabel(pm));//Ӧ�ó������  
//            map.put("packageName", pi.applicationInfo.packageName);//Ӧ�ó������
//            
//            //ѭ����ȡ���浽HashMap�У������ӵ�ArrayList�ϣ�һ��HashMap����һ��  
//            items.add(map);  
//        }  
//        
//        adapter = new MyAdapter(this, items, R.layout.list_item,   
//                new String[]{"icon", "appName", "packageName"},  
//                new int[]{R.id.icon, R.id.appName, R.id.packageName});  
//        
//        lv.setAdapter(adapter);  
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
  
class MyAdapter extends SimpleAdapter  {  
    private int[] appTo;  
    private String[] appFrom;  
    private ViewBinder appViewBinder;  
    private List<? extends Map<String, ?>>  appData;  
    private int appResource;  
    private LayoutInflater appInflater;  
      
    /**
     * ������
     * @param context
     * @param data
     * @param resource
     * @param from
     * @param to
     */
    public MyAdapter(Context context, List<? extends Map<String, ?>> data,  
            int resource, String[] from, int[] to) {  
        super(context, data, resource, from, to);  
        appData = data;  
        appResource = resource;  
        appFrom = from;  
        appTo = to;  
        appInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
      
    public View getView(int position, View convertView, ViewGroup parent){  
        return createViewFromResource(position, convertView, parent, appResource);  
    }  
      
    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource){  
        View v;  
        
        if(convertView == null){  
            v = appInflater.inflate(resource, parent,false);  
            final int[] to = appTo;  
            final int count = to.length;  
            final View[] holder = new View[count];  
              
            for(int i = 0; i < count; i++){  
                holder[i] = v.findViewById(to[i]);  
            }  
            
            v.setTag(holder);  
        }else {  
            v = convertView;  
        }  
        
        bindView(position, v);  
        return v;     
    }  
      
    private void bindView(int position, View view) {  
        final Map dataSet = appData.get(position);  
        
        if(dataSet == null){  
            return;  
        }  
          
        final ViewBinder binder = appViewBinder;  
        final View[] holder = (View[])view.getTag();  
        final String[] from = appFrom;  
        final int[] to = appTo;  
        final int count = to.length;  
          
        for(int i = 0; i < count; i++){  
            final View v = holder[i];  
            
            if(v != null){  
                final Object data = dataSet.get(from[i]);  
                String text = data == null ? "":data.toString();  
                
                if(text == null){  
                    text = "";  
                }  
                  
                boolean bound = false; 
                
                if(binder != null){  
                    bound = binder.setViewValue(v, data, text);  
                }  
                  
                if(!bound){  
                    /** 
                     * �Զ����������������������ݴ��ݹ����Ŀؼ��Լ�ֵ��������ͣ� 
                     * ִ����Ӧ�ķ��������Ը���Լ���Ҫ�������if��䡣���⣬CheckBox�� 
                     * ������TextView�Ŀؼ�Ҳ�ᱻʶ���TextView�������Ҫ�ж�ֵ������ 
                     */  
                    if(v instanceof TextView){  
                        //�����TextView�ؼ��������SimpleAdapter�Դ�ķ����������ı�  
                        setViewText((TextView)v, text);  
                    }else if(v instanceof ImageView){  
                        //�����ImageView�ؼ��������Լ�д�ķ���������ͼƬ  
                        setViewImage((ImageView)v, (Drawable)data);  
                    }else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +  
                                "view that can be bounds by this SimpleAdapter");  
                    }  
                }  
            }  
        }  
    }  
    
    public void setViewImage(ImageView v, Drawable value) {  
        v.setImageDrawable(value);  
    }  
    
}  


