package net.xy360.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.xy360.R;
import net.xy360.activitys.print.PrintOrderActivity;
import net.xy360.commonutils.imageloader.MyImageLoader;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.CopyCart;
import net.xy360.commonutils.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

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

        private ImageView iv_thumbnail;
        private TextView tv_book, tv_price, tv_retrailer;
        private TextView tv_author, tv_page;
        private ViewPager vp;
        private Button btn_print;
        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_thumbnail = (ImageView)itemView.findViewById(R.id.iv_thumbnail);
            tv_book = (TextView)itemView.findViewById(R.id.tv_book);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_retrailer = (TextView)itemView.findViewById(R.id.tv_retailer);
            tv_author = (TextView)itemView.findViewById(R.id.tv_author);
            tv_page = (TextView)itemView.findViewById(R.id.tv_page);
            btn_print = (Button)itemView.findViewById(R.id.btn_print);
            btn_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Copy copy = mDatas.get(position);
                    //insert cart to database
                    //this will block, will change later
                    RealmHelper.realm.beginTransaction();
                    copy = RealmHelper.realm.copyToRealmOrUpdate(copy);
                    Cart cart = RealmHelper.realm.where(Cart.class).equalTo("retailerId", copy.getRetailerId()).findFirst();
                    //check is inserted
                    if (cart == null) {
                        cart = RealmHelper.realm.createObject(Cart.class);
                        cart.setRetailerId(copy.getRetailerId());
                        cart.setRetailerName(copy.getRetailerName());
                        cart.setCopyItems(new RealmList<CopyCart>());
                        cart.setIsRetailerDelivery(false);
                    }
                    CopyCart copyCart = RealmHelper.realm.where(CopyCart.class).equalTo("id", copy.getRetailerId() + copy.getCopyId()).findFirst();
                    //check is insert
                    if (copyCart == null) {
                        copyCart = RealmHelper.realm.createObject(CopyCart.class);
                        copyCart.setId(copy.getRetailerId() + copy.getCopyId());
                        copyCart.setCopy(copy);
                        copyCart.setCopies(1);
                        cart.getCopyItems().add(copyCart);
                    } else {
                        copyCart.setCopies(copyCart.getCopies() + 1);
                    }
                    RealmHelper.realm.commitTransaction();
                    Intent intent = new Intent(context, PrintOrderActivity.class);
                    context.startActivity(intent);
                }
            });
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
        holder.tv_book.setText(mDatas.get(position).getName());
        holder.tv_price.setText(String.format("%.2f", mDatas.get(position).getPriceInCent() / 100.0));
        holder.tv_page.setText("" + mDatas.get(position).getPageNumber());
        holder.position = position;
        ImageLoader.getInstance().displayImage(mDatas.get(position).getThumbnail(), holder.iv_thumbnail, MyImageLoader.options);
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
