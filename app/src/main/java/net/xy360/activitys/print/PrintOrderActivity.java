package net.xy360.activitys.print;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.PrintOrderAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.realm.RealmHelper;
import net.xy360.commonutils.userdata.UserData;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class PrintOrderActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private PrintOrderAdapter printOrderAdapter;
    private View footerView;
    private PopupWindow popupAdd;
    private UserId userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        RealmResults<Cart> carts = RealmHelper.realm.where(Cart.class).findAll();

        List<Cart> list = new ArrayList<>();
        list.addAll(carts);
        printOrderAdapter.addData(list);
        userId = UserData.load(this, UserId.class);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        printOrderAdapter = new PrintOrderAdapter(this);
        recyclerView.setAdapter(printOrderAdapter);
        footerView = LayoutInflater.from(this).inflate(R.layout.item_print_order_footer, null);
        printOrderAdapter.setFooterView(footerView);
        footerView.findViewById(R.id.btn_add).setOnClickListener(this);
        View viewAdd = LayoutInflater.from(this).inflate(R.layout.popup_print_order_add, null);
        popupAdd = new PopupWindow(viewAdd, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupAdd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            popupAdd.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        } else if (id == R.id.tv_submit) {

        }
    }
}
