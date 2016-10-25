package com.codepath.nytarticles.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytarticles.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.wvArticle) WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lunchChromeTab();
    }

    /*
    * This Method Lunch the Chrome web browser
    */
    private void lunchChromeTab() {

        String url = null;

        // get the url
        if ( getIntent().hasExtra("webUrl") ) {
            url = getIntent().getStringExtra("webUrl");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "webUrl");
        }

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        // add share action to menu list
        builder.addDefaultShareMenuItem();
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(url));

        finish();
    }

}
