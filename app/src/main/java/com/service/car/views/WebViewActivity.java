package com.service.car.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.service.car.R;

public class WebViewActivity extends AppCompatActivity {
    private String weburl;
    WebView web_privacy_policy;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            weburl = bundle.getString("url");
        }
        web_privacy_policy = findViewById(R.id.privacypolicy_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_privacypolicy);
        progressBar.setVisibility(View.VISIBLE);

        // TODO : ADD TERMS AND CONDITIONS URL HERE
//            loadwebUrl(TERMS_URL);

    }

    private void loadwebUrl(String webUrl) {
        web_privacy_policy.getSettings().setLoadsImagesAutomatically(true);
        web_privacy_policy.getSettings().setJavaScriptEnabled(true);
        web_privacy_policy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_privacy_policy.setWebViewClient(new WebViewClient());
        web_privacy_policy.loadUrl(webUrl);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

    }
}
