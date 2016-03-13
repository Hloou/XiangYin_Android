package net.xy360.adapters;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jolin on 2016/3/13.
 */
public class YinPanTrashAdapter extends RecyclerView.Adapter<YinPanTrashAdapter.MyViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<File> fileList;
    private List<Boolean> selectedList;

    public YinPanTrashAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.fileList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_name, tv_time, tv_size;
        private CheckBox cb_selected;
        private File file;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            tv_size = (TextView)itemView.findViewById(R.id.tv_size);
            cb_selected = (CheckBox)itemView.findViewById(R.id.cb_selected);
            cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int index = fileList.indexOf(file);
                    selectedList.set(index, isChecked);
                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.item_yin_pan_trash, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        File file = fileList.get(position);
        holder.file = file;
        holder.tv_name.setText(file.getFileName());
        holder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getOwnedTime()));
        holder.tv_size.setText(file.getSize() / 1024 + "KB");
        if (file.getFileType() != null) {
            if (file.getFileType().equals("pdf"))
                holder.iv_icon.setImageResource(R.mipmap.pdf);
            else if (file.getFileType().equals("doc"))
                holder.iv_icon.setImageResource(R.mipmap.word);
        }
        holder.cb_selected.setChecked(selectedList.get(position));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void setFileList(List<File> files) {
        this.fileList.clear();
        this.fileList.addAll(files);
        this.selectedList.clear();
        List<Boolean> lb = new ArrayList<>(Arrays.asList(new Boolean[files.size()]));
        Collections.fill(lb, Boolean.FALSE);
        selectedList.addAll(lb);
        notifyDataSetChanged();
    }

    public List<File> getSelectedFile() {
        List<File> list = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            if (selectedList.get(i).booleanValue() == true)
                list.add(fileList.get(i));
        }
        return list;
    }

    public void removeFile(File file) {
        int index = fileList.indexOf(file);
        fileList.remove(index);
        selectedList.remove(index);
        notifyDataSetChanged();
    }

    public void allSelected() {
        selectedList.clear();
        List<Boolean> lb = new ArrayList<>(Arrays.asList(new Boolean[fileList.size()]));
        Collections.fill(lb, Boolean.TRUE);
        selectedList.addAll(lb);
        notifyDataSetChanged();
    }
}
