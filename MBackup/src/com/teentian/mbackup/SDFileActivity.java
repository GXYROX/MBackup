package com.teentian.mbackup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.teentian.util.IntentBuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class SDFileActivity extends Activity {
	
	private TextView fileDir;
	private ListView fileList;
	private Button buttonBack; 
	private File currentParent;
	private File[] currentFiles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdfile_layout);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.skin_purple));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.actionbar_pruple_bg);
		
		fileDir = (TextView)findViewById(R.id.filedirview);
		fileList = (ListView)findViewById(R.id.filedirlist);
		buttonBack = (Button)findViewById(R.id.filebuttonback);
		File root = new File(Environment.getExternalStorageDirectory().getPath());
		
		if(root.exists()) {
			currentParent = root;
			currentFiles = root.listFiles();
			inflateListView(currentFiles);
		}
		
		buttonBack.setOnClickListener(new ButtonBackClickListener());
		
		fileList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent , View view, int position, long id) {
					if(currentFiles[position].isFile()) {
						IntentBuilder.viewFile(SDFileActivity.this, currentFiles[position].getAbsolutePath());
						return;
					}
					
					File[] tempFile = currentFiles[position].listFiles();
					if(tempFile == null || tempFile.length == 0) {
						Toast.makeText(SDFileActivity.this, getResources().getString(R.string.empty_folder), Toast.LENGTH_LONG).show();
					}
					else {
						currentParent = currentFiles[position];
						currentFiles = tempFile;
						inflateListView(currentFiles);
					}
				
			}
		});
		
	}
	
	class ButtonBackClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				String strArray[] = (currentParent.getCanonicalFile().toString()).split("/");
				int len = strArray.length;
				System.out.println("len---->" + len);
//				if(!currentParent.getCanonicalFile().equals("/")) {
				if(len >= 2) {
						System.out.println("currentParent.getCanonicalFile()----> " + currentParent.getCanonicalFile());
						System.out.println("currentParent.getAbsolutePath()----> " + currentParent.getAbsolutePath());
						System.out.println("currentParent.getPath()----> " + currentParent.getPath());
						System.out.println("currentParent.toString()----> " + currentParent.toString());
						currentParent = currentParent.getParentFile();
						currentFiles = currentParent.listFiles();
						inflateListView(currentFiles);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} //-end of _class ButtonBackListener_ .LJ
	
	/**
	 * @param files
	 */
	private void inflateListView(File[] files) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for(int i=0; i<files.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			if(files[i].isDirectory()) {
				listItem.put("icon", R.drawable.ic_folder);
			}
			else {
				listItem.put("icon", R.drawable.ic_file);
			}
			listItem.put("fileName", files[i].getName());
			listItems.add(listItem);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.sdfile_list_item,
				new String[] {"icon", "fileName"},
				new int[] {R.id.file_item_icon, R.id.file_item_filename});
		fileList.setAdapter(adapter);
		try {
			fileDir.setText(currentParent.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
