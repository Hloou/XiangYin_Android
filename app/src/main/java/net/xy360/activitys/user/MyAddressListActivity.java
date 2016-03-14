package net.xy360.activitys.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.AddressAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.AddressService;
import net.xy360.commonutils.models.Address;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAddressListActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private Button btn_address_add;
    private AddressService addressService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        userId = UserData.load(this, UserId.class);

        if (addressService == null)
            addressService = BaseRequest.retrofit.create(AddressService.class);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(this);
        recyclerView.setAdapter(addressAdapter);
        btn_address_add = (Button)findViewById(R.id.btn_adderess_add);
        btn_address_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_adderess_add) {
            Intent intent = new Intent(this, MyAddressActivity.class);
            startActivity(intent);
        }
    }

    private void requestData() {
        addressService.getAddress(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Address>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(MyAddressListActivity.this, e);
                    }

                    @Override
                    public void onNext(List<Address> addresses) {
                        addressAdapter.setAddressList(addresses);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }
}
