package net.xy360.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.xy360.R;
import net.xy360.adapters.YinPanAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.FileService;
import net.xy360.commonutils.internetrequest.interfaces.LabelService;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.Label;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.interfaces.YinPanListener;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class YinPanActivity extends BaseActivity implements YinPanListener{

    private RecyclerView recyclerView;
    private YinPanAdapter yinPanAdapter;

    private LabelService labelService = null;
    private FileService fileService = null;

    private UserId userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yin_pan);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yinPanAdapter = new YinPanAdapter(this);
        yinPanAdapter.setYinPanListener(this);
        recyclerView.setAdapter(yinPanAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                yinPanAdapter.hideLastViewPager();
            }
        });

        userId = UserData.load(this, UserId.class);

        if (labelService == null)
            labelService = BaseRequest.retrofit.create(LabelService.class);
        if (fileService == null)
            fileService = BaseRequest.retrofit.create(FileService.class);

        requestData();
    }

    private void requestData() {
        labelService.getLabels(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Label>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Label> labels) {
                        yinPanAdapter.setLabelList(labels);
                    }
                });

        fileService.getFiles(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<File> files) {
                        yinPanAdapter.setFileList(files);
                    }
                });
    }

    @Override
    public void getFilesViaLabels(int labelId) {
        labelService.getFilesViaLabels(userId.userId, labelId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<File> files) {
                        Log.d("file", ""+ files.size());
                        yinPanAdapter.setFileList(files);
                    }
                });
    }
}
