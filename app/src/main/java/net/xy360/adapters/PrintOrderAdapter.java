package net.xy360.adapters;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.OrderService;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.CopyItem;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.Order;
import net.xy360.commonutils.models.PrintingItem;
import net.xy360.commonutils.realm.RealmHelper;
import net.xy360.interfaces.PrintOrderViewListener;
import net.xy360.views.PrintCopyView;
import net.xy360.views.PrintPrintingView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Retrofit;

/**
 * Created by jolin on 2016/3/5.
 */
public class PrintOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Cart> cartList;
    private View footerView;
    private OrderService orderService;

    class SelectedCart {
        boolean all_selected;
        List<Boolean> copy_selected;
        List<Boolean> printing_selected;
    }

    private List<SelectedCart> selectedCartList;

    public PrintOrderAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        cartList = new ArrayList<>();
        selectedCartList = new ArrayList<>();

    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements PrintOrderViewListener, CompoundButton.OnCheckedChangeListener{
        CheckBox cb_selected;
        TextView tv_retailer;
        LinearLayout ll_copyitem, ll_printitem;
        List<Boolean> copy_selected, print_selected;
        Cart cart;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_retailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            ll_copyitem = (LinearLayout)itemView.findViewById(R.id.ll_copyitem);
            ll_printitem = (LinearLayout)itemView.findViewById(R.id.ll_printingitem);
            cb_selected = (CheckBox)itemView.findViewById(R.id.cb_selected);
            cb_selected.setOnCheckedChangeListener(this);
        }

        @Override
        public void delete(View view, int type, Object o) {
            if (type == 0) {  //copy
                RealmHelper.realm.beginTransaction();
                int i = cart.getCopyItems().indexOf(o);
                cart.getCopyItems().remove(i);
                copy_selected.remove(i);
                if (cart.getCopyItems().size() == 0 && cart.getPrintingItems().size() == 0) {
                    int index = cartList.indexOf(cart);
                    cartList.remove(index);
                    //cuz it s not a realmlist, so we should remove it by cart.removefromrealm
                    cart.removeFromRealm();
                    PrintOrderAdapter.this.notifyItemRemoved(index);
                }
                RealmHelper.realm.commitTransaction();
                ll_copyitem.removeView(view);
            } else if (type == 1) { // printing
                RealmHelper.realm.beginTransaction();
                int i = cart.getPrintingItems().indexOf(o);
                cart.getPrintingItems().remove(i);
                print_selected.remove(i);
                if (cart.getCopyItems().size() == 0 && cart.getPrintingItems().size() == 0) {
                    int index = cartList.indexOf(cart);
                    cartList.remove(index);
                    cart.removeFromRealm();
                    PrintOrderAdapter.this.notifyItemRemoved(index);
                }
                RealmHelper.realm.commitTransaction();
                ll_printitem.removeView(view);
            }
        }

        @Override
        public void selectChange(View view, int type, boolean is_check) {
            if (type == 0) {
                int index = ll_copyitem.indexOfChild(view);
                copy_selected.set(index, is_check);
            } else if (type == 1) {
                int index = ll_printitem.indexOfChild(view);
                //Log.d("test selected", index + " " + is_check);
                print_selected.set(index, is_check);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //get the index
            int index = cartList.indexOf(cart);
            SelectedCart sc = selectedCartList.get(index);
            sc.all_selected = isChecked;
            int size = ll_copyitem.getChildCount();
            for (int i = 0; i < size; i++) {
                ((PrintCopyView)ll_copyitem.getChildAt(i)).setCb_selected(isChecked);
            }
            size = ll_printitem.getChildCount();
            for (int i = 0; i < size; i++) {
                ((PrintPrintingView)ll_printitem.getChildAt(i)).setCb_selected(isChecked);
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new MyViewHolder(inflater.inflate(R.layout.item_print_order, parent, false));
        else
            return new FooterViewHolder(footerView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == 0) {
            MyViewHolder holder = (MyViewHolder)viewHolder;
            Cart cart = cartList.get(position);
            holder.cart = cart;
            holder.tv_retailer.setText(cart.getRetailerName());

            SelectedCart sc = selectedCartList.get(position);

            holder.ll_copyitem.removeAllViews();
            holder.ll_printitem.removeAllViews();
            holder.cb_selected.setChecked(sc.all_selected);

            //set copy data
            holder.copy_selected = sc.copy_selected;
            List<Copy> copies = cart.getCopyItems();
            if (copies != null) {
                for (int i = 0; i < copies.size(); i++) {
                    PrintCopyView printCopyView = new PrintCopyView(context);
                    printCopyView.setPrintOrderViewListener(holder);
                    holder.ll_copyitem.addView(printCopyView);
                    printCopyView.setCb_selected(sc.copy_selected.get(i).booleanValue());
                    printCopyView.setData(copies.get(i));
                }
            }

            //set printing data
            holder.print_selected = sc.printing_selected;
            List<File> files = cart.getPrintingItems();
            if (files != null) {
                for (int i = 0; i < files.size(); i++) {
                    PrintPrintingView printPrintingView = new PrintPrintingView(context);
                    printPrintingView.setPrintOrderViewListener(holder);
                    holder.ll_printitem.addView(printPrintingView);
                    printPrintingView.setCb_selected(sc.printing_selected.get(i).booleanValue());
                    printPrintingView.setData(files.get(i));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size() + 1;
    }

    public void addData(List<Cart> carts) {
        cartList.addAll(carts);
        selectedCartList.clear();
        for (int i = 0; i < cartList.size(); i++) {
            SelectedCart sc = new SelectedCart();
            sc.all_selected = false;
            selectedCartList.add(sc);
            Cart cart = cartList.get(i);
            sc.copy_selected = new ArrayList<>(Arrays.asList(new Boolean[cart.getCopyItems().size()]));
            Collections.fill(sc.copy_selected, Boolean.FALSE);
            sc.printing_selected = new ArrayList<>(Arrays.asList(new Boolean[cart.getPrintingItems().size()]));
            Collections.fill(sc.printing_selected, Boolean.FALSE);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //1 for footer, 0 for usual
        return (position == cartList.size()) ? 1 : 0;
    }

    public void setFooterView(View view) {
        this.footerView = view;
    }

    public void submitItem() {
        if (orderService == null)
            orderService = BaseRequest.retrofit.create(OrderService.class);
        for (int i = 0; i < selectedCartList.size(); i++) {
            boolean t = false;
            SelectedCart sc = selectedCartList.get(i);
            Cart cart = cartList.get(i);
            List<CopyItem> copyItemList = new ArrayList<>();
            List<PrintingItem> printingItemList = new ArrayList<>();
            //choose selected item
            if (sc.copy_selected != null) {
                for (int j = 0; j < sc.copy_selected.size(); j++) {
                    Copy copy = cart.getCopyItems().get(j);
                    CopyItem copyItem = new CopyItem();
                    copyItem.copyId = Integer.parseInt(copy.getCopyId());
                    //default 1, will change later
                    copyItem.copies = 1;
                    copyItemList.add(copyItem);
                }
                Log.d("ffff", BaseRequest.gson.toJson(copyItemList));
            }

        }
    }
}
