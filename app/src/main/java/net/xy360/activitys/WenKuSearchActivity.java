package net.xy360.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.xy360.R;
import net.xy360.adapters.WenKuAdapter;

public class WenKuSearchActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private WenKuAdapter wenKuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wen_ku_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wenKuAdapter = new WenKuAdapter(this);
        
    }

    public void initView() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == android.R.id.home) {
            finish();
        }
    }
}
