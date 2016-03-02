package net.xy360.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.Copy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jolin on 2016/2/29.
 */
public class WenKuAdapter extends RecyclerView.Adapter<WenKuAdapter.MyViewHolder> {

    private List<Copy> mDatas = null;
    private Context context;
    private LayoutInflater inflater;
    private ViewPager lastShowViewPager = null;

    public WenKuAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mDatas = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_book, tv_price, tv_retrailer;
        private TextView tv_author, tv_page;
        private ViewPager vp;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_book = (TextView)itemView.findViewById(R.id.tv_book);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_retrailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            tv_author = (TextView)itemView.findViewById(R.id.tv_author);
            tv_page = (TextView)itemView.findViewById(R.id.tv_page);
            vp = (ViewPager)itemView.findViewById(R.id.viewPager);
            vp.setAdapter(new WenKuPagerAdapter());
            itemView.findViewById(R.id.iv_three_dots).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideLastShow();
                    vp.setCurrentItem(1, true);
                    lastShowViewPager = vp;
                }
            });
        }
    }

    public void hideLastShow() {
        if (lastShowViewPager != null) {
            lastShowViewPager.setCurrentItem(0, true);
            lastShowViewPager = null;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_wen_ku, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_book.setText(mDatas.get(position).name);
        holder.tv_price.setText(String.format("%.2f", mDatas.get(position).priceInCent / 100.0));
        holder.tv_page.setText("" + mDatas.get(position).pageNumber);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void addDatas(List<Copy> datas) {
        int i = mDatas.size();
        mDatas.addAll(datas);
        notifyItemRangeInserted(i, datas.size());
    }

    public void setDatas(List<Copy> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
}
