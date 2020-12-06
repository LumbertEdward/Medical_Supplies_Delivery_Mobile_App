package com.example.medicalsuppliesdelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalsuppliesdelivery.DataClasses.NotificationsClass;
import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.R;

import org.w3c.dom.Text;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<Orders> notificationsClassList;
    private Context context;
    private static final int FADE_DURATION = 1000;

    public NotificationAdapter(List<Orders> notificationsClassList, Context context) {
        this.notificationsClassList = notificationsClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(notificationsClassList.get(position).getNotification());
        holder.date.setText(notificationsClassList.get(position).getDate());
        setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return notificationsClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textView;
        private TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardNot);
            textView = (TextView) itemView.findViewById(R.id.textNot);
            date = (TextView) itemView.findViewById(R.id.dateNot);
        }
    }
    private void setScaleAnimation(View v){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(FADE_DURATION);
        v.startAnimation(scaleAnimation);
    }
}
