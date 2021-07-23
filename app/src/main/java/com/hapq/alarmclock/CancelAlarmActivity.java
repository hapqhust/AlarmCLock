package com.hapq.alarmclock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CancelAlarmActivity extends AppCompatActivity {

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    TextView txtNhan, txtND, txtGio;
    Button btnhuy;
    int bool = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cancel_alarm);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnhuy = (Button) findViewById(R.id.buttonhuy);
        txtNhan = (TextView) findViewById(R.id.textViewNhan);
        txtND = (TextView) findViewById(R.id.textViewND);
        txtGio = (TextView) findViewById(R.id.textGio);

        Intent intent_nhan = getIntent();
        Bundle bundle = intent_nhan.getBundleExtra("extra");
        int vitri = bundle.getInt("Vitri");
        Alarm_Infor alarm = (Alarm_Infor) bundle.getSerializable("DoiTuong");

        txtNhan.setText(alarm.getLabel());
        txtND.setText(alarm.getContent());
        txtGio.setText(alarm.getTime());
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        bool = 0;

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bool == 0) {
                    if (alarm.getPassword().equals("")) {
                        Bundle bun = new Bundle();
                        bun.putString("Mode", "off");
                        bun.putInt("Vitri", vitri);
                        bun.putSerializable("DoiTuong", alarm);
                        intent.putExtra("extra", bun);
                        sendBroadcast(intent);
                    } else {
                        Dialog dialog = new Dialog(CancelAlarmActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.cf_pass);
                        dialog.setCanceledOnTouchOutside(false);
                        EditText edt = (EditText) dialog.findViewById(R.id.editTextTextPersonName1);
                        Button btnDongY = (Button) dialog.findViewById(R.id.buttonDongY1);
                        Button btnHuy = (Button) dialog.findViewById(R.id.buttonBo1);

                        btnDongY.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String s = edt.getText().toString();
                                if (s.equals(alarm.getPassword())) {
                                    Bundle bun = new Bundle();
                                    bun.putString("Mode", "off");
                                    bun.putInt("Vitri", vitri);
                                    bun.putSerializable("DoiTuong", alarm);
                                    intent.putExtra("extra", bun);
                                    sendBroadcast(intent);
                                    dialog.cancel();

                                } else {
                                    edt.setText("");
                                    Toast.makeText(CancelAlarmActivity.this, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        btnHuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        dialog.show();

                    }
                }
                bool = 1;
            }
        });
    }
}