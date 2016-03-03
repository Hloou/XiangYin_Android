package net.xy360.activitys.wenku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.wenku.WenKuSearchActivity;
import net.xy360.adapters.WenKuAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.CopiesService;
import net.xy360.commonutils.models.Copy;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WenKuActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private WenKuAdapter wenKuAdapter;
    private CopiesService copiesService = null;
    private int nowpage = 1;
    private LinearLayout ll_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wen_ku);

        ll_search = (LinearLayout)findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wenKuAdapter = new WenKuAdapter(this);
        recyclerView.setAdapter(wenKuAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int d = layoutManager.findLastVisibleItemPosition();
                wenKuAdapter.hideLastShow();
                //scroll bottom to load more
                if (wenKuAdapter.getItemCount() - 1 <= d)
                    requestData();
            }
        });
        if (copiesService == null)
            copiesService = BaseRequest.retrofit.create(CopiesService.class);
        requestData();

    }

    private void requestData() {
        if (nowpage != 0)
            copiesService.getCopies("", nowpage++)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Copy>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(List<Copy> copies) {
                            if(copies.size() == 0) {
                                nowpage = 0;
                                return;
                            }
                            wenKuAdapter.addDatas(copies);
                        }
                    });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WenKuSearchActivity.class);
        startActivity(intent);
    }
}
