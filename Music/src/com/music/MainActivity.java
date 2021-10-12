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
			R.drawable.img12, }; // ���岢��ʼ������ͼƬid������
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Gallery gallery = (Gallery) findViewById(R.id.gallery1); // ��ȡGallery���
		/* ʹ��BaseAdapterָ��Ҫ��ʾ������ */
		BaseAdapter adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageview; // ����ImageView�Ķ���
				if (convertView == null) {
					imageview = new ImageView(MainActivity.this); // ʵ����ImageView�Ķ���
					imageview.setScaleType(ImageView.ScaleType.FIT_XY); // �������ŷ�ʽ
					imageview
							.setLayoutParams(new Gallery.LayoutParams(265, 220));
					TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
					imageview.setBackgroundResource(typedArray.getResourceId(
							R.styleable.Gallery_android_galleryItemBackground,
							0));
					imageview.setPadding(5, 0, 5, 0); // ����ImageView���ڱ߾�
				} else {
					imageview = (ImageView) convertView;
				}
				imageview.setImageResource(imageId[position]); // ΪImageView����Ҫ��ʾ��ͼƬ
				return imageview; // ����ImageView
			}

			/*
			 * ���ܣ���õ�ǰѡ���ID (non-Javadoc)
			 * 
			 * @see android.widget.Adapter#getItemId(int)
			 */
			@Override
			public long getItemId(int position) {
				return position;
			}

			/*
			 * ���ܣ���õ�ǰѡ�� (non-Javadoc)
			 * 
			 * @see android.widget.Adapter#getItem(int)
			 */
			@Override
			public Object getItem(int position) {
				return position;
			}

			/*
			 * ������� (non-Javadoc)
			 * 
			 * @see android.widget.Adapter#getCount()
			 */
			@Override
			public int getCount() {
				return imageId.length;
			}
		};
		gallery.setAdapter(adapter); // ����������Gallery����
		gallery.setSelection(imageId.length / 2); // ���м��ͼƬѡ��
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						"��ѡ���˵�" + String.valueOf(position+1) + "��ͼƬ",
						Toast.LENGTH_SHORT).show();
			}
		});

		/* �������� */
		final AudioManager am = (AudioManager) MainActivity.this
				.getSystemService(Context.AUDIO_SERVICE); // ��ȡ��Ƶ������Ķ���
		// ���õ�ǰ����������Сֻ�����ý������
		MainActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar1); // ��ȡ�϶���
		seekbar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));// �����϶��������ֵ
		int progress = am.getStreamVolume(AudioManager.STREAM_MUSIC); // ��ȡ��ǰ������
		seekbar.setProgress(progress); // �����϶�����Ĭ��ֵΪ��ǰ����
		// Ϊ�϶���������OnSeekBarChangeListener������
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
						AudioManager.FLAG_PLAY_SOUND); // ���øı�������
			}
		});

	}

	public void click(View v) {
		intent = new Intent(MainActivity.this, MusicService.class);
		int op = -1; // ����һ���м����
		switch (v.getId()) {
		case R.id.play: // ���Ű�ť
			op = 1;
			break;
		case R.id.pause:// ��ͣ��ť
			op = 2;
			break;
		case R.id.stop: // ֹͣ��ť
			op = 3;
			break;
		default:
			break;
		}

		Bundle bundle = new Bundle();
		bundle.putInt("msg", op);
		intent.putExtras(bundle);
		startService(intent);// ��ʼ����
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
		case R.id.exit:// �˳���ť
			if(intent!=null){
				stopService(intent);// ֹͣ����
				finish();// �˳�������
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
		builder.setTitle("��������");
		final View view1 = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.item, null);
		builder.setView(view1);
		builder.setPositiveButton("�����ϸ�����", new OnClickListener() {

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
		builder.setTitle("���߲���");
		final View view1 = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.blogitem, null);
		builder.setView(view1);
		builder.setPositiveButton("���ҵĲ���", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent();// ����Intent����
				intent2.setAction(Intent.ACTION_VIEW);// ΪIntent���ö���
				intent2.setData(Uri.parse("http://blog.csdn.net/u012561176"));// ΪIntent��������
				startActivity(intent2);

			}

		});
		builder.create().show();
	}
}
