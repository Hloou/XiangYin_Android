package net.xy360.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Retailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jolin on 2016/3/4.
 */
public class RetailerAdapter extends RecyclerView.Adapter<RetailerAdapter.MyViewHolder>{

    private List<Retailer> retailerList;
    private LayoutInflater inflater;
    private int lastSelected = -1;
    private RadioButton last_button = null;

    public RetailerAdapter(Context context) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.retailerList = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        RadioButton rb_selected;
        TextView tv_name, tv_address;
        int position;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            rb_selected = (RadioButton)itemView.findViewById(R.id.rb_selected);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_address = (TextView)itemView.findViewById(R.id.tv_address);
            rb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (last_button != null)
                            last_button.setChecked(false);
                        lastSelected = position;
                        last_button = rb_selected;
                    } else {
                        lastSelected = -1;
                        last_button = null;
                    }
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_retailer, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.position = position;
        holder.rb_selected.setChecked(position == lastSelected);
        holder.tv_name.setText(retailerList.get(position).retailerName);
        holder.tv_address.setText(retailerList.get(position).retailerAddress);
    }

    @Override
    public int getItemCount() {
        return retailerList.size();
    }

    public void addRetailerList(List<Retailer> list) {
        int i = retailerList.size();
        retailerList.addAll(list);
        notifyItemRangeInserted(i, list.size());
    }

    public Retailer getSelectedRetailer() {
        if (lastSelected == -1)
            return null;
        return retailerList.get(lastSelected);
    }
}
