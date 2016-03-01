package net.xy360.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.xy360.R;

/**
 * Created by jolin on 2016/3/1.
 */
public class YinPanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    class TabViewHolder extends RecyclerView.ViewHolder {
        public TabViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private ViewPager vp;
        public FileViewHolder(View itemView) {
            super(itemView);
            vp = (ViewPager)itemView.findViewById(R.id.viewPager);
            vp.setAdapter(new WenKuPagerAdapter());
        }
    }


    public YinPanAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return (position > 5) ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TabViewHolder(inflater.inflate(R.layout.item_yin_pan_tab, parent, false));
        } else if (viewType == 1) {
            return new FileViewHolder(inflater.inflate(R.layout.item_yin_pan, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 10;
    }
}
