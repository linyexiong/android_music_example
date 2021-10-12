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

	public static  MediaPlayer player;// ����player����
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// ��������
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		if (player == null) {
			player=player.create(MusicService.this, R.raw.woman);//����������Դ��ʵ����player
			player.setLooping(false);//���ֲ���ѭ��
		}
		super.onCreate();
	}
	
	//��������
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			player.release();//�ͷ���Դ
			super.onDestroy();
		}

		//����ʼ
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			// TODO Auto-generated method stub
			Bundle bundle=intent.getExtras();
			int op=bundle.getInt("msg");//��ô�MainActivity.java�ﴫ�ݹ�����op
			switch (op) {
			case 1://��opΪ1��������˲��Ű�ť
				play();//����play()����
				break;
			case 2://��opΪ2�����������ͣ��ť
				pause();//����pause()����
				break;
			case 3://��opΪ3���������ֹͣ��ť
				stop();//����stop()����
				break;
			default:
				break;
			}
			return super.onStartCommand(intent, flags, startId);
		}

		
		private void play() {
			// TODO Auto-generated method stub
			if(!player.isPlaying()){
				player.start();//��ʼ��������
				Toast.makeText(MusicService.this, "���ڲ�������...", Toast.LENGTH_SHORT).show();
			}
		}


		private void pause() {
			// TODO Auto-generated method stub
			if(player.isPlaying()&&player!=null){
				player.pause();//��ͣ��������
				Toast.makeText(MusicService.this, "��ͣ��������...", Toast.LENGTH_SHORT).show();
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
				player.pause();//ֹͣ��������
				player.seekTo(0);
				Toast.makeText(MusicService.this, "ֹͣ��������...", Toast.LENGTH_SHORT).show();
			}
		}

}
