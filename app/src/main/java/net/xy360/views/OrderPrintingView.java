package net.xy360.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.PrintingOrder;

import org.w3c.dom.Text;

/**
 * Created by jolin on 2016/3/10.
 */
public class OrderPrintingView extends FrameLayout {

    private TextView tv_name, tv_price, tv_specification, tv_double_side, tv_page, tv_copies;
    private PrintingOrder printingOrder;

    public OrderPrintingView(Context context) {
        super(context);
        init(context);
    }

    public OrderPrintingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderPrintingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_order_printing, this, true);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_price = (TextView)findViewById(R.id.tv_price);
        tv_specification = (TextView)findViewById(R.id.tv_specification);
        tv_double_side = (TextView)findViewById(R.id.tv_double_side);
        tv_page = (TextView)findViewById(R.id.tv_page);
        tv_copies = (TextView)findViewById(R.id.tv_copies);
    }

    public void setData(PrintingOrder printingOrder) {
        this.printingOrder = printingOrder;
        Context context = getContext();
        tv_name.setText(printingOrder.fileName);
        tv_price.setText(String.format("¥%.2f", printingOrder.unitPriceInCent * printingOrder.copies / 100.0));
        if (printingOrder.paperSpecificationId == 1)
            tv_specification.setText(context.getString(R.string.specification_1));
        else if (printingOrder.paperSpecificationId == 2)
            tv_specification.setText(context.getString(R.string.specification_2));
        else if (printingOrder.paperSpecificationId == 3)
            tv_specification.setText(context.getString(R.string.specification_3));
        else if (printingOrder.paperSpecificationId == 4)
            tv_specification.setText(context.getString(R.string.specification_4));
        else if (printingOrder.paperSpecificationId == 5)
            tv_specification.setText(context.getString(R.string.specification_5));
        tv_double_side.setText(printingOrder.isDoubleSided ? context.getString(R.string.specification_double) :
                        context.getString(R.string.specification_single));
        tv_page.setText(context.getString(R.string.all_order_totalword) + (printingOrder.endPageNumber - printingOrder.startPageNumber + 1) +
                        context.getString(R.string.all_order_pageword));
        tv_copies.setText("*" + printingOrder.copies + context.getString(R.string.all_order_typeword));
    }

}
