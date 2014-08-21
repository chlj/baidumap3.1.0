package com.example.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * 5. 单个点对点变颜色
 * 
 * @author Administrator
 * 
 */
public class baidu_map_point_to_point_Activity extends Activity implements
		OnClickListener {
	private TextView title_text;
	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	boolean isFirstLoc = true;// 是否首次定位
	private RelativeLayout rls;
	public MyLocationListenner myListener = new MyLocationListenner();

	private TextView txt_map_title, txt_map_distance; // 泡泡层

	private LatLng gpspoint, point = null; // 初始化定位的点，和图标显示的点

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.baidumap_mark_point);
		findview(); // 找到控件

		findview2();
	}

	public void findview2() {
		txt_map_title = (TextView) findViewById(R.id.txt_map_title);
		txt_map_distance = (TextView) findViewById(R.id.txt_map_distance);
	}

	public void findview() {
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("单个点对点变颜色");
		rls = (RelativeLayout) findViewById(R.id.id_marker_info);
		// 显示地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

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

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {
				point = mapPoi.getPosition();
				showPoint(mapPoi.getPosition());
				rls.setVisibility(View.VISIBLE);
				txt_map_title.setText(mapPoi.getName());
				double distance = DistanceUtil.getDistance(gpspoint, point); // 坐标点之间的距离
																				// 米
				txt_map_distance.setText("距我" + distance + "m  |");
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				rls.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 显示指定的坐标 [推荐使用]
	 * 
	 * @param point
	 */
	private void showPoint(LatLng point) {
		mBaiduMap.clear();
		// 标记指定的点 指定坐标 和 缩放值
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, 19);
		mBaiduMap.animateMapStatus(u);
		// 添加图标
		mBaiduMap.addOverlay(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.perspective(true)
				.position(point)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_openmap_focuse_mark)));

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
			
				
				return false;
			}
		});
	}

	/**
	 * 显示指定的坐标 [显示的图标偏大]
	 * 
	 * @param point
	 */
	private void showPoint2(LatLng point) {

		mBaiduMap.clear();
		// 有个默认死的图标不能修改
		MyLocationData locData = new MyLocationData.Builder()
				.latitude(point.latitude).longitude(point.longitude).build();
		mBaiduMap.setMyLocationData(locData);

		// 修改图标为自定义marker
		LocationMode mCurrentMode = LocationMode.FOLLOWING; // [跟随->将选中的点移动到屏幕的正中央]
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

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
				isFirstLoc = false;
				LatLng point = new LatLng(location.getLatitude(),
						location.getLongitude());
				showPoint(point); // 显示坐标

				gpspoint = point;
				mLocClient.stop(); //停止定位
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
