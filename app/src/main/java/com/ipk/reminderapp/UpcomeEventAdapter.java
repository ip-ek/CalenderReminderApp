package com.ipk.reminderapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class UpcomeEventAdapter extends RecyclerView.Adapter<UpcomeEventAdapter.MyViewHolder> {
    private Context context;    //başka sınıflara geçme vs için kullanılır. MainActivity.this gibi
    private ArrayList<UpcomeEvent> eventArrayList;

    UpcomeEventDatabase db;

    public UpcomeEventAdapter(Context context, ArrayList<UpcomeEvent> eventArrayList) {

        //Dışarıdan veri almaya yarar. Context, Activity özelliklerine sahip olmak için kullanılır
        this.context = context;
      //  Log.d("takip", "adapterda: "+eventArrayList.get(0).getLabel());
        this.eventArrayList = eventArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout infoLayout/*, eventOptions*/;
        ImageView eventColor, eventOptions;
        TextView eventType, eventLabel, eventDate, eventTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            infoLayout= itemView.findViewById(R.id.card_event_layout);
            eventColor= itemView.findViewById(R.id.card_event_color);
            eventOptions= itemView.findViewById(R.id.event_card_option);
            eventTime= itemView.findViewById(R.id.card_upcome_time);
            eventType=itemView.findViewById(R.id.card_upcome_type);
            eventLabel=itemView.findViewById(R.id.card_upcome_label);
            eventDate=itemView.findViewById(R.id.card_upcome_date);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //oluşturulan class ile birlikte CardView'ın Adaptera bağlanarak tanıtılması
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_upcoming,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //Görsel nesnelerin satır satır doldurulması ve yapılacak işlemler burada
        final UpcomeEvent event=eventArrayList.get(position); //bu final olmazsa inner erişemiyor. işlem yapılan card
        switch(event.getType()){
            case 0:
                holder.eventType.setText("Etkinlik");
                holder.eventColor.setBackgroundColor(context.getColor(R.color.event));
                /*DrawableCompat.setTint(
                        DrawableCompat.wrap(holder.eventColor.getDrawable()),
                        ContextCompat.getColor(context, R.color.meeting)
                );*/
                break;
            case 1:
                holder.eventType.setText("Toplantı");
                holder.eventColor.setBackgroundColor(context.getColor(R.color.meeting));
                break;
            case 2:
                holder.eventType.setText("Doğum Günü");
                holder.eventColor.setBackgroundColor(context.getColor(R.color.bday));
                break;
            case 3:
                holder.eventType.setText("Yıldönümü");
                holder.eventColor.setBackgroundColor(context.getColor(R.color.anniversary));
                break;
        }

        holder.eventLabel.setText(event.getLabel());
        holder.eventDate.setText(event.getStartDate());
        holder.eventTime.setText(event.getStartTime());

        //holder.   //onclickler falan burda
        //holder.eventDate.setText("dsakm");
        holder.eventOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context, holder.eventOptions);
                popupMenu.getMenuInflater().inflate(R.menu.upcome_card_menu,popupMenu.getMenu()); //bağladık
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.upcome_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Uyarı!");
                                builder.setMessage(event.getLabel()+ " - ve tekrar eden etkinlikler(varsa) silinsin mi?");
                                builder.setCancelable(false);

                                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //recycler'dan siliyor
                                        eventArrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,eventArrayList.size());
                                        Toast.makeText(context, event.getLabel()+" silindi", Toast.LENGTH_SHORT).show();

                                        db = new UpcomeEventDatabase(context);
                                        new UpcomeEventDao().deleteEvent(db, event.getEventID(), context);
                                        //TODO: diğerleri de silinmeli
                                    }
                                });

                                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create().show();

                                return true;
                            case R.id.upcome_send:
                                String eventMessage = event.toString();
                                try{
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, eventMessage);
                                    sendIntent.setType("text/plain");
                                    context.startActivity(sendIntent);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EventActivity.class);
                //eventID 0dan büyüktür
                if(event.getParent()==0){
                    intent.putExtra("eventID", event.getEventID());
                    intent.putExtra("type", event.getType());
                    intent.putExtra("label", event.getLabel());
                    intent.putExtra("content", event.getContent());
                    intent.putExtra("startDate", event.getStartDate());
                    intent.putExtra("startTime", event.getStartTime());
                    intent.putExtra("endDate", event.getEndDate());
                    intent.putExtra("endTime", event.getEndTime());
                    intent.putExtra("remindTime", event.getRemindTime());
                    intent.putExtra("enventFreq", event.getEnventFreq());
                    intent.putExtra("address", event.getAddress());
                    intent.putExtra("eventParent", event.getParent());
                    intent.putExtra("counter", event.getCounter());
                    intent.putExtra("alarm", event.getAlarm());
                }else{
                    db = new UpcomeEventDatabase(context);
                    UpcomeEvent parentEvent=new UpcomeEventDao().getEvent(db, event.getParent());
                    intent.putExtra("eventID", parentEvent.getEventID());
                    intent.putExtra("type", parentEvent.getType());
                    intent.putExtra("label", parentEvent.getLabel());
                    intent.putExtra("content", parentEvent.getContent());
                    intent.putExtra("startDate", parentEvent.getStartDate());
                    intent.putExtra("startTime", parentEvent.getStartTime());
                    intent.putExtra("endDate", parentEvent.getEndDate());
                    intent.putExtra("endTime", parentEvent.getEndTime());
                    intent.putExtra("remindTime", parentEvent.getRemindTime());
                    intent.putExtra("enventFreq", parentEvent.getEnventFreq());
                    intent.putExtra("address", parentEvent.getAddress());
                    intent.putExtra("eventParent", parentEvent.getParent());
                    intent.putExtra("counter", parentEvent.getCounter());
                    intent.putExtra("alarm",parentEvent.getAlarm());
                }
                Toast.makeText(context, "Tekrar eden etkinlik seçildi",Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //kaç veri ekleneceği
        return eventArrayList.size();
    }
}

