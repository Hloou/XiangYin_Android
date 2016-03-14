package net.xy360.activitys.ad;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.utils.DpPxChange;

/**
 * Created by Administrator on 2016/3/6.
 */
public class PrintMoneyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView integral_img_detail, integral_img_income, integral_img_expenditure;
    private LinearLayout integral_line_detail, integral_line_income, integral_line_expenditure;
    private TextView integral_tv_detail, integral_tv_income, integral_tv_expenditure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_money);
        initView();
        initListener();
    }

    @Override
    public void initView() {

        integral_line_detail = (LinearLayout) findViewById(R.id.integral_line_detail);
        integral_line_income = (LinearLayout) findViewById(R.id.integral_line_income);
        integral_line_expenditure = (LinearLayout) findViewById(R.id.integral_line_expenditure);

        integral_tv_detail = (TextView) findViewById(R.id.integral_tv_detail);
        integral_tv_income = (TextView) findViewById(R.id.integral_tv_income);
        integral_tv_expenditure = (TextView) findViewById(R.id.integral_tv_expenditure);

        integral_img_detail = (ImageView) findViewById(R.id.integral_img_detail);
        integral_img_income = (ImageView) findViewById(R.id.integral_img_income);
        integral_img_expenditure = (ImageView) findViewById(R.id.integral_img_expenditure);


    }

    private void initListener() {

        integral_line_detail.setOnClickListener(this);
        integral_line_income.setOnClickListener(this);
        integral_line_expenditure.setOnClickListener(this);
    }

    private void Tab() {

        integral_tv_detail.setTextColor(ContextCompat.getColor(this, R.color.black));
        integral_tv_income.setTextColor(ContextCompat.getColor(this, R.color.black));
        integral_tv_expenditure.setTextColor(ContextCompat.getColor(this, R.color.black));
        integral_img_detail.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        integral_img_income.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        integral_img_expenditure.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    public void onClick(View v) {
        Tab();
        switch (v.getId()) {
            case R.id.integral_line_detail:
                integral_tv_detail.setTextColor(ContextCompat.getColor(this, R.color.sky_blue));
                integral_img_detail.setBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue));
                break;
            case R.id.integral_line_income:
                integral_tv_income.setTextColor(ContextCompat.getColor(this, R.color.sky_blue));
                integral_img_income.setBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue));
                break;
            case R.id.integral_line_expenditure:
                integral_tv_expenditure.setTextColor(ContextCompat.getColor(this, R.color.sky_blue));
                integral_img_expenditure.setBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue));
                break;
        }

    }


}

