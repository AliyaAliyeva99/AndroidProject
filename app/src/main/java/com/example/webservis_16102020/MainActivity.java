package com.example.webservis_16102020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {
    Button btn, btnSend,btnShow;
    public static TextView txt, txtClCode;
    Context context;
    Activity activity;
    ListView listView;
    List<Model> list;
    MesajAdapter adp;
    SqlLiteMethod sqlLiteMethod;
    public static EditText editClDefn, editSpecode5;
    public static String _resultUpdate = "";
    Menu menuTitle;
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqlLiteMethod = new SqlLiteMethod(context);
        context = this;
        activity = this;
        btn = findViewById(R.id.btn);
        txt = findViewById(R.id.txt);
        txtClCode = findViewById(R.id.musteriKodu);
        listView = findViewById(R.id.musteriler);
        editClDefn = findViewById(R.id.editAd);
        editSpecode5 = findViewById(R.id.editSpecode5);
        btnSend = findViewById(R.id.btnSend);
        btnShow=findViewById(R.id.btnShow);
        btnclick();

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
    public class AsyncMpMusterListSaledGoster extends AsyncTask<HashMap<String, String>, Void, Void> {
        String TAG = "Master list zamani listden satilan mallarin rengi deishmesi";
        HashMap<String, String> hsParam = new HashMap<>();
        String _result = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Gozleyin");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            Log.i(TAG, "doInBackground");
            String url = "your url";
            _result = GetWebService(params[0], "MpTuncay", url);
           // _resultUpdate = GetWebService(params[0], "MpTuncayUpdate", url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            super.onPostExecute(result);
            if (_result.startsWith("{\"clients\":[{")) {
                try {
                    JSONObject jsonObjectAll = new JSONObject(_result);
                    JSONArray jsonArray = jsonObjectAll.getJSONArray("clients");
                    JSONObject jsonObject;
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Model m1 = new Model();
                        m1.setAd(jsonObject.getString("DEFINITION_"));
                        m1.setKod(jsonObject.getString("CODE"));
                        m1.setTemsilci(jsonObject.getString("SPECODE5"));
                        list.add(m1);
                    }
                    adp = new MesajAdapter(list, getApplicationContext());
                    listView.setAdapter(adp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(activity, "clinet info err", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }
    public class AsyncMpMusterListSaledGonder extends AsyncTask<HashMap<String, String>, Void, Void> {
        String TAG = "Master list zamani listden satilan mallarin rengi deishmesi";
        HashMap<String, String> hsParam = new HashMap<>();
        String _result = "";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Gozleyin");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(HashMap<String, String>... params) {
            Log.i(TAG, "doInBackground");
            String url = "your url";
            //_result = GetWebService(params[0], "MpTuncay", url);
            _resultUpdate = GetWebService(params[0], "MpTuncayUpdate", url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            super.onPostExecute(result);
            if (_resultUpdate.equals("okey")) {
               sqlite s = new sqlite(context);
               s.insertData(txtClCode.getText().toString(), editClDefn.getText().toString(), editSpecode5.getText().toString());
                txtClCode.setText("");
                editClDefn.setText("");
                editSpecode5.setText("");
                Toast.makeText(context, "Deyisiklik Edilsi Siyahini Yenileyin", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Deyisiklik Edilmesi Zamani Xeta Bas Verdi", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }
    public void setText(String ClCode, String clAd, String clSpecode5) {
        txtClCode.setText(ClCode);
        editClDefn.setText(clAd);
        editSpecode5.setText(clSpecode5);
    }
    public void btnclick() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("Sistemdeki Musteriler");
                AsyncMpMusterListSaledGoster asyncMpMusterListSaled = new AsyncMpMusterListSaledGoster();
                HashMap<String, String> hsparams = new HashMap<String, String>();
                asyncMpMusterListSaled.execute(hsparams);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtClCode.getText().toString() != "") {
                    AsyncMpMusterListSaledGonder asyncMpMusterListSaled = new AsyncMpMusterListSaledGonder();
                    HashMap<String, String> hsparams = new HashMap<String, String>();
                    hsparams.put("clCode", txtClCode.getText().toString());
                    hsparams.put("clDefn", editClDefn.getText().toString());
                    hsparams.put("clSpecode5", editSpecode5.getText().toString());
                    asyncMpMusterListSaled.execute(hsparams);
                }else{
                    Toast.makeText(context,"Zehmet Olmasa Musteri Secin",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DeyisenMusteriler.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menuTitle = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        m_CreateCounterTitleMenu(); // menu title create
        return true;
    }
    public void m_CreateCounterTitleMenu() {
        menuItem = menuTitle.findItem(R.id.open);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title menu click
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.open:
                // sened tesdiqlenmesi
                Toast.makeText(context,"menu",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),MiqraPlusClients.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}