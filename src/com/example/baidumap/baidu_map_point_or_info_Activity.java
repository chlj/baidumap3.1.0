package com.example.baidumap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 8. 信息与坐标互换
 * 
 * @author Administrator
 * 
 */
public class baidu_map_point_or_info_Activity extends Activity implements
		OnClickListener, OnGetGeoCoderResultListener {

	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private EditText city, geocodekey, lat, lon;
	private Button btn1, btn2;

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.baidumap_point_or_info);
		findview(); // 找到控件
	}

	public void findview() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);

		city = (EditText) findViewById(R.id.city);
		geocodekey = (EditText) findViewById(R.id.geocodekey);
		lat = (EditText) findViewById(R.id.lat);
		lon = (EditText) findViewById(R.id.lon);

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);

		// 显示地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 根据地址查
				mSearch.geocode(new GeoCodeOption().city(
						city.getText().toString()).address(
						geocodekey.getText().toString()));
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 根据坐标查
				LatLng ptCenter = new LatLng((Float.valueOf(lat.getText()
						.toString())),
						(Float.valueOf(lon.getText().toString())));
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
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
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// 根据地址查
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(baidu_map_point_or_info_Activity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		showPoint(mBaiduMap,result.getLocation(),19);
		
		
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(baidu_map_point_or_info_Activity.this, strInfo, Toast.LENGTH_LONG).show();

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// 根据坐标查
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(baidu_map_point_or_info_Activity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		showPoint(mBaiduMap,result.getLocation(),19);
	
		Toast.makeText(baidu_map_point_or_info_Activity.this, result.getAddress(),
				Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示一个坐标点
	 * 
	 * @param mBaiduMap
	 * @param point
	 * @param zoom
	 */
	private void showPoint(BaiduMap mBaiduMap, LatLng point, int zoom) {
		// 添加图标
		mBaiduMap
				.addOverlay(new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.perspective(true)
						.position(point)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_geo)));
		// 标记指定的点
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newLatLngZoom(point, zoom));
	}

	
}
