package com.example.webservis_16102020;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MesajAdapter extends BaseAdapter {
    List<Model> list;
    Context context;
    Button btn;

    public MesajAdapter(List<Model> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View layout= LayoutInflater.from(context).inflate(R.layout.layout,parent,false);
        TextView txtKod=layout.findViewById(R.id.clKod);
        TextView txtAd=layout.findViewById(R.id.clDefn);
        TextView txtSpecode5=layout.findViewById(R.id.clSpecode5);
        Button btnEdit=layout.findViewById(R.id.add);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m =new MainActivity();
                m.setText(list.get(position).getKod(),list.get(position).getAd(),list.get(position).getTemsilci());
            }
        });
        txtKod.setText(list.get(position).getKod());
        txtAd.setText(list.get(position).getAd());
        txtSpecode5.setText(list.get(position).getTemsilci());
        return layout;
    }

}
