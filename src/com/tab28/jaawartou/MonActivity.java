package com.tab28.jaawartou;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;

public class MonActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mon);
		ScrollTextView scrolltext=(ScrollTextView) findViewById(R.id.scrolltext);
        scrolltext.setText("error opening trace file: No such file or directory (2)\nerror opening trace file: No such file or directory (2)\nerror opening trace file: No such file or directory (2)\nerror opening trace file: No such file or directory (2)\nerror opening trace file: No such file or directory (2)\nerror opening trace file: No such file or directory (2)\nerror opening trace file: No such file or directory (2)\n");
        scrolltext.setTextColor(Color.WHITE);
        scrolltext.setEms(15);
        scrolltext.startScroll();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mon, menu);
		return true;
	}

}
