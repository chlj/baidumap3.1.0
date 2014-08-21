package com.example.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 14. 百度地图界面（一）
 * 
 * @author Administrator
 * 
 */
public class baidu_map_UI_Activity extends FragmentActivity implements
		OnClickListener {
	private TextView title_text;
	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	boolean isFirstLoc = true;// 是否首次定位
	public MyLocationListenner myListener = new MyLocationListenner();

	private Button traffic, central_point;

	private ScaleView mScaleView;
	private ZoomControlView mZoomControlView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.baidumap_ui);
		findview(); // 找到控件
	}

	public void findview() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("百度界面(一)");

		traffic = (Button) findViewById(R.id.traffic);
		central_point = (Button) findViewById(R.id.central_point);

		// 交通图
		traffic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if ("0".equals(traffic.getTag())) {
					mBaiduMap.setTrafficEnabled(true);
					traffic.setTag("1");

				} else {
					mBaiduMap.setTrafficEnabled(false);
					traffic.setTag("0");

				}
			}
		});

		// 定位
		central_point.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(baidu_map_UI_Activity.this, "开启定位");
				startGPS();
			}
		});

		// 显示地图

		 mMapView = (MapView) findViewById(R.id.bmapView);
		 mBaiduMap = mMapView.getMap();
		 
		 
		//
		//
		// BaiduMapOptions bo=new BaiduMapOptions();
		// bo.zoomControlsEnabled(false);

//		mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
//				.findFragmentById(R.id.bmapView))).getBaiduMap();
//		startGPS();

		
	
		
		mScaleView = (ScaleView) findViewById(R.id.scaleView);
		mScaleView.setMapView(mMapView);
		mZoomControlView = (ZoomControlView) findViewById(R.id.ZoomControlView);
		mZoomControlView.setMapView(mMapView);
		refreshScaleAndZoomControl();

	}

	private void refreshScaleAndZoomControl() {
		// 更新缩放按钮的状态
		mZoomControlView.refreshZoomButtonStatus(Math.round(mMapView.getMap()
				.getMapStatus().zoom));
		mScaleView.refreshScaleView(Math
				.round(mMapView.getMap().getMapStatus().zoom));
	}

	// 开启GPS定位
	public void startGPS() {

		isFirstLoc = true;
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			if (isFirstLoc) {
				mBaiduMap.clear();

				isFirstLoc = false;
				LatLng point = new LatLng(location.getLatitude(),
						location.getLongitude());

				// 标记指定的点 指定坐标 和 缩放值
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point,
						19);
				mBaiduMap.animateMapStatus(u);

				// 添加图标
				mBaiduMap.addOverlay(new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.perspective(true)
						.position(point)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_geo)));
				mLocClient.stop();
				// 关闭定位图层
				mBaiduMap.setMyLocationEnabled(false);

			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
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

		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
