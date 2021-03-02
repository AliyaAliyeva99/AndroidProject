package com.example.webservis_16102020;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MiqraPlusClients extends AppCompatActivity {
    Context context;
    Menu menuTitle;
    Activity activity_;
    MenuItem menuItemreturn_, menuItemclient, menuItemsearch, menuItemGPS, menuItemfiltr;
    SwipeMenuListView listView;
    ArrayList<ModelMPClients> list, listRut;
    MPClientAdaptr adp;
    TextView toplamBorc, toplamAlacaq, toplamQaliq;
    public static String _result;
    float borcToplam = 0;
    float alacakToplam = 0;
    float bakiyeToplam = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miqra_plus_clients);
        this.setTitle("");

        context = this;
        listView = findViewById(R.id.musterilerListi);
        toplamBorc = findViewById(R.id.toplamBorc);
        toplamAlacaq = findViewById(R.id.toplamAlacaq);
        toplamQaliq = findViewById(R.id.toplamQaliq);
        createswipemenu();
        swipemenuclick();


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,"LongClick",Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        AsyncMpMusterList asyncMpMusterListclients = new AsyncMpMusterList();
        HashMap<String, String> hsparams = new HashMap<String, String>();
        hsparams.put("connecting", "your connection string");
        hsparams.put("exp_no", "20006");
        hsparams.put("erpconnecting", "your erp connection string");
        asyncMpMusterListclients.execute(hsparams);
    }

    public class AsyncMpMusterList extends AsyncTask<HashMap<String, String>, Void, Void> {
        String TAG = "Master list zamani listden satilan mallarin rengi deishmesi";
        HashMap<String, String> hsParam = new HashMap<>();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Gozleyin");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            Log.i(TAG, "doInBackground");
            _result = "";
            String url = "your url";
            _result = GetWebService(params[0], "GetAllInfo", url);
            // _resultUpdate = GetWebService(params[0], "MpTuncayUpdate", url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            super.onPostExecute(result);
            if (_result.startsWith("{\"clients\":[{")) {
                try {
                    // Toast.makeText(activity, _result, Toast.LENGTH_LONG).show();
                    JSONObject jsonObjectAll = new JSONObject(_result);
                    JSONArray jsonArray = jsonObjectAll.getJSONArray("clients");
                    JSONObject jsonObject;
                   // float borcToplam = 0;
                   // float alacakToplam = 0;
                   // float bakiyeToplam = 0;
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        ModelMPClients m1 = new ModelMPClients();
                        m1.setClientCode(jsonObject.getString("Musteri_Kodu"));
                        m1.setClientName(jsonObject.getString("Musteri_Adi"));
                        m1.setClientBakiye(jsonObject.getString("Musteri_Borcu"));
                        m1.setRiskLimiti(jsonObject.getString("Risk_Limiti"));
                        m1.setGps_x(jsonObject.getString("Location_x"));
                        m1.setGps_y(jsonObject.getString("Location_y"));
                        m1.setPriceType(jsonObject.getString("Qiymet_Tipi"));
                        m1.setSlsmanCode(jsonObject.getString("Temsilci_Kodu"));
                        m1.setOdemeLimiti("");
                        m1.setClientOfficialName(jsonObject.getString("Aciqlama2"));
                        m1.setClientVede("");
                        m1.setClientType(jsonObject.getString("Musteri_Tipi"));
                        m1.setEndirimFaizi(jsonObject.getString("Endirim_Faizi"));
                        m1.setRut(jsonObject.getString("Rut"));
                        float bakiye = Float.parseFloat(jsonObject.getString("Musteri_Borcu"));
                        if (bakiye > 0) {
                            borcToplam = borcToplam + bakiye;
                        } else {
                            alacakToplam = alacakToplam + bakiye;
                        }
                        bakiyeToplam = bakiyeToplam + bakiye;
                        list.add(m1);
                    }
                    adp = new MPClientAdaptr(list, getApplicationContext());
                    listView.setAdapter(adp);
                    toplamBorc.setText(String.valueOf(borcToplam));
                    toplamAlacaq.setText(String.valueOf(alacakToplam));
                    toplamQaliq.setText(String.valueOf(bakiyeToplam));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity_, "clinet info err", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }

    public String GetJsonValueForMusterList() {

        final JSONObject jsonObjectall = new JSONObject();
        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            //String[][] _expCode = sqlLiteMethod.ExecuteSelect("SELECT exp_code,FIRM_ERP_CON,firm_conn FROM Expeditor");
            // String client_group = sqlLiteMethod.ExecuteScalar("select group_ from Clients where kodu ='" + _clientCode + "' limit 1");

            jsonObject = new JSONObject();
            jsonObject.put("exp_no", "");
            jsonObject.put("erp_connection", "");
            jsonObject.put("connection", "");
            jsonObject.put("client_code", "");
            jsonObject.put("client_type", "");
            jsonObject.put("client_group", "");
            jsonArray.put(jsonObject);
            jsonObjectall.put("fa_basliq", jsonArray);

        } catch (final Exception ex) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        return jsonObjectall.toString();
    }

    public String GetWebService(HashMap<String, String> hashMapParams, String _methodName, String Url) {

        Object response = "error";
        try {
            String _targetNamespace = "http://threepin.org/";
            String _soapAction = "http://threepin.org/" + _methodName;
            SoapObject request = new SoapObject(_targetNamespace, _methodName);
            if (hashMapParams != null) {
                Set set = hashMapParams.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry) iterator.next();
                    request.addProperty((String) mentry.getKey(), mentry.getValue());
                }
            } else {
                Toast.makeText(activity_, "clinet info err", Toast.LENGTH_LONG).show();
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.bodyOut = request;
            HttpTransportSE httpTransport = new HttpTransportSE(Url, 60000);// EVVEL 20000

            httpTransport.call(_soapAction, envelope);

            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.getMessage();
            if (Url.equals(""))
                response = GetWebService(hashMapParams, _methodName, "your url");

        }
        return response != null ? response.toString() : "error";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menuTitle = menu;
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.musteriler_menu, menu);
        m_CreateCounterTitleMenu(); // menu title create



        menuItemfiltr.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LayoutInflater inflater1 = getLayoutInflater();
                View view = inflater1.inflate(R.layout.layout_alertdialog, null);

                Button btnRut = view.findViewById(R.id.btnRut);
                Button btnAllClients=view.findViewById(R.id.btnAllClients);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(view);
                alert.setCancelable(true);
                final AlertDialog dialog = alert.create();

                dialog.show();
                btnRut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date now = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(now);
                        float borcToplamRut = 0;
                        float alacakToplamRut = 0;
                        float bakiyeToplamRut = 0;
                        String bugun = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
                        listRut = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getRut().equals(bugun)) {
                                ModelMPClients m1 = new ModelMPClients();
                                m1.setClientCode(list.get(i).getClientCode());
                                m1.setClientName(list.get(i).getClientName());
                                m1.setClientBakiye(list.get(i).getClientBakiye());
                                m1.setRiskLimiti(list.get(i).getRiskLimiti());
                                m1.setGps_x(list.get(i).getGps_x());
                                m1.setGps_y(list.get(i).getGps_y());
                                m1.setPriceType(list.get(i).getPriceType());
                                m1.setSlsmanCode(list.get(i).getSlsmanCode());
                                m1.setOdemeLimiti(list.get(i).getOdemeLimiti());
                                m1.setClientOfficialName(list.get(i).getClientOfficialName());
                                m1.setClientVede(list.get(i).getClientVede());
                                m1.setClientType(list.get(i).getClientType());
                                m1.setEndirimFaizi(list.get(i).getEndirimFaizi());
                                float bakiye = Float.parseFloat(list.get(i).getClientBakiye());
                                if (bakiye > 0) {
                                    borcToplamRut = borcToplamRut + bakiye;
                                } else {
                                    alacakToplamRut = alacakToplamRut + bakiye;
                                }
                                bakiyeToplamRut = bakiyeToplamRut + bakiye;
                                listRut.add(m1);
                            }
                        }
                        adp = new MPClientAdaptr(listRut, getApplicationContext());
                        listView.setAdapter(adp);
                        toplamBorc.setText(String.valueOf(borcToplamRut));
                        toplamAlacaq.setText(String.valueOf(alacakToplamRut));
                        toplamQaliq.setText(String.valueOf(bakiyeToplamRut));
                        dialog.cancel();
                    }
                });
                btnAllClients.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adp = new MPClientAdaptr(list, getApplicationContext());
                        listView.setAdapter(adp);
                        toplamBorc.setText(String.valueOf(borcToplam));
                        toplamAlacaq.setText(String.valueOf(alacakToplam));
                        toplamQaliq.setText(String.valueOf(bakiyeToplam));
                        dialog.cancel();
                    }
                });

                return false;
            }
        });
        menuItemGPS.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Uri gmmIntentUri = Uri.parse("https://goo.gl/maps/dYcrFCUjspxroBqD8");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return false;
            }
        });
        return true;
    }

    public void m_CreateCounterTitleMenu() {
       // menuItemreturn_ = menuTitle.findItem(R.id.return_);
        menuItemclient = menuTitle.findItem(R.id.client);
        menuItemsearch = menuTitle.findItem(R.id.search);
        menuItemGPS = menuTitle.findItem(R.id.GPS);
        menuItemfiltr = menuTitle.findItem(R.id.filtr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title menu click
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.open:
                // sened tesdiqlenmesi
                Toast.makeText(context, "menu", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MiqraPlusClients.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void createswipemenu() {
        // Yeni  Menu Creator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // View növündən asılı olaraq  menyu
                switch (menu.getViewType()) {
                    case 0:
                        createMenu0(menu);
                        break;
                }
            }

            private void createMenu0(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(context.getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                item1.setWidth(dp2px(65));
                item1.setTitleSize(15);
                item1.setTitleColor(Color.BLACK);
                // item1.setBackground(R.drawable.ic_baseline_gps_fixed_24);
                item1.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_gps_fixed_24, null));
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(context.getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                item2.setWidth(dp2px(65));
                item2.setTitleSize(15);
                item2.setTitleColor(Color.BLACK);
                // item1.setBackground(R.drawable.style_supervizor_menu);ic_cancel_white_36dp
                item2.setIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                menu.addMenuItem(item2);
                SwipeMenuItem item3 = new SwipeMenuItem(context.getApplicationContext());
                item3.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                item3.setWidth(dp2px(65));
                item3.setTitleSize(15);
                item3.setTitleColor(Color.BLACK);
                // item1.setBackground(R.drawable.style_supervizor_menu);ic_cancel_white_36dp
                item3.setIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                menu.addMenuItem(item3);
                SwipeMenuItem item4 = new SwipeMenuItem(context.getApplicationContext());
                item4.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                item4.setWidth(dp2px(65));
                item4.setTitleSize(15);
                item4.setTitleColor(Color.BLACK);
                // item1.setBackground(R.drawable.style_supervizor_menu);ic_cancel_white_36dp
                item4.setIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                menu.addMenuItem(item4);

            }
        };
        // set creator
        listView.setMenuCreator(creator);
        // Top formasinda baglanma
        listView.setCloseInterpolator(new BounceInterpolator());
        // set SwipeListener
        // Top formasinda baglanma
        listView.setCloseInterpolator(new BounceInterpolator());
    }

    public void swipemenuclick() {

        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeEnd(int position) {
                // Toast.makeText(context, "end", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0: // sifarish tesdiqle
                        // index e gore bilirsen ki hansi swipe buttona basilib
                        Toast.makeText(context, " Kodlu Musteri Siyahidan Silindi", Toast.LENGTH_LONG).show();
                        break;

                    case 1: // sifarish tesdiqle
                        // index e gore bilirsen ki hansi swipe buttona basilib

                        break;
                }
                return false;
            }


        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
