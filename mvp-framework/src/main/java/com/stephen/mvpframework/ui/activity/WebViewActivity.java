package com.stephen.mvpframework.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stephen.mvpframework.local.Content;
import com.stephen.mvpframework.utils.LogUtil;
import com.stephen.mvpframework.utils.viewutils.ToastUtil;


/**
 * 通用展示网页Activity
 * Created by Stephen on 2017/12/1.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public abstract class WebViewActivity extends AbstractActivity {

    private WebView mWebView;

    //初始化WebView
    @SuppressLint("JavascriptInterface")
    protected void initWebView(WebView webView, String url, ScriptObject scriptObject) {
        mWebView = webView;
        //编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        //设置加载file协议的本地文件
        mWebView.getSettings().setAllowFileAccess(true);
        // 禁止file协议加载JavaScript,其他协议可以加载
        mWebView.getSettings().setJavaScriptEnabled(!url.startsWith("file://"));
        // 设置是否允许通过file url加载的Js代码读取其他的本地文件
        mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        // 设置是否允许通过file url加载的Javascript可以访问其他的源(包括http、https等源)
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        //删除漏洞接口
        //mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        //mWebView.removeJavascriptInterface(" accessibility");
        //mWebView.removeJavascriptInterface("accessibilityTraversal");

        //设置滚动条
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //开启原生与JS交互接口
        mWebView.addJavascriptInterface(scriptObject != null ? scriptObject : new ScriptObject(),
                Content.Params.JS_CALL_NAME);
        //设置缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置WebChrome客户端
        mWebView.setWebChromeClient(new WebChromeClient() {
            //进度监控
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //加载成功
                if (newProgress == 100) {
                    //DialogUtil.closeBlockingProgressDialog();
                }
            }
        });

        //设置WebView客户端
        mWebView.setWebViewClient(new WebViewClient() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //DialogUtil.closeBlockingProgressDialog();
                ToastUtil.INSTANCE.showErrorToast("网络错误：" + error.getErrorCode() + "," + error.getDescription());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //DialogUtil.showBlockingProgressDialog();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        LogUtil.INSTANCE.testInfo("loadUrl--->" + url);
        //启动WebView
        mWebView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        //DialogUtil.closeBlockingProgressDialog();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }

    /**
     * js与Android互调实体基类
     */
    protected class ScriptObject {

    }
}
