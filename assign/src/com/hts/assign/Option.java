package com.hts.assign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Option extends Activity
{
	Button btnMyTicket;
	Button btnNewTicket;
	Button btnTicketForMe;
	Button btnReports;

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intntmain = new Intent(Option.this, Assign.class);
		startActivity(intntmain);
		Option.this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.option);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		btnMyTicket = (Button) findViewById(R.id.myticket);
		btnNewTicket = (Button) findViewById(R.id.newticket);
		btnTicketForMe = (Button) findViewById(R.id.ticketsforme);
		btnReports = (Button) findViewById(R.id.reports);
	}

	public void projectclick(View button) 
	{
		if (button == btnMyTicket) 
		{
			Intent intntMyticket = new Intent(Option.this, Myticket.class);
			startActivity(intntMyticket);
		}
		if (button == btnNewTicket)
		{
			Intent intntMyticket = new Intent(Option.this, Newticket.class);
			startActivity(intntMyticket);
		}
		if (button == btnTicketForMe) 
		{
			Toast.makeText(getApplicationContext(), "Pending....", Toast.LENGTH_SHORT).show();
		}
		if (button == btnReports)
		{
			Toast.makeText(getApplicationContext(), "Pending....", Toast.LENGTH_SHORT).show();
		}

	}
}
