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
