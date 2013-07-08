package com.hts.assign;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class Assign extends ListActivity 
{
	public static final String TAG = "HTS";
	public static int inobjSearchidslength = 1;
	public static String strDbRes = "";
	public static String strPrefServerRes = "";
	public static String strPrefPortRes = "";
	public static String strPrefUnameRes = "";
	public static String strPrefPwdRes = "";
	public int inCount = 0;
	public Object[] objSearchIds = null;
	boolean isDbFalse = false;
	boolean isDtlsFalse = false;
	boolean isEditText = false;
	boolean isLoadingMore = false;
	boolean isFull = false;
	boolean isLoginFalse = false;
	boolean isSearchIds = false;
	Boolean isInternetPresent = false;
	int inCountValues;
	int inUId;
	int inLimitSrchAsync = 0;
	int inPrefPort = 8069;
	ArrayAdapter<String> arrAdptr;
	ArrayList<String> strlstResult = new ArrayList<String>();
	ArrayList<String> strlstResult1 = new ArrayList<String>();
	ArrayList<String> strLstFullDetails = new ArrayList<String>();
	ArrayList<String> strLstFullDetails1 = new ArrayList<String>();
	ArrayList<String> strLstDetails = new ArrayList<String>();
	ArrayList<String> strLstDBContains = new ArrayList<String>();
	Button btnRefresh;
	Button btnSttgs;
	EditText etxtType;
	LinearLayout lnrlLoad;
	SharedPreferences shpref;
	SharedPreferences shPrefSave;
	final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences settings;
	String strIdValue;
	String strLstFull;
	String strValue = "%a%";
	String strLstAddress;
	String strLstCity;
	String strLstCreditLmt;
	String strLstName;
	String strLstPhone;
	String strlstvArray[];
	String strLstDispRes;
	String strPrefServer = "test.claudion.com";
	String strPrefPort = "8069";
	String strPrefDb = "testdb";
	String strPrefUname = "admin";
	String strPrefPwd = "ser";
	String strKeypost;
	String strValuepost;
	String strstrPrefDb = "testdb";
	String strstrPrefUname = "admin";
	String strstrPrefPwd = "ser";
	View vwFooter;
	ConnectionDetector DtectConnctn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.i(TAG, "Assign :: onCreate()");
		super.onCreate(savedInstanceState);
		DtectConnctn = new ConnectionDetector(getApplicationContext());
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnSttgs = (Button) findViewById(R.id.btnSttgs);
		etxtType = (EditText) findViewById(R.id.etxtType);
		lnrlLoad = (LinearLayout) findViewById(R.id.idLoad);
		lnrlLoad.setVisibility(View.INVISIBLE);
		int inI1 = 1;
		settings = getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean("my_first_time", true)) {
			settings.edit().putBoolean("my_first_time", false).commit();
			SharedPreferences SettingsAll = getSharedPreferences("PrefSpeed",
					MODE_PRIVATE);
			SharedPreferences.Editor editorall = SettingsAll.edit();
			editorall.clear();
			editorall.commit();
			shpref = PreferenceManager.getDefaultSharedPreferences(this);
			Editor editor = shpref.edit();
			editor.clear();
			editor.commit();
		}
		SharedPreferences shPrefSave = getSharedPreferences("PrefSpeed",
				MODE_PRIVATE);
		if ((shPrefSave != null) && (!shPrefSave.equals(""))) 
		{
			strlstResult1.clear();
			strLstFullDetails1.clear();
			lnrlLoad.setVisibility(View.INVISIBLE);
			Map<String, ?> prefsMap = shPrefSave.getAll();
			for (@SuppressWarnings("unused")
			Map.Entry<String, ?> entry : prefsMap.entrySet()) 
			{
				String strRemainingvalue = shPrefSave.getString("post" + inI1,
						"");
				Log.d("Remains", strRemainingvalue);
				String strDelimiter = "partner_split";
				String[] strMainValue = strRemainingvalue.split(strDelimiter);
				String strNamePh = strMainValue[0];
				String strDetails = strMainValue[1];
				strstrPrefDb = strMainValue[2];
				strstrPrefUname = strMainValue[3];
				strstrPrefPwd = strMainValue[4];
				String strCountValues = "0";
				strCountValues = strMainValue[5];
				if (!strCountValues.equals("0") || !strCountValues.equals("")) 
				{
					inCountValues = Integer.parseInt(strCountValues);
				}
				strlstResult1.add(strNamePh);
				strLstFullDetails1.add(strDetails);
				strLstDetails.add(strDetails);
				inI1++;
			}
		}
		arrAdptr = new ArrayAdapter<String>(this, R.layout.main_lstv, strlstResult1);
		vwFooter = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.listfooter, null, false);
	}

	public void PrefSttgs(View vwSttgs)
	{
		if (vwSttgs == btnSttgs) 
		{
			openOptionsMenu();
		} 
		else if (vwSttgs == btnRefresh) 
		{
			strlstResult1.clear();
			arrAdptr.clear();
			inLimitSrchAsync = 0;
			inCountValues = 0;
			lnrlLoad.setVisibility(View.VISIBLE);
			strValue = "%a%";
			Thread tdScroll = new Thread(null, rbleLoadMoreListItems);
			tdScroll.start();
		}
	}

	@Override
	public void onResume() 
	{
		Log.i(TAG, "Assign :: onResume()");
		super.onResume();
		shpref = PreferenceManager.getDefaultSharedPreferences(this);
		strPrefServer = shpref.getString("pref_server_nme", "test.claudion.com");
		strPrefPort = shpref.getString("pref_port", "8069");
		inPrefPort = Integer.parseInt(strPrefPort);
		strPrefDb = shpref.getString("pref_db", strstrPrefDb);
		strPrefUname = shpref.getString("pref_uname", strstrPrefUname);
		strPrefPwd = shpref.getString("pref_pwd", strstrPrefPwd);
		if ((!strDbRes.equals(strPrefDb) && !strDbRes.equals(""))
				|| (!strPrefServerRes.equals(strPrefServer) && !strPrefServerRes
						.equals(""))
						|| (!strPrefPortRes.equals(strPrefPort) && !strPrefPortRes
								.equals(""))
								|| (!strPrefUnameRes.equals(strPrefUname) && !strPrefUnameRes
										.equals(""))
										|| (!strPrefPwdRes.equals(strPrefPwd) && !strPrefPwdRes
												.equals(""))) 
		{
			strlstResult1.clear();
			strLstDetails.clear();
			strLstFullDetails.clear();
			inLimitSrchAsync = 0;
			inCountValues = 0;
			lnrlLoad.setVisibility(View.VISIBLE);
			strValue = "%a%";
			Thread tdScroll = new Thread(null, rbleLoadMoreListItems);
			tdScroll.start();
		}
		this.setListAdapter(arrAdptr);
		isInternetPresent = DtectConnctn.isConnectingToInternet();
		this.getListView().setOnScrollListener(onLstScroll);
		this.getListView().setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int inPosition, long arg3) 
			{
				Intent optioncall = new Intent(Assign.this, Option.class);
				startActivity(optioncall);
				Assign.this.finish();
			}
		});
		etxtType.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) 
			{
				arrAdptr.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3)
			{
			}

			@Override
			public void afterTextChanged(Editable arg0)
			{
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.partners_menu, menu);
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu)
	{
		super.onOptionsMenuClosed(menu);
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_Settings:
			Intent itntPref = new Intent(getApplicationContext(),
					EditPreferance.class);
			this.strDbRes = strPrefDb;
			this.strPrefServerRes = strPrefServer;
			this.strPrefPortRes = strPrefPort;
			this.strPrefUnameRes = strPrefUname;
			this.strPrefPwdRes = strPrefPwd;
			startActivity(itntPref);

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		Log.i(TAG, "Assign :: onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
	}

	OnScrollListener onLstScroll = new OnScrollListener() 
	{
		@Override
		public void onScroll(AbsListView view, int inFstVbleItm,
				int inVbleItmCnt, int inTotalItmCnt) 
		{
			int inLastInScrn = inFstVbleItm + inVbleItmCnt;
			if ((inLastInScrn == inTotalItmCnt) && !(isLoadingMore)
					&& (isSearchIds == false) && (isInternetPresent == true))
			{
				lnrlLoad.setVisibility(View.VISIBLE);
				strValue = "%a%";
				Thread tdScroll = new Thread(null, rbleLoadMoreListItems);
				tdScroll.start();
			}
			else if (isInternetPresent == false) 
			{
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout));
				((TextView) layout.findViewById(R.id.tstText))
				.setText("Please Check your Internet Connection.");
				Toast tstText = new Toast(getBaseContext());
				tstText.setDuration(Toast.LENGTH_SHORT);
				tstText.setView(layout);
				tstText.show();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int inScrollState)
		{
		}
	};
	private Runnable rbleLoadMoreListItems = new Runnable()
	{
		@SuppressWarnings({ "rawtypes" })
		@Override
		public void run()
		{
			isLoadingMore = true;
			strlstResult = new ArrayList<String>();
			try 
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) 
			{
			}
			strLstFullDetails.clear();
			strLstDBContains.clear();
			XmlRpcClient xmlrpcDb = new XmlRpcClient();
			XmlRpcClientConfigImpl xmlrpcConfigDb = new XmlRpcClientConfigImpl();
			xmlrpcConfigDb.setEnabledForExtensions(true);
			try 
			{
				xmlrpcConfigDb.setServerURL((new URL("http", strPrefServer,
						inPrefPort, "/xmlrpc/db")));
			} 
			catch (MalformedURLException ex) 
			{
				ex.printStackTrace();
			}
			xmlrpcDb.setConfig(xmlrpcConfigDb);
			try 
			{
				// Retrieve databases
				Vector<Object> params = new Vector<Object>();
				Object result = xmlrpcDb.execute("list", params);
				Object[] a = (Object[]) result;
				Vector<String> res = new Vector<String>();
				for (int i = 0; i < a.length; i++) 
				{
					if (a[i] instanceof String) 
					{
						res.addElement((String) a[i]);
						String strDbCheck = a[i].toString();
						strLstDBContains.add(strDbCheck);
						System.out.println(strDbCheck);
					}
				}
			} 
			catch (XmlRpcException e) 
			{
				isDtlsFalse = true;
				e.printStackTrace();
			}
			if (strLstDBContains.contains(strPrefDb)) 
			{
				RPCLogin objLogin = new RPCLogin(strPrefServer, inPrefPort,
						strPrefDb, strPrefUname, strPrefPwd);
				inUId = objLogin.UserId(1);
				if (RPCLogin.isUserIdCheck == true) 
				{
					RPCLogin.isUserIdCheck = false;
					isLoginFalse = true;
				} 
				else 
				{
					int inSearchId = 0;
					XmlRpcClient xmlrpcExecute = new XmlRpcClient();
					XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
					clientConfig.setEnabledForExtensions(true);
					try 
					{
						clientConfig.setServerURL(new URL("http",
								strPrefServer, inPrefPort, "/xmlrpc/object"));
					} 
					catch (MalformedURLException e)
					{
						e.printStackTrace();
					}
					xmlrpcExecute.setConfig(clientConfig);
					int inUid = (Integer) inUId;
					Object[] objParams = { "name", "ilike", strValue };
					Vector<Object> params = new Vector<Object>();
					params.add(objParams);
					Vector<Object> vectSearchArg = new Vector<Object>();
					vectSearchArg.add(strPrefDb);
					vectSearchArg.add(inUid);
					vectSearchArg.add(strPrefPwd);
					vectSearchArg.add("project.project");
					vectSearchArg.add("search");
					vectSearchArg.add(params);
					vectSearchArg.add(inCountValues * 10);
					vectSearchArg.add(10);
					HashMap hmapResult = new HashMap();
					try 
					{
						objSearchIds = (Object[]) xmlrpcExecute.execute(
								"execute", vectSearchArg);
						inobjSearchidslength = 0;
						inobjSearchidslength = objSearchIds.length;
						if (objSearchIds.length == 0)
						{
							isSearchIds = true;
						}
						inCountValues = inCountValues + 1;
						for (Object objSearchInputs : objSearchIds) 
						{
							Log.i(TAG, "Assign :: Partner ID : "
									+ objSearchInputs.toString());
							inCount++;
							inLimitSrchAsync++;
							inSearchId = Integer.parseInt(objSearchInputs
									.toString());
							Object[] objResultFields = { "complete_name", "id" };
							Vector<Object> vectReadArgs = new Vector<Object>();
							vectReadArgs.add(strPrefDb);
							vectReadArgs.add(inUid);
							vectReadArgs.add(strPrefPwd);
							vectReadArgs.add("project.project");
							vectReadArgs.add("read");
							vectReadArgs.add(inSearchId);
							vectReadArgs.add(objResultFields);
							try 
							{
								hmapResult = (HashMap) xmlrpcExecute.execute(
										"execute", vectReadArgs);
							} 
							catch (XmlRpcException ex) 
							{
								ex.printStackTrace();
							}
							String strResult = hmapResult.toString();
							String strLstResult = strResult.substring(1,
									strResult.length() - 1);
							if (!(strlstResult1.contains(strLstResult)))
							{
								strlstResult.remove(hmapResult.toString());
								strlstResult
								.add(objSearchInputs != null ? strLstResult
										: null);
							}
							Log.i("inCountValues Here : ", "inCountValues : "
									+ inCountValues);
							strLstFullDetails.add(strLstResult);
						}
						Collections.sort(strlstResult);
						Collections.sort(strLstFullDetails);
						for (int i = 0; i < strlstResult.size(); i++) {
							strlstResult1.add(strlstResult.get(i));
							strLstFullDetails1.add(strLstFullDetails.get(i));
						}
						Collections.sort(strlstResult1);
						Collections.sort(strLstFullDetails1);
						clearpreference();
						for (int inI = 1; inI <= strlstResult1.size(); inI++)
						{
							String strDispValue = "";
							strDispValue = strLstFullDetails1.get(inI - 1)
									.toString();
							String strValuepost1 = "";
							strValuepost1 = strlstResult1.get(inI - 1)
									.toString();
							strValuepost = "";
							strValuepost = strValuepost1.concat("partner_split"
									+ strDispValue
									.concat("partner_split"
											+ strstrPrefDb)
											.concat("partner_split"
													+ strstrPrefUname)
													.concat("partner_split"
															+ strstrPrefPwd)
															.concat("partner_split"
																	+ inCountValues));
							strKeypost = "";
							strKeypost = "post" + (inI);
							Assign.this
							.Savepreference(strKeypost, strValuepost);
						}
						strlstvArray = strlstResult1
								.toArray(new String[strlstResult1.size()]);
					}
					catch (XmlRpcException ex) 
					{
						ex.printStackTrace();
					}
				}
			} 
			else 
			{
				isDbFalse = true;
			}
			Log.i(TAG, "Assign :: List Items Count : " + inCountValues);
			runOnUiThread(rbleRes);
		}
	};
	private Runnable rbleRes = new Runnable() 
	{
		@Override
		public void run()
		{
			if (isLoginFalse == true)
			{
				isLoginFalse = false;
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout));
				((TextView) layout.findViewById(R.id.tstText))
				.setText("Check your login details");
				Toast tstText = new Toast(getBaseContext());
				tstText.setDuration(Toast.LENGTH_SHORT);
				tstText.setView(layout);
				tstText.show();
			}
			if (isDbFalse == true)
			{
				isDbFalse = false;
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout));
				((TextView) layout.findViewById(R.id.tstText))
				.setText("Check the details you entered");
				Toast tstText = new Toast(getBaseContext());
				tstText.setDuration(Toast.LENGTH_SHORT);
				tstText.setView(layout);
				tstText.show();
			}
			lnrlLoad.setVisibility(View.INVISIBLE);
			if (strlstResult1 != null && strlstResult1.size() > 0) 
			{
				for (int i = 0; i < strlstResult1.size(); i++) 
				{
					strLstDetails.add(strLstFullDetails1.get(i).toString());
				}
			}
			arrAdptr.notifyDataSetChanged();
			isLoadingMore = false;
		}
	};

	private void clearpreference()
	{
		SharedPreferences msharedpreference = getSharedPreferences("PrefSpeed",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = msharedpreference.edit();
		editor.clear();
		editor.commit();
	}

	public void Savepreference(String strKey, String strValue)
	{
		shPrefSave = getSharedPreferences("PrefSpeed", MODE_PRIVATE);
		SharedPreferences.Editor editor = shPrefSave.edit();
		editor.putString(strKey, strValue);
		editor.commit();
	}
}