package com.hts.assign;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import android.util.Log;

public class RPCLogin {
	public static boolean isUserIdCheck = false;
	int inPort;
	String strServer;
	String strDb;
	String strUname;
	String strPwd;

	public RPCLogin(String strPrefServer, int inPrefPort, String strPrefDb,
			String strPrefUname, String strPrefPwd) {
		strServer = strPrefServer;
		inPort = inPrefPort;
		strUname = strPrefUname;
		strPwd = strPrefPwd;
		strDb = strPrefDb;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int UserId(int inUid) {
		if (strUname == null) {
			return 0;
		} else {
			Object uId = null;
			Vector loginParams = new Vector();
			loginParams.addElement(strDb);
			loginParams.addElement(strUname);
			loginParams.addElement(strPwd);
			XmlRpcClient xmlrpcLogin = new XmlRpcClient();
			XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
			xmlrpcConfigLogin.setEnabledForExtensions(true);
			try {
				xmlrpcConfigLogin.setServerURL(new URL("http", strServer,
						inPort, "/xmlrpc/common"));
			} catch (MalformedURLException e3) {
				e3.printStackTrace();
			}
			xmlrpcLogin.setConfig(xmlrpcConfigLogin);
			try {
				uId = xmlrpcLogin.execute("login", loginParams);
				Log.i(Assign.TAG, "RPCLogin :: Login Id : " + uId.toString());
				if (uId.equals(false)) {
					isUserIdCheck = true;
				}
			} catch (XmlRpcException e2) {
				e2.printStackTrace();
			}
			return inUid;
		}
	}
}
