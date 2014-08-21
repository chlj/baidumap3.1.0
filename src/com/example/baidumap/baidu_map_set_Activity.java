package com.example.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

/**
 * 2.地图一般设置
 * 
 * @author Administrator
 * 
 */
public class baidu_map_set_Activity extends Activity implements OnClickListener {

	private TextView title_text;
	private LinearLayout linpubset;
	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private CheckBox traffice;// 交通图
	private RadioGroup RadioGroup;
	private RadioButton normal, statellite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.baidumap_load);
		findview(); // 找到控件
	}

	public void findview() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		linpubset = (LinearLayout) findViewById(R.id.linpubset);

		title_text = (TextView) findViewById(R.id.title_text);
		if ("0".equals(this.getIntent().getStringExtra("type"))) {
			linpubset.setVisibility(View.GONE);
		} else {
			linpubset.setVisibility(View.VISIBLE);
			title_text.setText("地图一般设置");
		}

		
		
		// 显示地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//
		RadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
		traffice = (CheckBox) findViewById(R.id.traffice);

		normal = (RadioButton) findViewById(R.id.normal);
		statellite = (RadioButton) findViewById(R.id.statellite);

		//普通图
		normal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean checked = ((RadioButton) view).isChecked();
				if (checked) {
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
				}
			}
		});
		//卫星图
		statellite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean checked = ((RadioButton) view).isChecked();
				if (checked) {
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				}
			}
		});
        //交通图
		traffice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mBaiduMap.setTrafficEnabled(((CheckBox) view).isChecked());
			}
		});
		
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
	}
}
