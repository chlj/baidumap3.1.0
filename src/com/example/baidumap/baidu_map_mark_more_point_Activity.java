package com.example.baidumap;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
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
import android.widget.TextView;

/**
 * 6. 给指定的多个点进行标记
 * 
 * @author Administrator
 * 
 */
public class baidu_map_mark_more_point_Activity extends Activity implements
		OnClickListener, OnGetGeoCoderResultListener {
	private TextView title_text;
	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private static final LatLng point1 = new LatLng(29.821723, 112.898416);// 纬度,经度
	private static final LatLng point2 = new LatLng(29.822437, 112.899111);// 纬度,经度

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.baidumap_mark_point);
		findview(); // 找到控件
	}

	public void findview() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("标记多个指定的点");

		// 显示地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		mBaiduMap.clear();

		showPoint(mBaiduMap, point1, 19);
		showPoint(mBaiduMap, point2, 19);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				LatLng point = marker.getPosition();
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(point));
				return false;
			}
		});
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

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// 根据坐标点查
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

			ToastUtils.showToast(baidu_map_mark_more_point_Activity.this,
					"抱歉，未能找到结果");
			return;
		}
		
		
		StringBuilder sb=new StringBuilder();
	    List<PoiInfo> list=	result.getPoiList();
	    for( int i=0;i<list.size();i++){
	    	if(i>0){
	    		sb.append(",");
	    	}
	    	sb.append(list.get(i).name);
	    }
		
		ToastUtils.showToast(baidu_map_mark_more_point_Activity.this,
				sb.toString());
		
		
	}
}
