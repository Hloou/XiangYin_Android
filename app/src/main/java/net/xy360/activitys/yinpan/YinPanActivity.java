package net.xy360.activitys.yinpan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.algorithm.MD5;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.print.SelectedRetailerActivity;
import net.xy360.adapters.YinPanAdapter;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.FileService;
import net.xy360.commonutils.internetrequest.interfaces.LabelService;
import net.xy360.commonutils.internetrequest.interfaces.OSSService;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.Label;
import net.xy360.commonutils.models.OSSCredential;
import net.xy360.commonutils.models.OSSFile;
import net.xy360.commonutils.models.StorageInfo;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.YinPanUploadFragment;
import net.xy360.interfaces.YinPanListener;
import net.xy360.utils.RealPathFromURI;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class YinPanActivity extends BaseActivity implements View.OnClickListener, YinPanUploadFragment.DownloadListener{

    private static final int SELECT_PHOTO = 100;

    private RecyclerView recyclerView;
    private TextView tv_size;
    private YinPanAdapter yinPanAdapter;

    private LabelService labelService = null;
    private FileService fileService = null;
    private OSSService ossService = null;

    private UserId userId;

    private PopupWindow popupMore;

    private WindowManager mWindowManager;
    private View mWidget;

    private OSSFile ossFile;

    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yin_pan);

        userId = UserData.load(this, UserId.class);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        tv_size = (TextView)findViewById(R.id.tv_size);
        tv_size.setText("0M/0M");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yinPanAdapter = new YinPanAdapter(this);
        //yinPanAdapter.setYinPanListener(this);
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
        if (ossService == null)
            ossService = BaseRequest.retrofit.create(OSSService.class);

        requestData();

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Log.d("handler", "1");
                showWidget(yinPanAdapter.getSelectedFile().size());
                mHandler.postDelayed(this, 200);
            }
        }, 200);

    }

    @Override
    public void initView() {
        //right top popup window
        View viewMore = LayoutInflater.from(this).inflate(R.layout.popup_yin_pan_more, null);
        popupMore = new PopupWindow(viewMore, getResources().getDimensionPixelSize(R.dimen.yinpan_popup_window_width),
                getResources().getDimensionPixelSize(R.dimen.yinpan_popup_window_height), true);
        popupMore.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewMore.findViewById(R.id.ll_trash).setOnClickListener(this);
        viewMore.findViewById(R.id.ll_upload).setOnClickListener(this);

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
        mWidget.findViewById(R.id.ll_delete).setOnClickListener(this);
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

        fileService.getFiles(userId.userId, userId.token, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<File>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(YinPanActivity.this, e);
                    }

                    @Override
                    public void onNext(List<File> files) {
                        yinPanAdapter.setFileList(files);
                    }
                });
        fileService.getStorage(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StorageInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(YinPanActivity.this, e);
                    }

                    @Override
                    public void onNext(StorageInfo storageInfo) {
                        tv_size.setText(String.format("%.2fM/%.2fM", (storageInfo.usedInByte >> 20) * 1.0, (storageInfo.maxStorageInByte >> 20) * 1.0));
                    }
                });
    }

    public void getFilesViaLabels(String labelId) {
        //should jump to another activity and show it

    }

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
        } else if (id == R.id.ll_upload) {
            popupMore.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PHOTO);
        } else if (id == R.id.ll_delete) {
            allDelete();
        }
    }

    public void goSelectedRetailer() {
        Intent intent = new Intent(this, SelectedRetailerActivity.class);
        List<File> list = yinPanAdapter.getSelectedFile();
        intent.putExtra(new TypeToken<List<File>>(){}.toString(), BaseRequest.gson.toJson(list));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                uploadFile(data.getData());
            }
        }
    }

    private void uploadFile(Uri uri) {
        final String realPath = RealPathFromURI.getRealPathFromURI(this, uri);
        final String hash = MD5.getMD5(new java.io.File(realPath));
        ossService.getFilePathViaHash(hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OSSFile>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(YinPanActivity.this, e);
                    }

                    @Override
                    public void onNext(OSSFile ossFile) {
                        YinPanActivity.this.ossFile = ossFile;
                        if (ossFile.isExisted)
                            uploadFileToServer(realPath, hash);
                        else
                            uploadFileToCloud(realPath, hash);
                    }
                });

    }

    private void uploadFileToCloud(final String realPath, final String hash) {
        ossService.getOSSCredential(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OSSCredential>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(YinPanActivity.this, e);
                    }

                    @Override
                    public void onNext(OSSCredential ossCredential) {
                        startUpload(realPath, hash, ossCredential);
                    }
                });
    }

    public void startUpload(final String realPath, final String hash, OSSCredential ossCredential) {
        YinPanUploadFragment yinPanUploadFragment = new YinPanUploadFragment();
        yinPanUploadFragment.setData(realPath, hash, ossFile, ossCredential);
        yinPanUploadFragment.show(getSupportFragmentManager(), "upload");
    }

    private void uploadFileToServer(String realPath, String hash) {
        Log.d("ff", RealPathFromURI.getFileType(realPath));
        Log.d("ff", RealPathFromURI.getFileName(realPath));
        Log.d("ff", hash);
        ossService.uploadFile(userId.userId, userId.token, hash, RealPathFromURI.getFileName(realPath), RealPathFromURI.getFileType(realPath))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<File>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.d("error", e.toString());
                    Log.d("error", e.getMessage());
                    BaseRequest.ErrorResponse(YinPanActivity.this, e);
                }

                @Override
                public void onNext(File file) {
                    Log.d("success", BaseRequest.gson.toJson(file));
                    yinPanAdapter.addFile(file);
                    Toast.makeText(YinPanActivity.this, getString(R.string.yinpan_finish_upload), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void afterDownload(String realPath, String hash, String reason) {
        if (realPath != null)
            uploadFileToServer(realPath, hash);
        else
            Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
    }

    private void allDelete() {
        List<File> files = yinPanAdapter.getSelectedFile();
        for (int i = 0; i < files.size(); i++) {
            final File file = files.get(i);
            fileService.deleteFile(userId.userId, "" + file.getInspaceUserFileId(), userId.token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            yinPanAdapter.removeFile(file);
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

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
