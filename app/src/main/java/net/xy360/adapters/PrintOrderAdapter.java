package net.xy360.adapters;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.realm.RealmHelper;
import net.xy360.interfaces.PrintOrderViewListener;
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

    class MyViewHolder extends RecyclerView.ViewHolder implements PrintOrderViewListener{
        RadioButton rb_selected;
        TextView tv_retailer;
        LinearLayout ll_copyitem, ll_printitem;
        Cart cart;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_retailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            ll_copyitem = (LinearLayout)itemView.findViewById(R.id.ll_copyitem);
            ll_printitem = (LinearLayout)itemView.findViewById(R.id.ll_printingitem);
            rb_selected = (RadioButton)itemView.findViewById(R.id.rb_selected);
        }

        @Override
        public void delete(View view, int type, Object o) {
            if (type == 0) {  //copy
                RealmHelper.realm.beginTransaction();
                cart.getCopyItems().remove(o);
                if (cart.getCopyItems().size() == 0 && cart.getPrintintItems().size() == 0) {
                    int index = cartList.indexOf(cart);
                    cartList.remove(cart);
                    //cuz it s not a realmlist, so we should remove it by cart.removefromrealm
                    cart.removeFromRealm();
                    PrintOrderAdapter.this.notifyItemRemoved(index);
                }
                RealmHelper.realm.commitTransaction();
                ll_copyitem.removeView(view);
            } else if (type == 1) { // printing
                RealmHelper.realm.beginTransaction();
                cart.getPrintintItems().remove(o);
                if (cart.getCopyItems().size() == 0 && cart.getPrintintItems().size() == 0) {
                    int index = cartList.indexOf(cart);
                    cartList.remove(cart);
                    cart.removeFromRealm();
                    PrintOrderAdapter.this.notifyItemRemoved(index);
                }
                RealmHelper.realm.commitTransaction();
                ll_printitem.removeView(view);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_print_order, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Cart cart = cartList.get(position);
        holder.cart = cart;
        holder.tv_retailer.setText(cart.getRetailerName());
        //set copy data
        holder.ll_copyitem.removeAllViews();
        List<Copy> copies = cart.getCopyItems();
        if (copies != null) {
            for (int i = 0; i < copies.size(); i++) {
                PrintCopyView printCopyView = new PrintCopyView(context);
                printCopyView.setPrintOrderViewListener(holder);
                holder.ll_copyitem.addView(printCopyView);
                printCopyView.setData(copies.get(i));
            }
        }

        //set printing data
        holder.ll_printitem.removeAllViews();
        List<File> files = cart.getPrintintItems();
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                PrintPrintingView printPrintingView = new PrintPrintingView(context);
                printPrintingView.setPrintOrderViewListener(holder);
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
