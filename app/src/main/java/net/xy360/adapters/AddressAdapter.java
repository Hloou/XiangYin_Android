package net.xy360.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jolin on 2016/3/14.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Address> addressList;

    public AddressAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        addressList = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_phone, tv_address;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView)itemView.findViewById(R.id.tv_phone);
            tv_address = (TextView)itemView.findViewById(R.id.tv_address);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.tv_name.setText(address.receiverName);
        holder.tv_phone.setText(address.receiverTelephone);
        holder.tv_address.setText(address.receiverAddress);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void setAddressList(List<Address> list) {
        this.addressList.clear();
        this.addressList.addAll(list);
        notifyDataSetChanged();
    }
}
