package com.example.baidumap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 3. 给指定的一个点进行标记
 * 
 * @author Administrator
 * 
 */
public class baidu_map_mark_point_Activity extends Activity implements
		OnClickListener {
	private TextView title_text;
	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private static final LatLng point = new LatLng(29.821723, 112.898416);// 纬度,经度

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
		title_text.setText("标记指定的点");

		// 显示地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.clear();

		// 添加图标
		mBaiduMap
				.addOverlay(new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.perspective(true)
						.position(point)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_geo)));

		// 标记指定的点
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 15)); // 指定坐标
																					// 和
																					// 缩放值

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
