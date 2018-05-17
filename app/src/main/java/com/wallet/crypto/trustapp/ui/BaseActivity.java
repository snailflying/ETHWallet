package com.wallet.crypto.trustapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wallet.crypto.trustapp.R;

import dagger.android.AndroidInjection;


public abstract class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
	}

	protected Toolbar toolbar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setTitle(getTitle());
		}
		enableDisplayHomeAsUp();
		return toolbar;
	}

	protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

	protected void enableDisplayHomeAsUp() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	protected void dissableDisplayHomeAsUp() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	protected void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
	    if (actionBar != null) {
	        actionBar.hide();
        }
    }

    protected void showToolbar() {
        ActionBar actionBar = getSupportActionBar();
	    if (actionBar != null) {
	        actionBar.show();
        }
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
}
