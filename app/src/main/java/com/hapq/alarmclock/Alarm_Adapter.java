package com.hapq.alarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Alarm_Adapter extends RecyclerView.Adapter<Alarm_Adapter.ViewHolder> {

    //Dữ liệu hiện thị là danh sách báo thức
    private ArrayList<Alarm_Infor> mAlarm;
    // Lưu Context để dễ dàng truy cập
    private Context mContext;
    private OnItemClickListener listener; // MainActivity

    public Alarm_Adapter(ArrayList<Alarm_Infor> _alarm, Context mContext, OnItemClickListener listener) {
        this.mAlarm = _alarm;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View alarmView = inflater.inflate(R.layout.alarm_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(alarmView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm_Infor alarm = mAlarm.get(position);

        holder.time.setText(alarm.getTime());
        holder.date.setText(alarm.getDate());
        if (mAlarm.get(position).getON() == true) holder.onoff_switch.setChecked(true);
        else holder.onoff_switch.setChecked(false);
        holder.repeat.setText(alarm.getRepeat());
        holder.title.setText(alarm.getLabel());
    }

    @Override
    public int getItemCount() {
        return mAlarm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView date;
        public Switch onoff_switch;
        public TextView repeat;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textViewTime);
            date = itemView.findViewById(R.id.textViewDate);
            onoff_switch = itemView.findViewById(R.id.switchOnOff);
            repeat = itemView.findViewById(R.id.textViewRepeat);
            title = itemView.findViewById(R.id.textViewNhande);

            onoff_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = ((Switch) v).isChecked();
                    listener.SwitchClick(isChecked, getAdapterPosition());
                    if(isChecked){
                        Toast.makeText(itemView.getContext(), "Bao thuc da duoc bat", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   listener.OnItemLongClick(getAdapterPosition());
                   return false;
                }
            });
        }
    }
}
