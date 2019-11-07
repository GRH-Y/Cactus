package com.yyz.ard.cactus.uiaf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.net.URL;

import log.LogDog;
import util.StringEnvoy;

/**
 * @className: WebContainer
 * @classDescription:
 * @author: yyz
 * @createTime: 2019/8/2
 */
public class WebContainer {

    protected WebView mWebView;
    protected SwipeRefreshLayout mSrlMainSwipeRefresh = null;
    protected boolean mIsSwipeRefreshEnabled = false;

    private OnWebViewListener mListener = null;
    private String cachePath = null;

    public WebContainer(WebView webView) {
        this.mWebView = webView;
        if (webView == null) {
            throw new NullPointerException(" webView is null !!!");
        }
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout layout) {
        this.mSrlMainSwipeRefresh = layout;
    }

    public void setSwipeRefreshLoading(boolean loading) {
        mIsSwipeRefreshEnabled = loading;
    }

    public void setOnWebViewListener(OnWebViewListener listener) {
        mListener = listener;
    }

    public void setCachePath(String cachePath) {
        if (StringEnvoy.isNotEmpty(cachePath)) {
            if (cachePath.endsWith("/")) {
                this.cachePath = cachePath;
            } else {
                this.cachePath = cachePath + File.separator;
            }
        }
    }

    public void initWebView() {
        initWebView(null);
    }

    public void initWebView(String ua) {
        WebSettings webSettings = mWebView.getSettings();
        // 支持启用缓存模式
        webSettings.setAppCacheEnabled(true);
        // 开启数据库缓存
        webSettings.setDatabaseEnabled(true);
        final String dbPath = mWebView.getContext().getDir("web_db", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dbPath);
        // 支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 开启DOM缓存,默认状态下是不支持LocalStorage的
        webSettings.setDomStorageEnabled(true);
        // 支持javascript
        webSettings.setJavaScriptEnabled(true);
        // 去掉縮放工具 api最低版本11
        webSettings.setDisplayZoomControls(true);
        // 设置控件属性，网页大小适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        // 设置 WebView 的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setTextZoom(100);
        //设置UA
        if (StringEnvoy.isNotEmpty(ua)) {
            String newUA = webSettings.getUserAgentString() + ua;
            webSettings.setUserAgentString(newUA);
        }
        //解决混合模式下图片加载不出来
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setBlockNetworkImage(false);

        mWebView.setWebViewClient(new WebViewClient() {

//            @Nullable
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                if (StringEnvoy.isEmpty(cachePath)) {
//                    return super.shouldInterceptRequest(view, url);
//                }
//                WebResourceResponse response;
//                if (url.endsWith(".png")) {
//                    response = getWebResourceResponse(url, "image/png");
//                } else if (url.endsWith(".gif")) {
//                    response = getWebResourceResponse(url, "image/gif");
//                } else if (url.endsWith(".jpg")) {
//                    response = getWebResourceResponse(url, "image/jepg");
//                } else if (url.endsWith(".jepg")) {
//                    response = getWebResourceResponse(url, "image/jepg");
//                } else if (url.endsWith(".js")) {
//                    //"text/javascript"
//                    response = getWebResourceResponse(url, "UTF-8");
//                } else if (url.endsWith(".css")) {
//                    //"text/css"
//                    response = getWebResourceResponse(url, "UTF-8");
//                } else if (url.endsWith(".html")) {
//                    //"text/html"
//                    response = getWebResourceResponse(url, "UTF-8");
//                } else {
//                    return super.shouldInterceptRequest(view, url);
//                }
//                return response;
//            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (mSrlMainSwipeRefresh != null && mSrlMainSwipeRefresh.isRefreshing()) {
                    mSrlMainSwipeRefresh.setRefreshing(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                if (mListener != null) {
                    mListener.onReceivedError(view, null, null, errorCode);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (mSrlMainSwipeRefresh != null && mSrlMainSwipeRefresh.isRefreshing()) {
                    mSrlMainSwipeRefresh.setRefreshing(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mListener != null) {
                    // 或者： if(request.getUrl().toString() .equals(getUrl()))
                    if (request.isForMainFrame()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mListener.onReceivedError(view, request, error, error.getErrorCode());
                        } else {
                            mListener.onReceivedError(view, request, error, -100);
                        }
                    }
                }
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mListener != null) {
                    mListener.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mSrlMainSwipeRefresh != null && mSrlMainSwipeRefresh.isRefreshing()) {
                    mSrlMainSwipeRefresh.setRefreshing(false);
                }
                if (mListener != null) {
                    mListener.onPageFinished(view, url);
                }
            }

            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogDog.d("==> WebContainer loading url = " + url);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mWebView.getContext().startActivity(intent);
                    return true;
                }
                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        mWebView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {
                        Toast.makeText(mWebView.getContext(), "打开支付宝失败!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                if (url.startsWith("weixin://wap/pay?")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mWebView.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(mWebView.getContext(), "打开微信失败!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                if (mListener != null) {
                    return mListener.onShouldOverrideUrlLoading(view, url);
                }
                return false;
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mListener != null) {
                    mListener.onProgressChanged(view, newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (mListener != null) {
                    mListener.onReceivedTitle(view, title);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mListener != null) {
                    return mListener.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                }
                return true;
            }
        });

    }

    private WebResourceResponse getWebResourceResponse(String url, String mime) {
        WebResourceResponse response = null;
        try {
            response = new WebResourceResponse(mime, "UTF-8", new URL(url).openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public void enabledWebviewDebug() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public interface OnWebViewListener {

        void onProgressChanged(WebView view, int newProgress);

        void onReceivedTitle(WebView view, String title);

        void onPageStarted(WebView view, String url, Bitmap favicon);

        boolean onShouldOverrideUrlLoading(WebView view, String url);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error, int errorCode);

        boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);
    }

}
