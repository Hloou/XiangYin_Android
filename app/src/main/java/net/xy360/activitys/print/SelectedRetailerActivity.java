package net.xy360.activitys.print;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.RetailerAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.OrderService;
import net.xy360.commonutils.models.Retailer;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectedRetailerActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RetailerAdapter retailerAdapter;
    private OrderService orderService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_retailer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        if (orderService == null)
            orderService = BaseRequest.retrofit.create(OrderService.class);
        requestData();
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        retailerAdapter = new RetailerAdapter(this);
        recyclerView.setAdapter(retailerAdapter);
    }

    private void requestData() {
        orderService.getRetailers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Retailer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!BaseRequest.ErrorResponse(SelectedRetailerActivity.this, e)) {
                            //do sth
                        }
                    }

                    @Override
                    public void onNext(List<Retailer> retailers) {
                        Log.d("retailer", retailers.size() + "");
                        retailerAdapter.addRetailerList(retailers);
                    }
                });
    }
}
