package net.xy360.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Order;
import net.xy360.fragments.AllOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jolin on 2016/3/9.
 */
public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Order> orderList;
    private int type;

    //type 0 for all order, show iv_status
    //type 1 for wait paid, show radio button, hide iv_status
    //type 2 for wait recive, hide radio button, hide iv_status
    public AllOrderAdapter(Context context, int type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        orderList = new ArrayList<>();
        this.type = type;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_retailer, tv_types, tv_price, tv_discount_price, tv_delivery_price, tv_real_pay;
        ImageView iv_status;
        LinearLayout ll_unpaid, ll_completed, ll_unreceived;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_retailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            tv_types = (TextView)itemView.findViewById(R.id.tv_types);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_delivery_price = (TextView)itemView.findViewById(R.id.tv_delivery_price);
            tv_real_pay = (TextView)itemView.findViewById(R.id.tv_real_pay);
            iv_status = (ImageView)itemView.findViewById(R.id.iv_status);
            ll_unpaid = (LinearLayout)itemView.findViewById(R.id.ll_unpaid);
            ll_completed = (LinearLayout)itemView.findViewById(R.id.ll_completed);
            ll_unreceived = (LinearLayout)itemView.findViewById(R.id.ll_unreceived);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_all_order, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tv_retailer.setText(order.retailerName);
        holder.tv_types.setText(((order.copyItems == null ? 0 : order.copyItems.size()) +
                                (order.printingItems == null ? 0 : order.printingItems.size())) + "");
        holder.tv_price.setText(String.format("%.2f", order.totalPriceInCent / 100.0));
        holder.tv_discount_price.setText("0.00");
        holder.tv_delivery_price.setText("0.00");
        holder.tv_real_pay.setText(String.format("%.2f", order.actualPriceInCent / 100.0));
        if (type == 0) {
            if (order.status == 0)
                holder.iv_status.setImageResource(R.mipmap.unpaid);
            else if (order.status == 1)
                holder.iv_status.setImageResource(R.mipmap.completed);
            else
                holder.iv_status.setImageResource(R.mipmap.unreceived);
        }
        hideAll(holder);
        if (order.status == 0)
            holder.ll_unpaid.setVisibility(View.VISIBLE);
        else if (order.status == 1)
            holder.ll_completed.setVisibility(View.VISIBLE);
        else
            holder.ll_unreceived.setVisibility(View.VISIBLE);
    }

    private void hideAll(MyViewHolder holder) {
        holder.ll_unreceived.setVisibility(View.GONE);
        holder.ll_unpaid.setVisibility(View.GONE);
        holder.ll_completed.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void addData(List<Order> orders) {
        int i = orderList.size();
        orderList.addAll(orders);
        notifyItemRangeInserted(i, orders.size());
    }
}
