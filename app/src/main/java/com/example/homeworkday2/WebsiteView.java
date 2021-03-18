package com.example.homeworkday2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteView extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    WebView websiteView;
    String webPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_view);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        webPage = sharedPreferences.getString("website", "");
        websiteView = findViewById(R.id.website_view);
        websiteView.setWebViewClient(new WebViewClient());
        websiteView.loadUrl("https://"+webPage);
    }
}