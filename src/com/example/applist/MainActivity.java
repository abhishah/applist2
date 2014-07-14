package com.example.applist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
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
		apkList = (ListView) findViewById(android.R.id.list);
		apkList.setAdapter(new ApkAdapter(this, packageList1, packagemanager));
		apkList.setOnItemClickListener(this);
		registerForContextMenu(apkList);
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
		case R.id.genapk:
			new Gen().execute("data");
			break;
		case R.id.action_settings:
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
public class Gen extends AsyncTask<String,Integer,String>{
	/*private void genapk() {
		// TODO Auto-generated method stub
		final Intent main=new Intent(Intent.ACTION_MAIN,null);
		main.addCategory(Intent.CATEGORY_LAUNCHER);
		final List packagelist=getPackageManager().queryIntentActivities(main, 0);
		//int z=0;
		for(Object object:packagelist){
			ResolveInfo rs=(ResolveInfo)object;
			File f1=new File(rs.activityInfo.applicationInfo.publicSourceDir);
			Log.v("file--", " "+f1.getName().toString()+"----"+rs.loadLabel(getPackageManager()));
			try{
				String filename=rs.loadLabel(getPackageManager()).toString();
				Log.d("file_name--",""+filename);
				File f2;
				String info=Environment.getExternalStorageState();
                 if(info.equals(Environment.MEDIA_MOUNTED)){
				 f2=new File(Environment.getExternalStorageDirectory().toString()+"/MyApps");}
				else{ f2=getCacheDir();}
				if(!f2.exists())f2.mkdirs();
				f2=new File(f2.getPath()+"/"+filename+".apk");
				f2.createNewFile();
				InputStream in=new FileInputStream(f1);
				OutputStream out=new FileOutputStream(f2);
				byte[]bf=new byte[1024];
				int len;
				while((len=in.read(bf))>0){
					out.write(bf,0,len);
				}
				in.close();
				out.close();
				System.out.println("File Copied");
				
			} catch(FileNotFoundException ex){
                System.out.println(ex.getMessage() + " in the specified directory.");
            }
            catch(IOException e){
                System.out.println(e.getMessage());      
            }
		}
	}*/
	ProgressDialog dialog;
	
	protected void onPreExecute() {
		// example of setting up something
		dialog = new ProgressDialog(MainActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMax(100);
		dialog.show();

	
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
	       
		
			// TODO Auto-generated method stub
			final Intent main=new Intent(Intent.ACTION_MAIN,null);
			main.addCategory(Intent.CATEGORY_LAUNCHER);
			final List packagelist=getPackageManager().queryIntentActivities(main, 0);
			int z=packageList.size();
			double b=100/z;
				for(final Object object:packagelist){
				publishProgress((int)b);
				Thread app=new Thread(new Runnable(){
					public void run() {
						ResolveInfo rs=(ResolveInfo)object;
				if((rs.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
				File f1=new File(rs.activityInfo.applicationInfo.publicSourceDir);
				Log.v("file--", " "+f1.getName().toString()+"----"+rs.loadLabel(getPackageManager()));
				try{
					String filename=rs.loadLabel(getPackageManager()).toString();
					Log.d("file_name--",""+filename);
					File f2;
					String info=Environment.getExternalStorageState();
	                 if(info.equals(Environment.MEDIA_MOUNTED)){
					 f2=new File(Environment.getExternalStorageDirectory().toString()+"/MyApps");}
					else{ f2=getCacheDir();}
					if(!f2.exists())f2.mkdirs();
					f2=new File(f2.getPath()+"/"+filename+".apk");
					f2.createNewFile();
					InputStream in=new FileInputStream(f1);
					OutputStream out=new FileOutputStream(f2);
					byte[]bf=new byte[1024];
					int len;
					while((len=in.read(bf))>0){
						out.write(bf,0,len);
					}
					in.close();
					out.close();
					System.out.println("File Copied");
					
				} catch(FileNotFoundException ex){
	                System.out.println(ex.getMessage() + " in the specified directory.");
	            }
	            catch(IOException e){
	                System.out.println(e.getMessage());      
	            }
			}
			
		};
		});
		app.start();
		}
	return null;
	}
	protected void onProgressUpdate(Integer... progress) {
		dialog.incrementProgressBy(progress[0]);
	}
	protected void onPostExecute(String result) {
		Toast.makeText(getApplicationContext(), "Back up of all application is made", Toast.LENGTH_LONG).show();
	}
	
}
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
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo localAdapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		ResolveInfo localResolveInfo = (ResolveInfo) getListAdapter().getItem(
				localAdapterContextMenuInfo.position);
		switch (item.getItemId()) {
		case R.id.appinfo:
			showInstalledAppDetails(localResolveInfo);
			break;
		case R.id.uninstall:
			uninstallPackage(localResolveInfo);
			break;
		case R.id.open:
			openApp(localResolveInfo);
			break;
		}
		return super.onContextItemSelected(item);

	}

	private void openApp(ResolveInfo localResolveInfo) {
		// TODO Auto-generated method stub
		String pack = localResolveInfo.activityInfo.name;
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
