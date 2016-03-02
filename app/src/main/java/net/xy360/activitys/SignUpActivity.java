package net.xy360.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.SendAuth;

import net.xy360.R;
import net.xy360.activitys.user.SettingsActivity;
import net.xy360.wxapi.WXEntryActivity;

import org.w3c.dom.Text;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private TextView user_agreement, own_account;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void initView() {
        user_agreement = (TextView) findViewById(R.id.signup_user_agreement);
        own_account = (TextView) findViewById(R.id.signup_account);
        signup = (Button) findViewById(R.id.signup_signup);

        user_agreement.setOnClickListener(this);
        own_account.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signup_user_agreement) {
            Intent intent = new Intent(SignUpActivity.this, ForgetPasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Log.d("tag", "zhuce");
            startActivity(intent);
        }
        if (id == R.id.signup_account) {
            Intent intent = new Intent(SignUpActivity.this, WXEntryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Log.d("tag", "zhanghu");
            startActivity(intent);
        }
        if (id == R.id.signup_signup) {
            Intent intent = new Intent(SignUpActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Log.d("tag", "zhanghu");
            startActivity(intent);
        }
    }

}
