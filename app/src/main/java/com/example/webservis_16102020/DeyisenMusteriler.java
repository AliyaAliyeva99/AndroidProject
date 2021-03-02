package com.example.webservis_16102020;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.res.ResourcesCompat;
        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.util.TypedValue;
        import android.view.animation.BounceInterpolator;
        import android.widget.Toast;

        import com.baoyz.swipemenulistview.SwipeMenu;
        import com.baoyz.swipemenulistview.SwipeMenuCreator;
        import com.baoyz.swipemenulistview.SwipeMenuItem;
        import com.baoyz.swipemenulistview.SwipeMenuListView;
        import java.util.ArrayList;

public class DeyisenMusteriler extends AppCompatActivity {
    SwipeMenuListView listView;
    ArrayList<Model> list;
    DeyisenlerAdaptr adp;
    Context context;
    SqlLiteMethod sqlLiteMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deyisen_musteriler);
        listView = findViewById(R.id.editcl);
        context = this;
        sqlLiteMethod = new SqlLiteMethod(context);
        //String[][] _expCode = sqlLiteMethod.ExecuteSelect("SELECT exp_code,FIRM_ERP_CON,firm_conn FROM Expeditor");
        // String client_group = sqlLiteMethod.ExecuteScalar("select group_ from Clients where kodu ='" + _clientCode + "' limit 1");
        createswipemenu();
        swipemenuclick();
        sqlite s = new sqlite(context);
        list = s.deyisenMusteriler();
        //   String selectQuery = "select clCode,clDefn,clSpecode5 from musteriler";
        //   String a[][] = sqlLiteMethod.ExecuteSelect(selectQuery);
        adp = new DeyisenlerAdaptr(list, getApplicationContext());
        listView.setAdapter(adp);
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
                // item1.setBackground(R.drawable.style_supervizor_menu);ic_cancel_white_36dp
                item1.setIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(context.getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(255, 255, 255)));
                item2.setWidth(dp2px(65));
                item2.setTitleSize(15);
                item2.setTitleColor(Color.BLACK);
                // item1.setBackground(R.drawable.style_supervizor_menu);ic_cancel_white_36dp
                item2.setIcon(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));
                menu.addMenuItem(item2);

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
                        sqlite s = new sqlite(context);
                        String clKod_=list.get(position).getKod();
                        list.remove(list.get(position));
                        s.deleteData(clKod_);
                        adp.notifyDataSetChanged();
                        Toast.makeText(context,clKod_+" Kodlu Musteri Siyahidan Silindi",Toast.LENGTH_LONG).show();

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