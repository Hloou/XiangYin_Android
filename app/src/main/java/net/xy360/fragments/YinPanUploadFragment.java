package net.xy360.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import net.xy360.R;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.OSSCredential;
import net.xy360.commonutils.models.OSSFile;
import net.xy360.contants.Download;
import net.xy360.utils.RealPathFromURI;

/**
 * Created by jolin on 2016/3/13.
 */
public class YinPanUploadFragment extends DialogFragment implements View.OnClickListener{

    private ImageView iv_icon;
    private TextView tv_name, tv_progress;
    private ProgressBar progressBar;
    private Button btn_cancel;
    private String realPath;
    private OSSFile ossFile;
    private OSSCredential ossCredential;
    private String hash;

    private OSSAsyncTask task = null;

    public interface DownloadListener {
        public void afterDownload(String realPath, String hash, String reason);
    }

    private DownloadListener downloadListener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_yin_pan_upload, null);
        iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
        tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_progress = (TextView)view.findViewById(R.id.tv_progress);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        btn_cancel = (Button)view.findViewById(R.id.btn_cancel);

        if (RealPathFromURI.getFileType(realPath).equals("pdf"))
            iv_icon.setImageResource(R.mipmap.pdf);
        else if (RealPathFromURI.getFileType(realPath).equals("doc"))
            iv_icon.setImageResource(R.mipmap.word);
        tv_name.setText(RealPathFromURI.getFileName(realPath));
        tv_progress.setText("0KB");
        btn_cancel.setOnClickListener(this);
        PutObjectRequest put = new PutObjectRequest(ossFile.ossBucketName, hash + "." + RealPathFromURI.getFileType(realPath), realPath);
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossCredential.AccessKeyId, ossCredential.AccessKeySecret, ossCredential.SecurityToken);
        OSS oss = new OSSClient(getActivity(), ossFile.ossEndPoint, credentialProvider);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest putObjectRequest, final long l, final long l1) {
                Log.d("upload", l + " " + l1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_progress.setText(String.format("%.2fKB", l / 1024.0));
                        progressBar.setMax((int) l1);
                        progressBar.setProgress((int) l);
                    }
                });
            }
        });
        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                Log.d("putobject", "success");
                if (downloadListener != null)
                    downloadListener.afterDownload(realPath, hash, null);
                dismiss();
                //uploadFileToServer(realPath, hash);
                //dismiss();
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                Log.d("putobject", "failure");
                if (e != null)
                    Log.d("ffff", e.toString());
                if (e1 != null)
                    Log.d("ffff", e1.toString());
                if (downloadListener != null)
                    downloadListener.afterDownload(null, null, getString(R.string.yinpan_error_upload));
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    public void setData(String realPath, String hash, OSSFile ossFile, OSSCredential ossCredential) {
        this.realPath = realPath;
        this.hash = hash;
        this.ossFile = ossFile;
        this.ossCredential = ossCredential;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        downloadListener = (DownloadListener)activity;
    }

    @Override
    public void onDetach() {
        Log.d("fragment", "dismiss");
        if (task != null)
            task.cancel();

        super.onDetach();
    }
}
