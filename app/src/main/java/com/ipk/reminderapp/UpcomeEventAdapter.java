package com.ipk.reminderapp;

import android.content.Context;
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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpcomeEventAdapter extends RecyclerView.Adapter<UpcomeEventAdapter.MyViewHolder> {
    private Context context;    //başka sınıflara geçme vs için kullanılır. MainActivity.this gibi
    private ArrayList<UpcomeEvent> eventArrayList;

    public UpcomeEventAdapter(Context context, ArrayList<UpcomeEvent> eventArrayList) {

        //Dışarıdan veri almaya yarar. Context, Activity özelliklerine sahip olmak için kullanılır
        this.context = context;
        Log.d("takip", "adapterda: "+eventArrayList.get(0).getLabel());
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //Görsel nesnelerin satır satır doldurulması ve yapılacak işlemler burada
        final UpcomeEvent event=eventArrayList.get(position); //bu final olmazsa inner erişemiyor. işlem yapılan card
        holder.eventType.setText(event.getType());
        holder.eventLabel.setText(event.getLabel());
        holder.eventDate.setText(event.getDate());
        holder.eventTime.setText(event.getTime());

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
                                eventArrayList.remove(event);
                                return true;
                            case R.id.upcome_send:
                                String eventMessage = "Etkinlik Türü: "+event.getType()+"\nEtkinlik Adı: "+event.getLabel()+"\nEtkinlik Başlangıç Tarih ve Saati: "+event.getDate()+" - "+event.getTime();
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
    }

    @Override
    public int getItemCount() {
        //kaç veri ekleneceği
        return eventArrayList.size();
    }
}

