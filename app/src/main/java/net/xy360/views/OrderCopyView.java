package net.xy360.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.CopyItem;
import net.xy360.commonutils.models.CopyOrder;

import org.w3c.dom.Text;

/**
 * Created by jolin on 2016/3/9.
 */
public class OrderCopyView extends FrameLayout{

    private CopyOrder copyOrder;
    private TextView tv_name, tv_price, tv_page, tv_specification;
    private ImageView iv_icon;

    public OrderCopyView(Context context) {
        super(context);
        init(context);
    }

    public OrderCopyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderCopyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_order_copy, this, true);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_page = (TextView)findViewById(R.id.tv_page);
        tv_price = (TextView)findViewById(R.id.tv_price);
        tv_specification = (TextView)findViewById(R.id.tv_specification);
        iv_icon = (ImageView)findViewById(R.id.iv_icon);
    }

    public void setData(CopyOrder copyOrder) {
        this.copyOrder = copyOrder;
        tv_name.setText(copyOrder.name);
        tv_price.setText(String.format("¥%.2f", copyOrder.copies * copyOrder.unitPriceInCent / 100.0));
    }

}
