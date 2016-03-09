package net.xy360.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import net.xy360.R;

/**
 * Created by jolin on 2016/3/9.
 */
public class OrderCopyView extends FrameLayout{

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
    }
}
