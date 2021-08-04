package com.hapq.alarmclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    RecyclerView recyclerView;
    Alarm_Adapter adapter;
    ArrayList<Alarm_Infor> list_alarm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent intent_chinh;
    Database database;
    FloatingActionButton btnthem;
    int INT_REQUEST_CODE = 123;
    int bool = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recyclerView = (RecyclerView) findViewById(R.id.AlarmList);
        btnthem = (FloatingActionButton) findViewById(R.id.buttonThem);
        list_alarm = new ArrayList<Alarm_Infor>();
        database = new Database(this, "ghichu.sqlite", null, 1);

        // create table
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY, " +
                "Time VARCHAR(20), Hour INT, Minute INT, Date VARCHAR(20), ONOFF INT, " +
                "Repeat VARCHAR(20), Label VARCHAR(100), Content VARCHAR(100), Password VARCHAR(100))");
        //select data
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        while (dataCongViec.moveToNext()) {
            bool = 1;
            int id = dataCongViec.getInt(0);
            String time = dataCongViec.getString(1);
            int gio = dataCongViec.getInt(2);
            int phut = dataCongViec.getInt(3);
            String date = dataCongViec.getString(4);
            int onoff = dataCongViec.getInt(5);
            String repeat = dataCongViec.getString(6);
            String label = dataCongViec.getString(7);
            String content = dataCongViec.getString(8);
            String password = dataCongViec.getString(9);

            Alarm_Infor alarm = new Alarm_Infor();
            alarm.setTime(gio, phut);
            alarm.setDate(date);
            if (onoff == 0) alarm.setON(false);
            else alarm.setON(true);
            alarm.setRepeat(repeat);
            alarm.setLabel(label);
            alarm.setContent(content);
            alarm.setPassword(password);
            list_alarm.add(alarm);
        }
        if (bool == 0) {
            for (int i = 1; i <= 4; i++) {
                Alarm_Infor alarm = new Alarm_Infor();
                alarm.setTime(i, 0);
                alarm.setDate("29/07/2021");
                alarm.setON(false);
                alarm.setRepeat("Một lần");
                alarm.setLabel("");
                alarm.setContent("");
                alarm.setPassword("");

                list_alarm.add(alarm);
                // insert data
                database.QueryData("INSERT INTO CongViec VALUES(" + i + ",'" + list_alarm.get(i - 1).getTime() + "',"
                        + list_alarm.get(i - 1).getHour() + ","
                        + list_alarm.get(i - 1).getMinute() + ",'"
                        + list_alarm.get(i - 1).getDate() + "',"
                        + "0" + ",'"
                        + list_alarm.get(i - 1).getRepeat() + "','"
                        + list_alarm.get(i - 1).getLabel() + "','"
                        + list_alarm.get(i - 1).getContent() + "','"
                        + list_alarm.get(i - 1).getPassword() + "')"
                );
            }
        }

        adapter = new Alarm_Adapter(list_alarm, this, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

//        Intent intentHuy = getIntent();
//        int so = intentHuy.getIntExtra("HUYREP", -1);
//        Log.e("toi o main:", "" + so);
//        if (so != -1) {
//            list_alarm.get(so).setON(false);
//            database.QueryData("UPDATE CongViec SET ONOFF = 0 WHERE Id =" + (so + 1) + " ");
//            adapter.notifyDataSetChanged();
//            Log.e("toi o main:", "" + so);
//            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, so, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.cancel(pendingIntent);
//        } else {
//            Log.e("toi o main:", "" + so);
//        }

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intent_chinh = new Intent(MainActivity.this, AlarmReceiver.class);

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = list_alarm.size();
                Alarm_Infor alarm = new Alarm_Infor();
                Calendar calendar = Calendar.getInstance();
                int gio = calendar.get(Calendar.HOUR_OF_DAY);
                int phut = calendar.get(Calendar.MINUTE);
                Date date = new Date();
                SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
                String dateString  = sp.format(date);
                alarm.setTime(gio, phut);
                alarm.setDate(dateString);
                alarm.setON(false);
                alarm.setRepeat("Một lần");
                alarm.setLabel("");
                alarm.setContent("");
                alarm.setPassword("");

                list_alarm.add(alarm);
                // insert data
                database.QueryData("INSERT INTO CongViec VALUES(" + (i + 1) + ",'" + list_alarm.get(i).getTime() + "',"
                        + list_alarm.get(i).getHour() + ","
                        + list_alarm.get(i).getMinute() + ",'"
                        + list_alarm.get(i).getDate() + "',"
                        + "0" + ",'"
                        + list_alarm.get(i).getRepeat() + "','"
                        + list_alarm.get(i).getLabel() + "','"
                        + list_alarm.get(i).getContent() + "','"
                        + list_alarm.get(i).getPassword() + "')"
                );
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putInt("POSITION", i);
                bundle.putSerializable("DOITUONG", list_alarm.get(i));
                intent.putExtra("DULIEU", bundle);
                startActivityForResult(intent, INT_REQUEST_CODE);
            }
        });

    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        bundle.putSerializable("DOITUONG", list_alarm.get(position));
        /*bundle.putInt("HOUR", list_alarm.get(position).getHour());
        bundle.putInt("MINUTE", list_alarm.get(position).getMinute());
        bundle.putString("DATE", list_alarm.get(position).getDate());*/
        intent.putExtra("DULIEU", bundle);
        startActivityForResult(intent, INT_REQUEST_CODE);
    }

    @Override
    public void OnItemLongClick(int position) {
        XacnhanXoa(position);
    }

    private void XacnhanXoa(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông báo !");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có chắc muốn xóa báo thức này không ?");

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String u = list_alarm.get(position).getRepeat();
                if(u.equals("Hàng ngày") || u.equals("Một lần"))
                {
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, position, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                }
                else{
                    if(u.equals("Hàng tháng")){
                        for(int i = 1; i <= 12; i++) {
                            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position * 100 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                        }
                    }
                    else{
                        int[] b = new int[10];
                        for (int i = 2; i <= 8; i++) {
                            b[i] = 0;
                        }
                        int size = u.length();
                        for(int i = 3; i <= size; i+=3){
                            switch(u.substring(i-3, i-1)){
                                case "Mo":
                                    b[2] = 1;
                                    break;
                                case "Tu":
                                    b[3] = 1;
                                    break;
                                case "We":
                                    b[4] = 1;
                                    break;
                                case "Th":
                                    b[5] = 1;
                                    break;
                                case "Fr":
                                    b[6] = 1;
                                    break;
                                case "Sa":
                                    b[7] = 1;
                                    break;
                                case "Su":
                                    b[8] = 1;
                                    break;
                            }
                        }
                        for(int i = 2; i <= 8; i++) {
                            if(b[i] == 1)
                            {
                                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position*100 + 50 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.cancel(pendingIntent);
                            }
                        }
                    }
                }
                list_alarm.remove(position);
                database.QueryData("DELETE FROM CongViec WHERE Id = " + (position + 1));
                for (int i = position + 1; i <= list_alarm.size(); i++)
                    database.QueryData("UPDATE CongViec SET Id = " + i + " WHERE CongViec.Id =" + (i + 1) + " ");

                adapter.notifyDataSetChanged();
            }
        });

        alertDialog.show();
    }

    @Override
    public void SwitchClick(boolean bl, int position) {
        list_alarm.get(position).setON(bl);
        if (bl == true) {
            database.QueryData("UPDATE CongViec SET ONOFF = 1 WHERE Id =" + (position + 1) + " ");
        } else {
            database.QueryData("UPDATE CongViec SET ONOFF = 0 WHERE Id =" + (position + 1) + " ");
        }
        if (bl == true) {
            // Một lần
            if (list_alarm.get(position).getRepeat().equals("Một lần")) {
                Calendar calendar = Calendar.getInstance();
                String dateString = list_alarm.get(position).getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date();
                try {
                    date2 = df.parse(dateString);
                    calendar.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
                calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
                date2 = calendar.getTime();
                Date date1 = new Date();
                //Toast.makeText(this, "DATE: " + date2, Toast.LENGTH_LONG).show();
                if (date1.compareTo(date2) < 0) {
                    Bundle bun = new Bundle();
                    bun.putString("Mode", "on");
                    bun.putInt("Vitri", position);
                    bun.putSerializable("DoiTuong", list_alarm.get(position));
                    intent_chinh.putExtra("extra", bun);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, position, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                return;
            }

            //Hàng ngày
            if (list_alarm.get(position).getRepeat().equals("Hàng ngày")) {
                Calendar calendar = Calendar.getInstance();
                String dateString = list_alarm.get(position).getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date();
                try {
                    date2 = df.parse(dateString);
                    calendar.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
                calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
                date2 = calendar.getTime();
                Date date1 = new Date();
                if (date1.compareTo(date2) >= 0) {
                    calendar.add(Calendar.DATE, 1);
                }
                Log.e("Main","DATE: " + calendar.getTime());
                Bundle bun = new Bundle();
                bun.putString("Mode", "on");
                bun.putInt("Vitri", position);
                bun.putSerializable("DoiTuong", list_alarm.get(position));
                intent_chinh.putExtra("extra", bun);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, position, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                return;
            }

            //hàng tháng
            if (list_alarm.get(position).getRepeat().equals("Hàng tháng")) {
                Calendar calendar = Calendar.getInstance();
                String dateString = list_alarm.get(position).getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date();
                try {
                    date2 = df.parse(dateString);
                    calendar.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
                calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
                calendar.set(Calendar.MONTH, 0);
                Log.e("Main", "Time calendar  " + calendar.getTime());
                for (int i = 1; i <= 12; i++) {
                    date2 = calendar.getTime();
                    Date date1 = new Date();
                    int utc = 365 * 24 * 60 * 60;
                    Log.e("Main", "" + i + ") Time calendar  " + calendar.getTime());
                    Calendar calendar2 = calendar;
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.YEAR, 1);
                    }
                    Log.e("Main", "" + i + ") Time calendar2  " + calendar2.getTime());
                    Bundle bun = new Bundle();
                    bun.putString("Mode", "on");
                    bun.putInt("Vitri", position);
                    bun.putSerializable("DoiTuong", list_alarm.get(position));
                    intent_chinh.putExtra("extra", bun);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position * 100 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), utc * 1000L, pendingIntent);
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.YEAR, -1);
                    }
                    calendar.add(Calendar.MONTH, 1);
                }
                return;
            }

            //Hàng tuần
            int[] b = new int[10];
            for (int i = 2; i <= 8; i++) {
                b[i] = 0;
            }
            String u = list_alarm.get(position).getRepeat();
            int size = u.length();
            for(int i = 3; i <= size; i+=3){
                switch(u.substring(i-3, i-1)){
                    case "Mo":
                        b[2] = 1;
                        break;
                    case "Tu":
                        b[3] = 1;
                        break;
                    case "We":
                        b[4] = 1;
                        break;
                    case "Th":
                        b[5] = 1;
                        break;
                    case "Fr":
                        b[6] = 1;
                        break;
                    case "Sa":
                        b[7] = 1;
                        break;
                    case "Su":
                        b[8] = 1;
                        break;
                }
            }
            Calendar calendar = Calendar.getInstance();
            String dateString = list_alarm.get(position).getDate();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date2 = new Date();
            try {
                date2 = df.parse(dateString);
                calendar.setTime(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
            calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
            calendar.set(Calendar.DAY_OF_WEEK, 2);
            Log.e("Main Hàng tuần", "Time calendar  " + calendar.getTime());
            for(int i = 2; i<=8; i++){
                date2 = calendar.getTime();
                Date date1 = new Date();
                if(b[i] == 1){
                    Calendar calendar2 = calendar;
                    Log.e("Main", ""+ i +") Time calendar  " + calendar.getTime());
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.DATE,7);
                    }
                    Log.e("Main hàng tuần", ""+ i +") Time calendar2  " + calendar2.getTime());
                    Bundle bun = new Bundle();
                    bun.putString("Mode", "on");
                    bun.putInt("Vitri", position);
                    bun.putSerializable("DoiTuong", list_alarm.get(position));
                    intent_chinh.putExtra("extra", bun);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position*100 + 50 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 7*24*60*60*1000L, pendingIntent);
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.DATE,-7);
                    }
                }
                calendar.add(Calendar.DATE,1);
            }

        } else {
            String u = list_alarm.get(position).getRepeat();
            if(u.equals("Hàng ngày") || u.equals("Một lần"))
            {
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, position, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
            else{
                if(u.equals("Hàng tháng")){
                    for(int i = 1; i <= 12; i++) {
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position * 100 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                    }
                }
                else{
                    int[] b = new int[10];
                    for (int i = 2; i <= 8; i++) {
                        b[i] = 0;
                    }
                    int size = u.length();
                    for(int i = 3; i <= size; i+=3){
                        switch(u.substring(i-3, i-1)){
                            case "Mo":
                                b[2] = 1;
                                break;
                            case "Tu":
                                b[3] = 1;
                                break;
                            case "We":
                                b[4] = 1;
                                break;
                            case "Th":
                                b[5] = 1;
                                break;
                            case "Fr":
                                b[6] = 1;
                                break;
                            case "Sa":
                                b[7] = 1;
                                break;
                            case "Su":
                                b[8] = 1;
                                break;
                        }
                    }
                    for(int i = 2; i <= 8; i++) {
                        if(b[i] == 1)
                        {
                            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position*100 + 50 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getBundleExtra("DATA");
            int position = bundle.getInt("POSITION");
            Alarm_Infor alarm = (Alarm_Infor) bundle.getSerializable("DOITUONG");
            int hour = alarm.getHour();
            int minute = alarm.getMinute();
            String date = alarm.getDate();
            list_alarm.get(position).setDate(date);
            list_alarm.get(position).setTime(hour, minute);
            list_alarm.get(position).setON(true);
            list_alarm.get(position).setRepeat(alarm.getRepeat());
            list_alarm.get(position).setLabel(alarm.getLabel());
            list_alarm.get(position).setContent(alarm.getContent());
            list_alarm.get(position).setPassword(alarm.getPassword());

            // xu ly database
            database.QueryData("UPDATE CongViec SET Time = '" + list_alarm.get(position).getTime() + "' WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET Hour = " + list_alarm.get(position).getHour() + " WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET Minute = " + list_alarm.get(position).getMinute() + " WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET Date = '" + list_alarm.get(position).getDate() + "' WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET ONOFF = 1 WHERE ID = " + (position + 1));

            database.QueryData("UPDATE CongViec SET Repeat = '" + list_alarm.get(position).getRepeat() + "' WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET Label = '" + list_alarm.get(position).getLabel() + "' WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET Content = '" + list_alarm.get(position).getContent() + "' WHERE ID = " + (position + 1));
            database.QueryData("UPDATE CongViec SET Password = '" + list_alarm.get(position).getPassword() + "' WHERE ID = " + (position + 1));

            adapter.notifyDataSetChanged();

            Toast.makeText(MainActivity.this, "Label: " + alarm.getLabel() + "\n"
                    + "Content: " + alarm.getContent() + "\n"
                    + "Password: " + alarm.getPassword(), Toast.LENGTH_LONG).show();

            // Một lần
            if (list_alarm.get(position).getRepeat().equals("Một lần")) {
                Calendar calendar = Calendar.getInstance();
                String dateString = list_alarm.get(position).getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date();
                try {
                    date2 = df.parse(dateString);
                    calendar.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
                calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
                date2 = calendar.getTime();
                Date date1 = new Date();
                //Toast.makeText(this, "DATE: " + date2, Toast.LENGTH_LONG).show();
                if (date1.compareTo(date2) < 0) {
                    Bundle bun = new Bundle();
                    bun.putString("Mode", "on");
                    bun.putInt("Vitri", position);
                    bun.putSerializable("DoiTuong", list_alarm.get(position));
                    intent_chinh.putExtra("extra", bun);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, position, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                return;
            }

            //Hàng ngày
            if (list_alarm.get(position).getRepeat().equals("Hàng ngày")) {
                Calendar calendar = Calendar.getInstance();
                String dateString = list_alarm.get(position).getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date();
                try {
                    date2 = df.parse(dateString);
                    calendar.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
                calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
                date2 = calendar.getTime();
                Date date1 = new Date();
                //Toast.makeText(this, "DATE: " + date2, Toast.LENGTH_LONG).show();
                if (date1.compareTo(date2) >= 0) {
                    calendar.add(Calendar.DATE, 1);
                }
                Log.e("Main","DATE: " + calendar.getTime());
                Bundle bun = new Bundle();
                bun.putString("Mode", "on");
                bun.putInt("Vitri", position);
                bun.putSerializable("DoiTuong", list_alarm.get(position));
                intent_chinh.putExtra("extra", bun);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, position, intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                return;

            }
            if (list_alarm.get(position).getRepeat().equals("Hàng tháng")) {
                Calendar calendar = Calendar.getInstance();
                String dateString = list_alarm.get(position).getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date();
                try {
                    date2 = df.parse(dateString);
                    calendar.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
                calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
                calendar.set(Calendar.MONTH, 0);
                Log.e("Main", "Time calendar  " + calendar.getTime());
                for(int i = 1; i <= 12; i++){
                    date2 = calendar.getTime();
                    Date date1 = new Date();
                    int utc = 365*24*60*60;
                    Log.e("Main", ""+ i +") Time calendar  " + calendar.getTime());
                    Calendar calendar2 = calendar;
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.YEAR,1);
                    }
                    Log.e("Main", ""+ i +") Time calendar2  " + calendar2.getTime());
                    Bundle bun = new Bundle();
                    bun.putString("Mode", "on");
                    bun.putInt("Vitri", position);
                    bun.putSerializable("DoiTuong", list_alarm.get(position));
                    intent_chinh.putExtra("extra", bun);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position*100 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), utc*1000L, pendingIntent);
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.YEAR,-1);
                    }
                    calendar.add(Calendar.MONTH, 1);
                }
                return;
            }

            //Hàng tuần
            int[] b = new int[10];
            for (int i = 2; i <= 8; i++) {
                b[i] = 0;
            }
            String u = list_alarm.get(position).getRepeat();
            int size = u.length();
            for(int i = 3; i <= size; i+=3){
                switch(u.substring(i-3, i-1)){
                    case "Mo":
                        b[2] = 1;
                        break;
                    case "Tu":
                        b[3] = 1;
                        break;
                    case "We":
                        b[4] = 1;
                        break;
                    case "Th":
                        b[5] = 1;
                        break;
                    case "Fr":
                        b[6] = 1;
                        break;
                    case "Sa":
                        b[7] = 1;
                        break;
                    case "Su":
                        b[8] = 1;
                        break;
                }
            }
            Calendar calendar = Calendar.getInstance();
            String dateString = list_alarm.get(position).getDate();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date2 = new Date();
            try {
                date2 = df.parse(dateString);
                calendar.setTime(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.set(Calendar.HOUR_OF_DAY, list_alarm.get(position).getHour());
            calendar.set(Calendar.MINUTE, list_alarm.get(position).getMinute());
            calendar.set(Calendar.DAY_OF_WEEK, 2);
            Log.e("Main Hàng tuần", "Time calendar  " + calendar.getTime());
            for(int i = 2; i<=8; i++){
                date2 = calendar.getTime();
                Date date1 = new Date();
                if(b[i] == 1){
                    Calendar calendar2 = calendar;
                    Log.e("Main", ""+ i +") Time calendar  " + calendar.getTime());
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.DATE,7);
                    }
                    Log.e("Main hàng tuần", ""+ i +") Time calendar2  " + calendar2.getTime());
                    Bundle bun = new Bundle();
                    bun.putString("Mode", "on");
                    bun.putInt("Vitri", position);
                    bun.putSerializable("DoiTuong", list_alarm.get(position));
                    intent_chinh.putExtra("extra", bun);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (position*100 + 50 + i), intent_chinh, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 7*24*60*60*1000L, pendingIntent);
                    if (date1.compareTo(date2) >= 0) {
                        calendar2.add(Calendar.DATE,-7);
                    }
                }
                calendar.add(Calendar.DATE,1);
            }
        }
    }
}