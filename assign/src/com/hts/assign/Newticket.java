package com.hts.assign;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Newticket extends Activity
{
	public static final String TAG = "HTS";
	ArrayList<String> strLstDBContains = new ArrayList<String>();
	boolean isErrorDet = false;
	Button btnSubmit;
	Button btnCancel;
	EditText etxtName;
	EditText etxtSubj;
	EditText etxtNotes;
	EditText etxtProjId;
	SharedPreferences shpref;
	Spinner spnPriority;
	int inPrefPort = 8069;
	int inUId;

	String strPrefServer = "test.claudion.com";
	String strPrefPort = "8069";
	String strPrefDb = "testdb";
	String strPrefUname = "admin";
	String strPrefPwd = "ser";
	String strstrPrefDb = "testdb";
	String strstrPrefUname = "admin";
	String strstrPrefPwd = "ser";

	String strName;
	String strSubj;
	String strNotes;
	String strProjId;
	String strPriority;
	String strRetId = "";
	String strPrior = "";

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.i(TAG, "Newticket :: onCreate()");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.newticket);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		etxtName = (EditText) findViewById(R.id.etxtName);
		etxtSubj = (EditText) findViewById(R.id.etxtSub);
		etxtNotes = (EditText) findViewById(R.id.etxtNotes);
		etxtProjId = (EditText) findViewById(R.id.etxtProjId);
		spnPriority = (Spinner) findViewById(R.id.spnPriority);
		List<String> list = new ArrayList<String>();
		list.add("LOW");
		list.add("MEDIUM");
		list.add("URGENT");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPriority.setAdapter(dataAdapter);
	}
	@Override
	public void onResume()
	{
		Log.i(TAG, "Newticket :: onResume()");
		super.onResume();
		shpref = PreferenceManager.getDefaultSharedPreferences(this);
		strPrefServer = shpref.getString("pref_server_nme", "test.claudion.com");
		strPrefPort = shpref.getString("pref_port", "8069");
		inPrefPort = Integer.parseInt(strPrefPort);
		strPrefDb = shpref.getString("pref_db", strstrPrefDb);
		strPrefUname = shpref.getString("pref_uname", strstrPrefUname);
		strPrefPwd = shpref.getString("pref_pwd", strstrPrefPwd);
	}
	public void SubmitClick(View vwSubmit)
	{
		if(vwSubmit == btnSubmit)
		{
			strName = etxtName.getText().toString();
			strSubj = etxtSubj.getText().toString();
			strNotes = etxtNotes.getText().toString();
			strProjId = etxtProjId.getText().toString();
			strPriority = String.valueOf(spnPriority.getSelectedItem());
			if(strPriority.equals("LOW"))
			{
				strPrior = "0";
			}
			else if(strPriority.equals("MEDIUM"))
			{
				strPrior = "1";
			}
			else if(strPriority.equals("URGENT"))
			{
				strPrior = "2";
			}
			Thread tdScroll = new Thread(null, rbleCreate);
			tdScroll.start();
		}
		else if(vwSubmit == btnCancel)
		{
			finish();
		}
	}
	private Runnable rbleCreate = new Runnable() 
	{
		@Override
		public void run() 
		{
			XmlRpcClient xmlrpcDb = new XmlRpcClient();
			XmlRpcClientConfigImpl xmlrpcConfigDb = new XmlRpcClientConfigImpl();
			xmlrpcConfigDb.setEnabledForExtensions(true);
			try 
			{
				xmlrpcConfigDb.setServerURL((new URL("http", strPrefServer,	inPrefPort, "/xmlrpc/db")));
			} 
			catch (MalformedURLException ex) 
			{
				ex.printStackTrace();
			}
			xmlrpcDb.setConfig(xmlrpcConfigDb);
			try 
			{
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
				e.printStackTrace();
			}
			if (strLstDBContains.contains(strPrefDb))
			{
				if(!(strName.equals("")) || !(strSubj.equals("")) || !(strNotes.equals("")) 
						|| !(strProjId.equals("")) || !(strPrior.equals("")))
				{				
					RPCLogin objLogin = new RPCLogin(strPrefServer, inPrefPort,
							strPrefDb, strPrefUname, strPrefPwd);
					inUId = objLogin.UserId(1);
					if (RPCLogin.isUserIdCheck == true) 
					{
						RPCLogin.isUserIdCheck = false;
					} 
					else 
					{
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
						HashMap<Object, Object> params = new HashMap<Object, Object>();

						params.put("name", strName);
						params.put("description", strSubj);
						params.put("notes", strNotes);
						params.put("project_id", strProjId);
						params.put("priority", strPrior);
						Vector<Object> vectArgs = new Vector<Object>();

						vectArgs.add("testdb");
						vectArgs.add(inUid);
						vectArgs.add("ser");
						vectArgs.add("project.task");
						vectArgs.add("create");
						vectArgs.add(params);

						try 
						{
							Object ret_id = xmlrpcExecute.execute("execute", vectArgs);
							strRetId = ret_id.toString();
						}
						catch (XmlRpcException e) 
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					isErrorDet = true;
				}
			}
			runOnUiThread(rbleRes);
		}
	};
	private Runnable rbleRes = new Runnable()
	{
		@Override
		public void run() 
		{
			if (isErrorDet == true) 
			{
				isErrorDet = false;
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout));
				((TextView) layout.findViewById(R.id.tstText))
				.setText("Please enter Full Details");
				Toast tstText = new Toast(getBaseContext());
				tstText.setDuration(Toast.LENGTH_SHORT);
				tstText.setView(layout);
				tstText.show();
			}
			else
			{
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.toast_layout));
				((TextView) layout.findViewById(R.id.tstText))
				.setText("Created new ticket with id : " + strRetId);
				Toast tstText = new Toast(getBaseContext());
				tstText.setDuration(Toast.LENGTH_SHORT);
				tstText.setView(layout);
				tstText.show();
			}
		}
	};
	public void tstNotification(String strText) 
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
		((TextView) layout.findViewById(R.id.tstText)).setText(strText);
		Toast tstText = new Toast(getBaseContext());
		tstText.setDuration(Toast.LENGTH_SHORT);
		tstText.setView(layout);
		tstText.show();
	}
}
