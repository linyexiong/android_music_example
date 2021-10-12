# android_music_example
Android apps, apps that can only play one song

1.上学期，选修课为安卓，要求我们做一个小应用作为安卓的大作业，我就做了一个小小的音乐播放器，实现的功能很少，就是使用Service服务来播放一首音乐，其中用了很多菜单效果，然后主界面实现了一个画廊的效果，使用Gallery这个过时的控件来做，并有三个图片按钮，功能为暂停，播放，停止音乐，下面有个滑动条实现了控制音量的功能，主页面如下所示：

![image](https://user-images.githubusercontent.com/10420128/136875513-67e1f476-ae1c-400b-9750-7f700b520360.png)


2.布局文件activity_main.xml代码如下：

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >
 
    <Gallery
        android:id="@+id/gallery1"
        android:layout_width="match_parent"
        android:layout_height="330dp"
        android:spacing="10px"
        android:unselectedAlpha="0.8" />
 
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@drawable/dzp"
        android:orientation="vertical"
        android:padding="10dp" >
 
        <RelativeLayout
            android:id="@+id/relativeLayout1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:gravity="center_vertical|center_horizontal" >
 
            <ImageButton
                android:id="@+id/play"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:onClick="click"
                android:padding="10dp"
                android:src="@drawable/play" />
 
            <ImageButton
                android:id="@+id/pause"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_toRightOf="@id/play"
                android:onClick="click"
                android:padding="10dp"
                android:src="@drawable/pause" />
 
            <ImageButton
                android:id="@+id/stop"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_toRightOf="@id/pause"
                android:onClick="click"
                android:padding="10dp"
                android:src="@drawable/stop" />
        </RelativeLayout>
 
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@+id/relativeLayout1" >
 
            <SeekBar
                android:id="@+id/seekBar1"
                android:layout_width="321dp"
                android:layout_height="wrap_content" />
        </LinearLayout>
    </LinearLayout>
 
</LinearLayout>


3.MainActivity.java文件代码如下：

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
这个类主要实现了 画廊的效果和滑动条控制音量的功能，以及一些菜单的实现，把播放音乐的功能放到服务里实现，所以接下来要附上服务类的代码。



4.MusicService.java文件代码如下：

package com.service;
 
import java.io.IOException;
 
 
 
import com.music.R;
 
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
 
 
public class MusicService extends Service {
 
	public static  MediaPlayer player;// 创建player对象
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
 
	// 创建服务
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (player == null) {
			player=player.create(MusicService.this, R.raw.woman);//设置音乐资源并实例化player
			player.setLooping(false);//音乐不能循环
		}
		super.onCreate();
	}
	
	//服务销毁
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			player.release();//释放资源
			super.onDestroy();
		}
 
		//服务开始
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int op=bundle.getInt("msg");//获得从MainActivity.java里传递过来的op
			switch (op) {
			case 1://当op为1，即点击了播放按钮
				play();//调用play()方法
				break;
			case 2://当op为2，即点击了暂停按钮
				pause();//调用pause()方法
				break;
			case 3://当op为3，即点击了停止按钮
				stop();//调用stop()方法
				break;
			default:
				break;
			}
			return super.onStartCommand(intent, flags, startId);
		}
 
		
		private void play() {
			// TODO Auto-generated method stub
			if(!player.isPlaying()){
				player.start();//开始播放音乐
				Toast.makeText(MusicService.this, "正在播放音乐...", Toast.LENGTH_SHORT).show();
			}
		}
 
 
		private void pause() {
			// TODO Auto-generated method stub
			if(player.isPlaying()&&player!=null){
				player.pause();//暂停播放音乐
				Toast.makeText(MusicService.this, "暂停播放音乐...", Toast.LENGTH_SHORT).show();
				try {
					player.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void stop() {
			// TODO Auto-generated method stub
			if(player!=null){
				player.pause();//停止播放音乐
				player.seekTo(0);
				Toast.makeText(MusicService.this, "停止播放音乐...", Toast.LENGTH_SHORT).show();
			}
		}
 
}
这个服务类实现了播放一首歌的功能，其中我们可以把一首音乐放入res包下的raw文件夹下。


5.主要的类的代码已经给出，还有其它类和布局文件代码这里就不附上了，最后实现效果如上图主页面所示，还有其它的功能我也一一截图了，只附上截图，后面我会附上源代码下载地址：

![image](https://user-images.githubusercontent.com/10420128/136875579-951ccabd-435d-4469-8aa4-cbcf76775838.png)


点击浏览网页菜单的话，弹出下图的界面：

![image](https://user-images.githubusercontent.com/10420128/136875605-058ff70a-a084-4c5b-8af9-fb1565f247c4.png)


点击浏览你输入的网页地址按钮即可访问网络，其中你的主机必须要有网络！


如果点击关于作者按钮，就会弹出一个对话框，即下图所示：

![image](https://user-images.githubusercontent.com/10420128/136875634-46ed3bab-a24a-4925-aa4b-1f3b632355d3.png)

如果点击作者博客的话，就出现下图的对话框：

![image](https://user-images.githubusercontent.com/10420128/136875657-9eb6c4ff-b883-4c9d-bb6a-5a53b7d50895.png)

点击打开我的博客按钮的话，就可以打开我的博客地址。


点击退出应用菜单的话，就可以退出当前界面，并把存放在后台运行的程序关闭。


6.其中这个小项目有点小bug，请大家见谅！

我的csdn博客，其中文章地址：https://blog.csdn.net/u012561176/article/details/46343987
