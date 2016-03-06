package net.xy360.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.Label;
import net.xy360.interfaces.YinPanListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jolin on 2016/3/1.
 */
public class YinPanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<File> fileList = null;
    private List<Label> labelList = null;
    private List<Boolean> selectedList = null;
    private int selectedCount = 0;

    private YinPanListener yinPanListener = null;

    private ViewPager lastViewPager = null;

    class TabViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private int position;
        public TabViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (yinPanListener != null)
                        yinPanListener.getFilesViaLabels(labelList.get(position).userLabelId);
                    hideLastViewPager();
                }
            });
        }
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private ViewPager vp;
        private ImageView iv_icon;
        private TextView tv_name, tv_time, tv_size;
        private CheckBox cb_selected;
        private int position;
        public FileViewHolder(View itemView) {
            super(itemView);
            vp = (ViewPager)itemView.findViewById(R.id.viewPager);
            vp.setAdapter(new WenKuPagerAdapter());
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position == 0) {
                        lastViewPager = null;
                    } else {
                        hideLastViewPager();
                        lastViewPager = vp;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            tv_size = (TextView)itemView.findViewById(R.id.tv_size);
            cb_selected = (CheckBox)itemView.findViewById(R.id.cb_selected);
            cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedList.set(position, isChecked);
                    if (isChecked)
                        selectedCount++;
                    else
                        selectedCount = selectedCount == 0 ? 0 : --selectedCount;
                    if (yinPanListener != null)
                        yinPanListener.showWidget(selectedCount);

                }
            });
        }
    }


    public YinPanAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
        labelList = new ArrayList<>();
        fileList = new ArrayList<>();
        selectedList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (position < labelList.size()) ? 0 : 1;
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
        int i = getItemViewType(position);
        if (i == 0) {
            TabViewHolder myViewHolder = (TabViewHolder)holder;
            myViewHolder.position = position;
            myViewHolder.tv_name.setText(labelList.get(position).description);
        } else if (i == 1) {
            position = position - labelList.size();
            FileViewHolder myViewHolder = (FileViewHolder)holder;
            File file = fileList.get(position);
            myViewHolder.position = position;
            myViewHolder.tv_name.setText(file.getFileName());
            myViewHolder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getOwnedTime()));
            myViewHolder.tv_size.setText("" + file.getSize() / 1024);
            if (file.getFileType().equals("pdf"))
                myViewHolder.iv_icon.setImageResource(R.mipmap.pdf);
            else if (file.getFileType().equals("doc"))
                myViewHolder.iv_icon.setImageResource(R.mipmap.word);
            myViewHolder.cb_selected.setChecked(selectedList.get(position));
        }
    }

    public void setYinPanListener(YinPanListener yinPanListener) {
        this.yinPanListener = yinPanListener;
    }

    public void setLabelList(List<Label> list) {
        labelList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFileList(List<File> list) {
        selectedCount = 0;
        if (yinPanListener != null)
            yinPanListener.showWidget(selectedCount);
        fileList.clear();
        fileList.addAll(list);
        selectedList.clear();
        List<Boolean> lb = new ArrayList<>(Arrays.asList(new Boolean[list.size()]));
        Collections.fill(lb, Boolean.FALSE);
        selectedList.addAll(lb);
        notifyDataSetChanged();

    }

    public void hideLastViewPager() {
        if (lastViewPager != null)
            lastViewPager.setCurrentItem(0, true);
        lastViewPager = null;
    }

    @Override
    public int getItemCount() {
        return labelList.size() + fileList.size();
    }

    public List<File> getSelectedFile() {
        List<File> list = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            if (selectedList.get(i).booleanValue() == true)
                list.add(fileList.get(i));
        }
        return list;
    }
}
