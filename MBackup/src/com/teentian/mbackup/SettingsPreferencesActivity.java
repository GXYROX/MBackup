package com.teentian.mbackup;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackPreferenceActivity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chiralcode.colorpicker.ColorPicker;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Ljrfox
 * @date 2014骞�4鏈�18鏃� 涓嬪崍5:06:55
 */
public class SettingsPreferencesActivity extends SwipeBackPreferenceActivity
		implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {

	private static final String SETTINGS_ABOUT = "pref_key_setting_about";
	private static final String SETTINGS_THEME = "pref_key_setting_theme";
	public static SharedPreferences sp;
	public static SharedPreferences.Editor editor;
	private SwipeBackLayout mSwipeBackLayout;
	private Preference mAboutPreference, mThemePreference;
	private ColorPicker mColorPicker;
	private ActionBar actionBar;
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		addPreferencesFromResource(R.xml.preferences);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.skin_purple));

		sp = getSharedPreferences("COLOR", Context.MODE_PRIVATE);
		editor = sp.edit();
		
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		title = (TextView) findViewById(titleId);
		
        int mColor = sp.getInt("color", 0xFFFFFFFF);
        title.setTextColor(mColor); 
		Drawable colorDrawable = new ColorDrawable(mColor); 
		actionBar.setBackgroundDrawable(colorDrawable);
         
//		mColorPicker = (ColorPicker) findViewById(R.id.color_picker);
//		applySelectedColor();
		
		this.mSwipeBackLayout = getSwipeBackLayout();
		this.mSwipeBackLayout.setEdgeTrackingEnabled(1);

		mAboutPreference = (Preference) findPreference(SETTINGS_ABOUT);
		mThemePreference = (Preference) findPreference(SETTINGS_THEME);
		mAboutPreference.setOnPreferenceClickListener(this);
		mThemePreference.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals(SETTINGS_ABOUT)) {
			aboutDialog();
		}
		if (preference.getKey().equals(SETTINGS_THEME)) {
			themeDialog();
		}
		return true;
	}

	public void aboutDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View aboutDialogView = inflater.inflate(R.layout.about_dialog_layout, (ViewGroup) findViewById(R.id.dialog_about));
		Builder dialog = new AlertDialog.Builder(SettingsPreferencesActivity.this);
		dialog.setTitle(R.string.app_name);
		dialog.setView(aboutDialogView);
		dialog.setPositiveButton(getResources().getString(R.string.confirm), null);
		dialog.show();
	}

	public void themeDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View themeDialogView = inflater.inflate(R.layout.theme_dialog_layout, (ViewGroup) findViewById(R.id.dialog_theme));
		mColorPicker = (ColorPicker)themeDialogView.findViewById(R.id.color_picker);
		Builder dialog = new AlertDialog.Builder(SettingsPreferencesActivity.this);
//		dialog.setTitle(R.string.app_name);
		dialog.setCancelable(false);
		dialog.setView(themeDialogView);
		dialog.setPositiveButton(getResources().getString(R.string.action_settings), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				applySelectedColor();
			}
		});
		dialog.setNegativeButton(getResources().getString(R.string.cancel), null);
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.menu_setting, menu);
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String key) {
		// TODO Auto-generated method stub
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

	private void applySelectedColor() {
		int selected = mColorPicker.getColor();
		int color = Color.argb(153, Color.red(selected), Color.green(selected), Color.blue(selected));
		
		Drawable colorDrawable = new ColorDrawable(color); 
		actionBar.setBackgroundDrawable(colorDrawable);
		title.setTextColor(color);
		
		editor.putInt("color", color);
		editor.commit();
	}
	
}
