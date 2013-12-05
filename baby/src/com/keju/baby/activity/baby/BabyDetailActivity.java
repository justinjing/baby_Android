package com.keju.baby.activity.baby;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.NewAddBabyRecordActivity;
import com.keju.baby.activity.base.BaseWebViewActivity;
import com.keju.baby.bean.BabyBean;
import com.keju.baby.util.SharedPrefUtil;

/**
 * 婴儿详情界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午3:29:41
 */
public class BabyDetailActivity extends BaseWebViewActivity implements OnClickListener{
	
	private BabyBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent() != null){
			bean = (BabyBean) getIntent().getExtras().getSerializable(Constants.EXTRA_DATA);
		}
		findView();
		fillData();
	}

	private void findView() {
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setImageResource(R.drawable.btn_back_selector);
		btnRight.setImageResource(R.drawable.btn_add_record_selector);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(bean.getName());
		webView = (WebView) findViewById(R.id.webview);
	}

	private void fillData() {
		loadUrl(Constants.URL_VISIT_RECORD + bean.getId());
		webView.addJavascriptInterface(new Object() {
			public void webviewAddVisit(int code) {
				if(code == 200){
					if(webView.getUrl() == null){
						return;
					}
					if(webView.canGoBack()){
						webView.goBack();
						webView.reload();
					}
				}
			}
		}, "app");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == Constants.REQUEST_NEW_ADD_VISIT_CODE){
			webView.reload();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			if(webView.getUrl() == null){
				return;
			}
			if(webView.getUrl().contains(Constants.URL_ADD_FOLLOW_UP) || webView.getUrl().contains(Constants.URL_NEED)){
				webView.goBack();
			}else{
				finish();
			}
			break;
		case R.id.btnRight:
			Bundle b = new Bundle();
			b.putInt(Constants.EXTRA_DATA, bean.getId());
			Intent intent = new Intent(this, NewAddBabyRecordActivity.class);
			intent.putExtras(b);
			startActivityForResult(intent, Constants.REQUEST_NEW_ADD_VISIT_CODE);
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if(webView.getUrl() == null){
				return super.onKeyDown(keyCode, event);
			}
			if(webView.getUrl().contains(Constants.URL_NEED)){
				if(webView.canGoBack()){
					webView.goBack();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
