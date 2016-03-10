package net.xy360.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.activitys.print.AllOrderActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.OrderService;
import net.xy360.commonutils.models.CopyItem;
import net.xy360.commonutils.models.CopyOrder;
import net.xy360.commonutils.models.Order;
import net.xy360.commonutils.models.PrintingOrder;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.AllOrderFragment;
import net.xy360.views.OrderCopyView;
import net.xy360.views.OrderPrintingView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jolin on 2016/3/9.
 */
public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Order> orderList;
    private int type;
    private OrderService orderService = null;
    private UserId userId = null;

    //type 0 for all order, show iv_status
    //type 1 for wait paid, show radio button, hide iv_status
    //type 2 for wait recive, hide radio button, hide iv_status
    public AllOrderAdapter(Context context, int type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        orderList = new ArrayList<>();
        this.type = type;
        if (orderService == null)
            orderService = BaseRequest.retrofit.create(OrderService.class);
        if (userId == null)
            userId = UserData.load(context, UserId.class);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_retailer, tv_types, tv_price, tv_discount_price, tv_delivery_price, tv_real_pay, tv_submit_time;
        ImageView iv_status;
        LinearLayout ll_unpaid, ll_completed, ll_unreceived;
        LinearLayout ll_copyitem, ll_printingitem;
        CheckBox cb_selected;
        Order order;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_retailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            tv_types = (TextView)itemView.findViewById(R.id.tv_types);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_delivery_price = (TextView)itemView.findViewById(R.id.tv_delivery_price);
            tv_real_pay = (TextView)itemView.findViewById(R.id.tv_real_pay);
            tv_submit_time = (TextView)itemView.findViewById(R.id.tv_submit_time);
            iv_status = (ImageView)itemView.findViewById(R.id.iv_status);
            ll_unpaid = (LinearLayout)itemView.findViewById(R.id.ll_unpaid);
            ll_completed = (LinearLayout)itemView.findViewById(R.id.ll_completed);
            ll_unreceived = (LinearLayout)itemView.findViewById(R.id.ll_unreceived);
            ll_copyitem = (LinearLayout)itemView.findViewById(R.id.ll_copyitem);
            ll_printingitem = (LinearLayout)itemView.findViewById(R.id.ll_printingitem);
            cb_selected = (CheckBox)itemView.findViewById(R.id.cb_selected);
            itemView.findViewById(R.id.btn_cancel).setOnClickListener(this);
            itemView.findViewById(R.id.btn_complete).setOnClickListener(this);
            itemView.findViewById(R.id.btn_receive).setOnClickListener(this);
            if (type == 1)
                cb_selected.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_complete) {

            } else if (id == R.id.btn_cancel) {
                orderService.updatePrintOrder(userId.userId, order.orderId + "", userId.token, "cancel")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            refreshUI();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {

                        }
                    });
                refreshUI();
            } else if (id == R.id.btn_receive) {
                orderService.updatePrintOrder(userId.userId, order.orderId + "", userId.token, "confirm")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                refreshUI();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {

                            }
                        });
                refreshUI();
            }
        }

        public void refreshUI() {
            Intent intent = new Intent(context, AllOrderActivity.class);
            //use single top, send intent to activity to refresh
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
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
        holder.tv_submit_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.userSubmittedTime));
        holder.order = orderList.get(position);
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
        else if (order.status >=2 && order.status <= 5)
            holder.ll_unreceived.setVisibility(View.VISIBLE);
        holder.ll_copyitem.removeAllViews();
        holder.ll_printingitem.removeAllViews();
        List<CopyOrder> copyOrders = order.copyItems;
        if (copyOrders != null) {
            for (int i = 0; i < copyOrders.size(); i++) {
                OrderCopyView orderCopyView = new OrderCopyView(context);
                orderCopyView.setData(copyOrders.get(i));
                holder.ll_copyitem.addView(orderCopyView);
            }
        }
        List<PrintingOrder> printingOrders = order.printingItems;
        if (printingOrders != null) {
            for (int i = 0; i < printingOrders.size(); i++) {
                OrderPrintingView orderPrintingView = new OrderPrintingView(context);
                orderPrintingView.setData(printingOrders.get(i));
                holder.ll_printingitem.addView(orderPrintingView);
            }
        }
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
