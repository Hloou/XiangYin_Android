package net.xy360.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.xy360.R;
import net.xy360.adapters.AllOrderAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.OrderService;
import net.xy360.commonutils.models.Order;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private AllOrderAdapter allOrderAdapter;
    private OrderService orderService = null;
    private UserId userId;

    private int page = 1;

    private boolean requesting = false;

    public AllOrderFragment() {
        // Required empty public constructor
    }


    public static AllOrderFragment newInstance() {
        AllOrderFragment fragment = new AllOrderFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_order, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allOrderAdapter = new AllOrderAdapter(getContext(), 0);
        recyclerView.setAdapter(allOrderAdapter);

        userId = UserData.load(getContext(), UserId.class);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int d = layoutManager.findLastVisibleItemPosition();
                if (allOrderAdapter.getItemCount() - 1 <= d)
                    requestData();
            }
        });

        if (orderService == null)
            orderService = BaseRequest.retrofit.create(OrderService.class);

        requestData();

        return view;
    }

    private void requestData() {
        if (page == 0)
            return;
        if (requesting)
            return;
        requesting = true;
        orderService.getPrintOrder(userId.userId, userId.token, page++, "[6]")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Order>>() {
                    @Override
                    public void onCompleted() {
                        requesting = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page != 0)
                            BaseRequest.ErrorResponse(getContext(), e);
                        requesting = false;
                    }

                    @Override
                    public void onNext(List<Order> orders) {
                        if (orders.size() == 0) {
                            page = 0;
                            return;
                        }
                        allOrderAdapter.addData(orders);
                        Log.d("addData", orders.size() + "");
                    }
                });
    }

}
