package com.music;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LookActivity extends Activity{

	private EditText address;
	private Button see;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look);
		
		address=(EditText)findViewById(R.id.address);
		see=(Button)findViewById(R.id.see);
		
		see.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				address=(EditText)findViewById(R.id.address);
				String uri=address.getText().toString();
				Intent intent = new Intent();// 创建Intent对象
				intent.setAction(Intent.ACTION_VIEW);// 为Intent设置动作
				intent.setData(Uri.parse(uri));// 为Intent设置数据
				startActivity(intent);
			}
		});
	}
	
}
