package net.xy360.activitys.print;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.widget.TextView;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.PrintOrderAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.realm.RealmHelper;
import net.xy360.commonutils.userdata.UserData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class PrintOrderActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private PrintOrderAdapter printOrderAdapter;
    private View footerView;
    private PopupWindow popupAdd;
    private UserId userId;
    private TextView tv_real_pay;
    private TextView tv_types;

    private Handler mhandler;

    //footer element
    private TextView tv_print_total_price;
    private TextView tv_discount_price;

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

        //refresh the total price
        mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PrintOrderAdapter.RetTotal retTotal = printOrderAdapter.calTotalPrice();
                tv_print_total_price.setText(String.format("%.2f", retTotal.total / 100.0));
                //must minus discount price
                tv_real_pay.setText(String.format("%.2f", retTotal.total / 100.0));
                tv_types.setText("" + retTotal.type);
                mhandler.postDelayed(this, 200);
            }
        }, 200);

    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        printOrderAdapter = new PrintOrderAdapter(this);
        recyclerView.setAdapter(printOrderAdapter);
        tv_real_pay = (TextView)findViewById(R.id.tv_real_pay);
        tv_real_pay.setText("0.00");

        //footer and footerview
        footerView = LayoutInflater.from(this).inflate(R.layout.item_print_order_footer, null);
        tv_print_total_price = (TextView)footerView.findViewById(R.id.tv_print_total_price);
        tv_print_total_price.setText("0.00");
        tv_discount_price = (TextView)footerView.findViewById(R.id.tv_discount_price);
        tv_discount_price.setText("0.00");
        tv_types = (TextView)footerView.findViewById(R.id.tv_types);
        tv_types.setText("0");
        printOrderAdapter.setFooterView(footerView);
        View viewAdd = LayoutInflater.from(this).inflate(R.layout.popup_print_order_add, null);
        popupAdd = new PopupWindow(viewAdd, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupAdd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_submit) {
            printOrderAdapter.submitItem(userId);
        }
    }

    @Override
    protected void onDestroy() {
        mhandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
