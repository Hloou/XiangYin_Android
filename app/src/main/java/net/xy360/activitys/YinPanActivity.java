package net.xy360.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.xy360.R;
import net.xy360.adapters.YinPanAdapter;

public class YinPanActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private YinPanAdapter yinPanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yin_pan);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yinPanAdapter = new YinPanAdapter(this);
        recyclerView.setAdapter(yinPanAdapter);
    }

    public void initView() {

    }
}
