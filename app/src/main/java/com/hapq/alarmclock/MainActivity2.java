package com.hapq.alarmclock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

    TextView txtNgay, txtlabel, txtcontent, txtTat, txtRepeat;
    Button btnCF, btnCancel;
    TimePicker timePicker;
    ImageView imgDate;
    RelativeLayout rLlabel, rLcontent;
    Alarm_Infor alarm;
    int hour, minutes, position;
    String mk;

    CheckBox cb2, cb3, cb4, cb5, cb6, cb7, cb8;
    int[] b = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // ánh xạ
        Anhxa();

        timePicker.setIs24HourView(false);

        // intent nhận từ phía Main Activity
        Intent intent_nhan = getIntent();
        Bundle bundle2 = intent_nhan.getBundleExtra("DULIEU");

        alarm = (Alarm_Infor) bundle2.getSerializable("DOITUONG");
        /*hour2 = bundle2.getInt("HOUR");
        minute2 = bundle2.getInt("MINUTE");
        date2 = bundle2.getString("DATE");*/

        // Xử lý dữ liệu
        position = bundle2.getInt("POSITION");
        hour = alarm.getHour();
        minutes = alarm.getMinute();

        txtlabel.setText(alarm.getLabel());
        if(alarm.getLabel().length() >= 15){
            String r = alarm.getLabel().substring(0, 15);
            r += "...";
            txtlabel.setText(r);
        }
        txtcontent.setText(alarm.getContent());
        if(alarm.getContent().length() >= 15){
            String r2 = alarm.getContent().substring(0, 15);
            r2 += "...";
            txtcontent.setText(r2);
        }

        mk = alarm.getPassword();
        if(mk.equals("")) txtTat.setText("Tắt thông thường");
        else{
            txtTat.setText("Dùng mật khẩu");
        }

        txtNgay.setText(alarm.getDate());
        int gio = alarm.getHour();
        //if(gio > 12) gio -= 12;
        timePicker.setHour(gio);
        timePicker.setMinute(alarm.getMinute());

        //thiết lập checkBox kèm Repeat
        String u = alarm.getRepeat();
        if(u.equals("Hàng ngày") || u.equals("Hàng tháng") || u.equals("Một lần")){
            txtRepeat.setText(u);
            for (int i = 2; i <= 8; i++) {
                b[i] = 0;
            }
        }
        else{
            txtRepeat.setText("Hàng tuần");
            int size = u.length();
            for(int i = 3; i <= size; i+=3){
                switch(u.substring(i-3, i-1)){
                    case "Mo":
                        b[2] = 1;
                        cb2.setChecked(true);
                        break;
                    case "Tu":
                        b[3] = 1;
                        cb3.setChecked(true);
                        break;
                    case "We":
                        b[4] = 1;
                        cb4.setChecked(true);
                        break;
                    case "Th":
                        b[5] = 1;
                        cb5.setChecked(true);
                        break;
                    case "Fr":
                        b[6] = 1;
                        cb6.setChecked(true);
                        break;
                    case "Sa":
                        b[7] = 1;
                        cb7.setChecked(true);
                        break;
                    case "Su":
                        b[8] = 1;
                        cb8.setChecked(true);
                        break;
                }
            }
        }

        // Xử lý sự kiện bắt đầu từ đây
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[2] = 1;
                }
                else b[2] = 0;
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[3] = 1;
                }
                else b[3] = 0;
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[4] = 1;
                }
                else b[4] = 0;
            }
        });
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[5] = 1;
                }
                else b[5] = 0;
            }
        });
        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[6] = 1;
                }
                else b[6] = 0;
            }
        });
        cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[7] = 1;
                }
                else b[7] = 0;
            }
        });
        cb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    b[8] = 1;
                }
                else b[8] = 0;
            }
        });

        rLlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit(0, alarm.getLabel()); // Label
            }
        });

        rLcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit(1, alarm.getContent()); // content
            }
        });

        txtRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenuRepeat();
            }
        });

        txtTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenuCancel();
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;
            }
        });

        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
            }
        });

        btnCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = txtNgay.getText().toString();

                Intent intent = new Intent();

                Bundle bundle = new Bundle();
                //Update du lieu
                    alarm.setDate(date);
                    alarm.setTime(hour, minutes);
                    alarm.setPassword(mk);

                    String u = txtRepeat.getText().toString();
                    if(u.equals("Hàng tuần")){
                        alarm.setRepeat(Solve());
                    }

                bundle.putSerializable("DOITUONG", alarm);
                bundle.putInt("POSITION", position);
                intent.putExtra("DATA", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                /*Bundle bundle = new Bundle();
                bundle.putSerializable("DOITUONG", alarm);
                bundle.putInt("POSITION", position);
                intent.putExtra("DATA", bundle);*/
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void Anhxa(){
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCF = (Button) findViewById(R.id.buttonConfirm);
        txtNgay = (TextView) findViewById(R.id.textDate);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        imgDate = (ImageView) findViewById(R.id.ImgDate);
        txtlabel = (TextView) findViewById(R.id.textlabel);
        txtcontent = (TextView) findViewById(R.id.textContent);
        txtTat = (TextView) findViewById(R.id.textTat);
        txtRepeat = (TextView) findViewById(R.id.textRepeat);
        rLlabel = (RelativeLayout) findViewById(R.id.RLayoutLabel);
        rLcontent = (RelativeLayout) findViewById(R.id.RLayoutContent);

        cb2 = (CheckBox) findViewById(R.id.checkbox2);
        cb3 = (CheckBox) findViewById(R.id.checkbox3);
        cb4 = (CheckBox) findViewById(R.id.checkbox4);
        cb5 = (CheckBox) findViewById(R.id.checkbox5);
        cb6 = (CheckBox) findViewById(R.id.checkbox6);
        cb7 = (CheckBox) findViewById(R.id.checkbox7);
        cb8 = (CheckBox) findViewById(R.id.checkbox8);

    }

    private void DialogPass() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password_dialog);
        dialog.setCanceledOnTouchOutside(true);
        final EditText edtPass = (EditText) dialog.findViewById(R.id.editPassword);
        final EditText edtPassAgain = (EditText) dialog.findViewById(R.id.editPasswordAgain);
        Button btnDongY = (Button) dialog.findViewById(R.id.buttonYes);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonNo);
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = edtPass.getText().toString();
                String pass2 = edtPassAgain.getText().toString();
                if(pass1.equals(pass2)){
                     mk = pass1;
                     Toast.makeText(MainActivity2.this, "Tạo mật khẩu thành công", Toast.LENGTH_SHORT).show();
                     dialog.cancel();
                }
                else{
                    edtPass.setText("");
                    edtPassAgain.setText("");
                    Toast.makeText(MainActivity2.this, "Tạo mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTat.setText("Tắt thông thường");
                mk = "";
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void DialogEdit(int id, String s){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_content_dialog);
        dialog.setCanceledOnTouchOutside(false);
        EditText edt = (EditText) dialog.findViewById(R.id.editTextTextPersonName);
        Button btnDongY = (Button) dialog.findViewById(R.id.buttonDongY);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonBo);

        edt.setText(s);

        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edt.getText().toString().trim();
                if(id == 0)
                {
                    alarm.setLabel(content);
                    txtlabel.setText(content);
                    String s;
                    if(content.length() >= 15){
                        s = content.substring(0, 15);
                        s += "...";
                        txtlabel.setText(s);
                    }

                }
                if(id == 1) {
                    alarm.setContent(content);
                    txtcontent.setText(content);
                    String s2;
                    if(content.length() >= 15){
                        s2 = content.substring(0, 15);
                        s2 += "...";
                        txtcontent.setText(s2);
                    }
                }
                dialog.cancel();
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

    private void ChonNgay(){
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    private void ShowMenuRepeat(){
        PopupMenu popupMenu = new PopupMenu(this, txtRepeat);
        popupMenu.getMenuInflater().inflate(R.menu.menu_repeat, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuEveryDay:
                        txtRepeat.setText("Hàng ngày");
                        alarm.setRepeat("Hàng ngày");
                        break;
                    case R.id.menuOneTime:
                        txtRepeat.setText("Một lần");
                        alarm.setRepeat("Một lần");
                        break;
                    case R.id.menuEveryMonth:
                        txtRepeat.setText("Hàng tháng");
                        alarm.setRepeat("Hàng tháng");
                        break;
                    case R.id.menuEveryWeek:
                        txtRepeat.setText("Hàng tuần");
                        String check = Solve();
                        alarm.setRepeat(check);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private String Solve(){
        String ck = "";
        for(int i=2; i<=8; i++){
            if(b[i] == 1){
                switch(i){
                    case 2:
                        ck += "Mo ";
                        break;
                    case 3:
                        ck += "Tu ";
                        break;
                    case 4:
                        ck += "We ";
                        break;
                    case 5:
                        ck += "Th ";
                        break;
                    case 6:
                        ck += "Fr ";
                        break;
                    case 7:
                        ck += "Sa ";
                        break;
                    case 8:
                        ck += "Su ";
                        break;
                }

            }
        }
        return ck;
    }

    private void ShowMenuCancel(){
        PopupMenu popupMenu = new PopupMenu(this, txtTat);
        popupMenu.getMenuInflater().inflate(R.menu.menu_cancel_alarm, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuCasual:
                        txtTat.setText("Tắt thông thường");
                        mk = "";
                        break;
                    case R.id.menuPass:
                        txtTat.setText("Dùng mật khẩu");
                        DialogPass();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}