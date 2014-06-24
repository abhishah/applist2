package com.example.applist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnItemClickListener {
	PackageManager packagemanager;
	ListView apkList;
	List<PackageInfo> packageList;
	List<PackageInfo> packageList1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		packagemanager = getPackageManager();
		packageList = packagemanager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		packageList1 = new ArrayList<PackageInfo>();
		for (PackageInfo pi : packageList) {
			boolean b = isSystemPackage(pi);
			if (!b) {
				packageList1.add(pi);
			}
		}
		apkList = (ListView) findViewById(R.id.applist);
		apkList.setAdapter(new ApkAdapter(this, packageList1, packagemanager));
		apkList.setOnItemClickListener(this);
		//registerForContextMenu(apkList);
	}

	private boolean isSystemPackage(PackageInfo pi) {
		// TODO Auto-generated method stub
		return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
				: false;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		PackageInfo packageinfo = (PackageInfo) arg0.getItemAtPosition(arg2);
		AppData appdata = (AppData) getApplicationContext();
		appdata.setPackageInfo(packageinfo);

		Intent appInfo = new Intent(getApplicationContext(), ApkInfo.class);
		startActivity(appInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.apk, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			break;
		case R.id.genapk:
			String a = getExtractPath();
			Toast.makeText(getBaseContext(), a, Toast.LENGTH_LONG).show();
			break;
		case R.id.genfile:
			WriteData wd = new WriteData();
			wd.makeFile(packageList1);
			Toast.makeText(getBaseContext(), "File stored in external memory ",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.exit1:
			finish();
		}

		return super.onMenuItemSelected(featureId, item);
	}

	/*
	 * public void makeFile(List<PackageInfo> packageList1) { // TODO
	 * Auto-generated method stubList<PackageInfo> packageList; String state;
	 * StringBuilder data=new StringBuilder(""); packageList = packageList1;
	 * state = Environment.getExternalStorageState(); if
	 * (state.equals(Environment.MEDIA_MOUNTED)) { PackageInfo info; String
	 * line=""; String l="\n"; for(int i=0;i<packageList.size();i++){
	 * info=packageList.get(i); line=(String)
	 * (getPackageManager().getApplicationLabel( info.applicationInfo));
	 * data.append(line+l); } try { File myFile = new File(
	 * Environment.getExternalStorageDirectory().getPath(),"apps.txt");
	 * myFile.createNewFile(); FileOutputStream fOut = new
	 * FileOutputStream(myFile); OutputStreamWriter myOutWriter = new
	 * OutputStreamWriter(fOut); myOutWriter.append(data.toString());
	 * myOutWriter.close(); fOut.close(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * } }
	 */

	private String getExtractPath() {
		// TODO Auto-generated method stub
		return PreferenceManager.getDefaultSharedPreferences(this).getString(
				"extract_path",
				new File(Environment.getExternalStorageDirectory(),
						"ApkExtractor").getAbsolutePath());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "view app info");
		menu.add(0, 2, 2, "delete app");
		menu.add(0, 3, 3, "open app");

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo localAdapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		ResolveInfo localResolveInfo = (ResolveInfo) getListAdapter().getItem(
				localAdapterContextMenuInfo.position);
		switch (item.getItemId()) {
		case 1:
			showInstalledAppDetails(localResolveInfo);
			break;
		case 2:
			uninstallPackage(localResolveInfo);
			break;
		case 3:
			openApp(localResolveInfo);
			break;
		}
		return super.onContextItemSelected(item);

	}

	private void openApp(ResolveInfo localResolveInfo) {
		// TODO Auto-generated method stub
		String pack = localResolveInfo.activityInfo.packageName;
		Intent local = new Intent("android.intent.action.MAIN");
		local.setClassName(localResolveInfo.activityInfo.packageName, pack);
		startActivity(local);
	}

	private void uninstallPackage(ResolveInfo paramResolveInfo) {
		Uri localUri = Uri.fromParts("package",
				paramResolveInfo.activityInfo.packageName, null);
		Intent localIntent = new Intent("android.intent.action.DELETE");
		localIntent.setData(localUri);
		startActivity(localIntent);
	}

	@SuppressWarnings("unused")
	private void showInstalledAppDetails(ResolveInfo paramResolveInfo) {
		String str1 = paramResolveInfo.activityInfo.packageName;
		Intent localIntent = new Intent();
		int i = Build.VERSION.SDK_INT;
		if (i >= 9) {
			localIntent
					.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			localIntent.setData(Uri.fromParts("package", str1, null));
			startActivity(localIntent);
			return;
		}
		if (i == 8) {
		}
		for (String str2 = "pkg";; str2 = "com.android.settings.ApplicationPkgName") {
			localIntent.setAction("android.intent.action.VIEW");
			localIntent.setClassName("com.android.settings",
					"com.android.settings.InstalledAppDetails");
			localIntent.putExtra(str2, str1);
			break;
		}
	}
}
