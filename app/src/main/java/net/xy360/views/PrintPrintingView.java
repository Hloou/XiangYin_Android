package net.xy360.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.File;
import net.xy360.interfaces.PrintOrderViewListener;

/**
 * Created by jolin on 2016/3/5.
 */
public class PrintPrintingView extends FrameLayout implements View.OnClickListener{

    private RadioButton rb_selected;
    private TextView tv_minus, tv_plus, tv_count, tv_name, tv_price, tv_page;
    private int count;
    private Button btn_delete;
    private PrintOrderViewListener printOrderViewListener;
    private File file;

    public PrintPrintingView(Context context) {
        super(context);
        init(context);
    }

    public PrintPrintingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PrintPrintingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_print_order_printing, this, true);
        count = 1;
        rb_selected = (RadioButton)findViewById(R.id.rb_selected);
        tv_count = (TextView)findViewById(R.id.tv_count);
        tv_minus = (TextView)findViewById(R.id.tv_minus);
        tv_plus = (TextView)findViewById(R.id.tv_plus);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_page = (TextView)findViewById(R.id.tv_page);
        tv_price = (TextView)findViewById(R.id.tv_price);
        btn_delete = (Button)findViewById(R.id.btn_delete);

        tv_count.setText("" + count);
        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_minus) {
            count = (count == 1 ? 1 : --count);
            tv_count.setText("" + count);
        } else if (id == R.id.tv_plus) {
            tv_count.setText("" + (++count));
        } else if (id == R.id.btn_delete) {
            if (printOrderViewListener != null)
                printOrderViewListener.delete(this, 1, file);
            //type 1 for printing
        }
    }

    public void setData(File file) {
        this.file = file;
        tv_name.setText(file.getFileName());

    }

    public void setPrintOrderViewListener(PrintOrderViewListener printOrderViewListener) {
        this.printOrderViewListener = printOrderViewListener;
    }
}
