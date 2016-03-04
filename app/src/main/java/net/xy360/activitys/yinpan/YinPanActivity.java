package net.xy360.activitys.yinpan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.print.SelectedRetailerActivity;
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

public class YinPanActivity extends BaseActivity implements YinPanListener, View.OnClickListener{

    private RecyclerView recyclerView;
    private YinPanAdapter yinPanAdapter;

    private LabelService labelService = null;
    private FileService fileService = null;

    private UserId userId;

    private PopupWindow popupMore;

    private WindowManager mWindowManager;
    private View mWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yin_pan);

        userId = UserData.load(this, UserId.class);

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

        initView();

        if (labelService == null)
            labelService = BaseRequest.retrofit.create(LabelService.class);
        if (fileService == null)
            fileService = BaseRequest.retrofit.create(FileService.class);

        requestData();
    }

    @Override
    public void initView() {
        //right top popup window
        View viewMore = LayoutInflater.from(this).inflate(R.layout.popup_yin_pan_more, null);
        popupMore = new PopupWindow(viewMore, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupMore.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewMore.findViewById(R.id.ll_trash).setOnClickListener(this);

        //bottom widget
        mWindowManager = getWindowManager();
        Display display = mWindowManager.getDefaultDisplay();
        WindowManager.LayoutParams wp = new WindowManager.LayoutParams();
        wp.width = display.getWidth();
        wp.height = (int)getResources().getDimension(R.dimen.widget_height);
        wp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wp.x = 0;
        wp.y = 0;
        wp.gravity = Gravity.LEFT | Gravity.BOTTOM;
        mWidget = LayoutInflater.from(this).inflate(R.layout.popup_yin_pan_selected, null);
        mWidget.findViewById(R.id.ll_print).setOnClickListener(this);
        mWindowManager.addView(mWidget, wp);
        mWidget.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yin_pan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_more) {
            popupMore.showAsDropDown(findViewById(R.id.menu_more));
            return true;
        } else
            return super.onOptionsItemSelected(item);
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
    public void getFilesViaLabels(String labelId) {
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

    @Override
    public void showWidget(int show) {
        //Log.d("show", "" + show);
        mWidget.setVisibility(show == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_trash) {
            popupMore.dismiss();
            Intent intent = new Intent(this, TrashActivity.class);
            startActivity(intent);
        } else if (id == R.id.ll_print) {
            goSelectedRetailer();
        }
    }

    public void goSelectedRetailer() {
        Intent intent = new Intent(this, SelectedRetailerActivity.class);
        startActivity(intent);
    }
}
