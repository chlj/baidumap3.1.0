package com.example.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 4. 根据点 得到相应的信息 (单个点的信息)
 * 
 * @author Administrator
 * 
 */
public class baidu_map_mark_point_getInfo_Activity extends Activity implements
		OnClickListener {
	private TextView title_text;
	private Button btn_back;
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private RelativeLayout rls;
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
		title_text.setText("根据点得到相应的信息 ");
		rls = (RelativeLayout) findViewById(R.id.id_marker_info);
		
		// 显示地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.clear();

		// 添加图标

		mBaiduMap
				.addOverlay(new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.title("县医院旁")
						.perspective(true)
						.position(point)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_geo)));

		// 标记指定的坐标点
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 15)); 
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				rls.setVisibility(View.GONE);
			}
		});
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				LatLng mPoint = marker.getPosition(); // 获取 Marker 覆盖物的位置坐标
				View view = getLayoutInflater().inflate(R.layout.act_paopao2,
						null);

				TextView popupText = (TextView) view
						.findViewById(R.id.textcache);

				ImageView img = (ImageView) view.findViewById(R.id.popright);

				popupText.setText(marker.getTitle());
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(
								baidu_map_mark_point_getInfo_Activity.this,
								"点击了我", Toast.LENGTH_SHORT).show();
					}
				});
				// InfoWindow infoWindow = new InfoWindow(view, mPoint,
				// new OnInfoWindowClickListener() {
				// @Override
				// public void onInfoWindowClick() {
				// ToastUtils
				// .showToast(
				// baidu_map_mark_point_getInfo_Activity.this,
				// "点击的是图片");
				// }
				// });
				//
				// mBaiduMap.showInfoWindow(infoWindow);

				// createDialog();

				rls.setVisibility(View.VISIBLE);
				return false;

			}
		});
	}

	private void createDialog() {

		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setView(LayoutInflater.from(this).inflate(
				R.layout.create_user_dialog, null)); // 加入这段代码, 能弹出输入法
		dlg.show();
		final Window window = dlg.getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setContentView(R.layout.create_user_dialog);
		WindowManager.LayoutParams layparam = window.getAttributes();
		layparam.width = getWindowManager().getDefaultDisplay().getWidth();
		window.setWindowAnimations(R.style.mystyle);// 设置动画
		window.setAttributes(layparam);

		final RadioGroup group = (RadioGroup) window
				.findViewById(R.id.sexGroup);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) window
						.findViewById(group.getCheckedRadioButtonId());
				String str = radioButton.getText().toString();
				ToastUtils.showToast(
						baidu_map_mark_point_getInfo_Activity.this, str);
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
