package com.example.webservis_16102020;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MPClientAdaptr extends BaseAdapter {
    ArrayList<ModelMPClients> list;
    Context context;
    private LayoutInflater layoutInflater;

    public MPClientAdaptr(ArrayList<ModelMPClients> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;//final
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_musteriler, null);
            holder = new ViewHolder();
            holder.gps_x = convertView.findViewById(R.id.gps_x);
            holder.gps_y=convertView.findViewById(R.id.gps_y);
            holder.clientCode=convertView.findViewById(R.id.clientCode);
            holder.clientName=convertView.findViewById(R.id.clientName);
            holder.clientOfficialName=convertView.findViewById(R.id.clientOfficialName);
            holder.clientBakiye=convertView.findViewById(R.id.clientBakiye);
            holder.clientVede=convertView.findViewById(R.id.clientVede);
            holder.odemeLimiti=convertView.findViewById(R.id.odemeLimiti);
            holder.riskLimiti=convertView.findViewById(R.id.riskLimiti);
            holder.endirimFaizi=convertView.findViewById(R.id.endirimFaizi);
            holder.clientType=convertView.findViewById(R.id.clientType);
            holder.priceType=convertView.findViewById(R.id.priceType);
            holder.slsmanCode=convertView.findViewById(R.id.slsmanCode);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.gps_x.setText(list.get(position).getGps_x());
        holder.gps_y.setText(list.get(position).getGps_y());
        holder.clientCode.setText(list.get(position).getClientCode());
        holder.clientName.setText(list.get(position).getClientName());
        holder.clientOfficialName.setText(list.get(position).getClientOfficialName());
        holder.clientBakiye.setText(list.get(position).getClientBakiye());
        if (Float.parseFloat(list.get(position).getClientBakiye())>0){
            holder.clientBakiye.setTextColor(Color.parseColor("#186fdb"));
        }else if (Float.parseFloat(list.get(position).getClientBakiye())<0){
            holder.clientBakiye.setTextColor(Color.parseColor("#ff0000"));
        }else{
            holder.clientBakiye.setTextColor(Color.parseColor("#000000"));
        }
        holder.clientVede.setText(list.get(position).getClientVede());
        holder.odemeLimiti.setText(list.get(position).getOdemeLimiti());
        holder.riskLimiti.setText(list.get(position).getRiskLimiti());
        holder.endirimFaizi.setText(list.get(position).getEndirimFaizi());
        holder.clientType.setText(list.get(position).getClientType());
        holder.priceType.setText(list.get(position).getPriceType());
        holder.slsmanCode.setText(list.get(position).getSlsmanCode());
        return convertView;
    }

    class ViewHolder {
        TextView gps_x,gps_y,clientCode,clientName,clientOfficialName,clientBakiye,clientVede,odemeLimiti,riskLimiti;
        TextView endirimFaizi,clientType,priceType,slsmanCode;
    }
}
