package net.xy360.activitys.yinpan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.YinPanTrashAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.FileService;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TrashActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private YinPanTrashAdapter yinPanTrashAdapter;
    private FileService fileService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        if (fileService == null)
            fileService = BaseRequest.retrofit.create(FileService.class);
        userId = UserData.load(this, UserId.class);

        request();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yin_pan_trash, menu);
        return true;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yinPanTrashAdapter = new YinPanTrashAdapter(this);
        recyclerView.setAdapter(yinPanTrashAdapter);
        findViewById(R.id.ll_fully_delete).setOnClickListener(this);
        findViewById(R.id.ll_restore).setOnClickListener(this);
    }

    private void request() {
        fileService.getFiles(userId.userId, userId.token, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(TrashActivity.this, e);
                    }

                    @Override
                    public void onNext(List<File> files) {
                        Log.d("delete", BaseRequest.gson.toJson(files));
                        yinPanTrashAdapter.setFileList(files);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_restore)
            restore();
        else if (id == R.id.ll_fully_delete)
            delete();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_all_selected) {
            yinPanTrashAdapter.allSelected();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void restore() {
        List<File> files = yinPanTrashAdapter.getSelectedFile();
        for (int i = 0; i < files.size(); i++) {
            final File file = files.get(i);
            fileService.restoreFile(userId.userId, "" + file.getInspaceUserFileId(), userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        yinPanTrashAdapter.removeFile(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(TrashActivity.this, e);
                    }

                    @Override
                    public void onNext(String s) {
                    }
                });
        }
    }

    private void delete() {
        List<File> files = yinPanTrashAdapter.getSelectedFile();
        for (int i = 0; i < files.size(); i++) {
            final File file = files.get(i);
            fileService.removeFile(userId.userId, "" + file.getInspaceUserFileId(), userId.token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            yinPanTrashAdapter.removeFile(file);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {

                        }
                    });
        }
    }
}
