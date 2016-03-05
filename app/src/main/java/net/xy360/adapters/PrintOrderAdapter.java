package net.xy360.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.File;
import net.xy360.views.PrintCopyView;
import net.xy360.views.PrintPrintingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jolin on 2016/3/5.
 */
public class PrintOrderAdapter extends RecyclerView.Adapter<PrintOrderAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Cart> cartList;

    public PrintOrderAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        cartList = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_retailer;
        LinearLayout ll_copyitem, ll_printitem;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_retailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            ll_copyitem = (LinearLayout)itemView.findViewById(R.id.ll_copyitem);
            ll_printitem = (LinearLayout)itemView.findViewById(R.id.ll_printingitem);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_print_order, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Cart cart = cartList.get(position);
        holder.tv_retailer.setText(cart.retailerName);
        //set copy data
        holder.ll_copyitem.removeAllViews();
        List<Copy> copies = cart.copyItems;
        if (copies != null) {
            for (int i = 0; i < copies.size(); i++) {
                PrintCopyView printCopyView = new PrintCopyView(context);
                holder.ll_copyitem.addView(printCopyView);
                printCopyView.setData(copies.get(i));
            }
        }
        //set printing data
        holder.ll_printitem.removeAllViews();
        List<File> files = cart.printintItems;
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                PrintPrintingView printPrintingView = new PrintPrintingView(context);
                holder.ll_printitem.addView(printPrintingView);
                printPrintingView.setData(files.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void addData(List<Cart> cartList) {
        this.cartList.addAll(cartList);
        notifyDataSetChanged();
    }
}
