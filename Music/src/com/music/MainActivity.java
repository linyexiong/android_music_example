package com.music;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.service.MusicService;

public class MainActivity extends Activity {
	private int[] imageId = new int[] { R.drawable.img01, R.drawable.img02,
			R.drawable.img03, R.drawable.img04, R.drawable.img05,
			R.drawable.img06, R.drawable.img07, R.drawable.img08,
			R.drawable.img09, R.drawable.img10, R.drawable.img11,
			R.drawable.img12, }; // 定义并初始化保存图片id的数组
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Gallery gallery = (Gallery) findViewById(R.id.gallery1); // 获取Gallery组件
		/* 使用BaseAdapter指定要显示的内容 */
		BaseAdapter adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageview; // 声明ImageView的对象
				if (convertView == null) {
					imageview = new ImageView(MainActivity.this); // 实例化ImageView的对象
					imageview.setScaleType(ImageView.ScaleType.FIT_XY); // 设置缩放方式
					imageview
							.setLayoutParams(new Gallery.LayoutParams(265, 220));
					TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
					imageview.setBackgroundResource(typedArray.getResourceId(
							R.styleable.Gallery_android_galleryItemBackground,
							0));
					imageview.setPadding(5, 0, 5, 0); // 设置ImageView的内边距
				} else {
					imageview = (ImageView) convertView;
				}
				imageview.setImageResource(imageId[position]); // 为ImageView设置要显示的图片
				return imageview; // 返回ImageView
			}

			/*
			 * 功能：获得当前选项的ID (non-Javadoc)
			 * 
			 * @see android.widget.Adapter#getItemId(int)
			 */
			@Override
			public long getItemId(int position) {
				return position;
			}

			/*
			 * 功能：获得当前选项 (non-Javadoc)
			 * 
			 * @see android.widget.Adapter#getItem(int)
			 */
			@Override
			public Object getItem(int position) {
				return position;
			}

			/*
			 * 获得数量 (non-Javadoc)
			 * 
			 * @see android.widget.Adapter#getCount()
			 */
			@Override
			public int getCount() {
				return imageId.length;
			}
		};
		gallery.setAdapter(adapter); // 将适配器与Gallery关联
		gallery.setSelection(imageId.length / 2); // 让中间的图片选中
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						"您选择了第" + String.valueOf(position+1) + "张图片",
						Toast.LENGTH_SHORT).show();
			}
		});

		/* 调整音量 */
		final AudioManager am = (AudioManager) MainActivity.this
				.getSystemService(Context.AUDIO_SERVICE); // 获取音频管理类的对象
		// 设置当前调整音量大小只是针对媒体音乐
		MainActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar1); // 获取拖动条
		seekbar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));// 设置拖动条的最大值
		int progress = am.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前的音量
		seekbar.setProgress(progress); // 设置拖动条的默认值为当前音量
		// 为拖动条组件添加OnSeekBarChangeListener监听器
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				am.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
						AudioManager.FLAG_PLAY_SOUND); // 设置改变后的音量
			}
		});

	}

	public void click(View v) {
		intent = new Intent(MainActivity.this, MusicService.class);
		int op = -1; // 定义一个中间变量
		switch (v.getId()) {
		case R.id.play: // 播放按钮
			op = 1;
			break;
		case R.id.pause:// 暂停按钮
			op = 2;
			break;
		case R.id.stop: // 停止按钮
			op = 3;
			break;
		default:
			break;
		}

		Bundle bundle = new Bundle();
		bundle.putInt("msg", op);
		intent.putExtras(bundle);
		startService(intent);// 开始服务
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.call:
			Intent intent1 = new Intent(MainActivity.this, LookActivity.class);
			startActivity(intent1);
			break;
		case R.id.about:
			openDialog();
			break;
		case R.id.blog:
			openDialog1();
			break;
		case R.id.exit:// 退出按钮
			if(intent!=null){
				stopService(intent);// 停止服务
				finish();// 退出本界面
			}else{
				finish();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void openDialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("关于作者");
		final View view1 = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.item, null);
		builder.setView(view1);
		builder.setPositiveButton("返回上个界面", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				finishActivity(R.layout.activity_main);
			}

		});
		builder.create().show();
	}

	protected void openDialog1() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("作者博客");
		final View view1 = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.blogitem, null);
		builder.setView(view1);
		builder.setPositiveButton("打开我的博客", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent();// 创建Intent对象
				intent2.setAction(Intent.ACTION_VIEW);// 为Intent设置动作
				intent2.setData(Uri.parse("http://blog.csdn.net/u012561176"));// 为Intent设置数据
				startActivity(intent2);

			}

		});
		builder.create().show();
	}
}
