package net.xy360.activitys.user;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ReportService;
import net.xy360.commonutils.models.Report;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.LoadingFragment;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_title, et_content;
    private Button btn_submit;
    private ReportService reportService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();

        if (userId == null)
            userId = UserData.load(this, UserId.class);

        if (reportService == null)
            reportService = BaseRequest.retrofit.create(ReportService.class);
    }


    public void initView() {
        et_title = (EditText)findViewById(R.id.et_title);
        et_content = (EditText)findViewById(R.id.et_content);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            Report report = new Report();
            report.title = et_title.getText().toString();
            report.description = et_content.getText().toString();
            final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
            Subscription s = reportService.sendReport(userId.userId, userId.token, BaseRequest.gson.toJson(report))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Report>() {
                        @Override
                        public void onCompleted() {
                            loadingFragment.dismiss();
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            BaseRequest.ErrorResponse(FeedbackActivity.this, e);
                            loadingFragment.dismiss();
                        }

                        @Override
                        public void onNext(Report report) {
                            Toast.makeText(FeedbackActivity.this, getString(R.string.feedback_submit_success), Toast.LENGTH_SHORT).show();
                        }
                    });
            loadingFragment.setSubscription(s);
        }
    }
}
