package com.example.itube;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class VideoPlayerFragment extends Fragment {
    private WebView youtubeWebView;

    private Button backButton;

    public VideoPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        youtubeWebView = view.findViewById(R.id.youtubeWebView);
        backButton = view.findViewById(R.id.backButton);

        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        youtubeWebView.setWebViewClient(new WebViewClient());

        String videoId = getArguments().getString("videoId");
        String iframeUrl = "https://www.youtube.com/embed/" + videoId;

        String html = "<html><body style='margin:0;padding:0;'>" +
                "<iframe width='100%' height='100%' " +
                "src='" + iframeUrl + "' frameborder='0' " +
                "allow='autoplay; encrypted-media' allowfullscreen>" +
                "</iframe></body></html>";

        youtubeWebView.loadData(html, "text/html", "utf-8");

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
