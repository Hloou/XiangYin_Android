package net.xy360.activitys.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.AddressService;
import net.xy360.commonutils.models.Address;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.LoadingFragment;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAddressActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_adderess_add;
    private EditText et_receiver, et_phone, et_address;
    private AddressService addressService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        if (userId == null)
            userId = UserData.load(this, UserId.class);
        if (addressService == null)
            addressService = BaseRequest.retrofit.create(AddressService.class);
    }


    @Override
    public void initView() {
        et_receiver = (EditText)findViewById(R.id.et_receiver);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_address = (EditText)findViewById(R.id.et_address);
        btn_adderess_add = (Button)findViewById(R.id.btn_adderess_add);
        btn_adderess_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_adderess_add) {
            Address address = new Address();
            address.receiverName = et_receiver.getText().toString();
            address.receiverTelephone = et_phone.getText().toString();
            address.receiverAddress = et_address.getText().toString();
            final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
            Subscription s = addressService.addAddress(userId.userId, userId.token, BaseRequest.gson.toJson(address))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Address>() {
                        @Override
                        public void onCompleted() {
                            loadingFragment.dismiss();
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            BaseRequest.ErrorResponse(MyAddressActivity.this, e);
                            loadingFragment.dismiss();
                        }

                        @Override
                        public void onNext(Address address) {
                            Toast.makeText(MyAddressActivity.this, getString(R.string.newaddr_submit_success), Toast.LENGTH_SHORT).show();
                        }
                    });
            loadingFragment.setSubscription(s);
        }
    }
}
