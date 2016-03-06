package net.xy360.activitys.print;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.PrintOrderAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class PrintOrderActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PrintOrderAdapter printOrderAdapter;

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
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        printOrderAdapter = new PrintOrderAdapter(this);
        recyclerView.setAdapter(printOrderAdapter);
    }
}
