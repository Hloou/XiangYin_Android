package net.xy360.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import net.xy360.R;
import net.xy360.adapters.WenKuAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.CopiesService;
import net.xy360.commonutils.models.Copy;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WenKuSearchActivity extends BaseActivity implements TextWatcher{

    private RecyclerView recyclerView;
    private WenKuAdapter wenKuAdapter;
    private CopiesService copiesService = null;
    private int nowpage = 1;
    private EditText et_search;
    private String searchText = "";

    private int setAddFlag = 0; //0 for set, 1 for add

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wen_ku_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_search = (EditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(this);

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
                if (wenKuAdapter.getItemCount() - 1 <= d) {
                    setAddFlag = 1;
                    requestData();
                }
            }
        });
        if (copiesService == null)
            copiesService = BaseRequest.retrofit.create(CopiesService.class);

        requestData();

    }

    private void requestData() {
        if (nowpage != 0)
            copiesService.getCopies(searchText, nowpage++)
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
                            if (setAddFlag == 0)
                                wenKuAdapter.setDatas(copies);
                            else
                                wenKuAdapter.addDatas(copies);
                        }
                    });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchText = new String(s.toString());
        nowpage = 1;
        setAddFlag = 0;
        requestData();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
