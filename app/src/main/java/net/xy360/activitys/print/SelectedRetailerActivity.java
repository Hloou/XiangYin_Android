package net.xy360.activitys.print;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.RetailerAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.OrderService;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.PrintingCart;
import net.xy360.commonutils.models.Retailer;
import net.xy360.commonutils.realm.RealmHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectedRetailerActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RetailerAdapter retailerAdapter;
    private OrderService orderService = null;
    private List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_retailer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        TypeToken type = new TypeToken<List<File>>(){};
        String data = getIntent().getStringExtra(type.toString());
        fileList = BaseRequest.gson.fromJson(data, type.getType());
        if (orderService == null)
            orderService = BaseRequest.retrofit.create(OrderService.class);
        requestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_retailer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.select_retailer) {
            goPrintOrder();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

    private void goPrintOrder() {

        Retailer retailer = retailerAdapter.getSelectedRetailer();
        if (retailer == null) {
            Toast.makeText(this, getString(R.string.select_retailer_have_not_select), Toast.LENGTH_SHORT).show();
            return;
        }

        RealmHelper.realm.beginTransaction();
        Cart cart = RealmHelper.realm.where(Cart.class).equalTo("retailerId", retailer.retailerId).findFirst();
        if (cart == null) {
            cart = RealmHelper.realm.createObject(Cart.class);
            cart.setRetailerId(retailer.retailerId);
            cart.setRetailerName(retailer.retailerName);
            cart.setPrintingItems(new RealmList<PrintingCart>());
        }

        for (int i = 0; i < fileList.size(); i++) {
            File file = RealmHelper.realm.copyToRealmOrUpdate(fileList.get(i));

            PrintingCart printingCart = RealmHelper.realm.where(PrintingCart.class)
                    .equalTo("id", retailer.retailerId + file.getFileId()).findFirst();
            if (printingCart == null) {
                printingCart = RealmHelper.realm.createObject(PrintingCart.class);
                printingCart.setId(retailer.retailerId + file.getFileId());
                printingCart.setFile(file);
                printingCart.setCopies(1);
                cart.getPrintingItems().add(printingCart);
            } else {
                printingCart.setCopies(printingCart.getCopies() + 1);
            }
        }

        RealmHelper.realm.commitTransaction();

        Intent intent = new Intent(this, PrintOrderActivity.class);
        intent.putExtra(Cart.class.getName(), BaseRequest.gson.toJson(cart));
        startActivity(intent);
    }
}
