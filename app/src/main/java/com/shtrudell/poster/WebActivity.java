package com.shtrudell.poster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String url = getIntent().getStringExtra("URL");

        WebView webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);
    }
}