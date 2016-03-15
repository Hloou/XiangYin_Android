package net.xy360.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.internal.ResponseParsers;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import net.xy360.R;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.FileService;
import net.xy360.commonutils.internetrequest.interfaces.OSSService;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.Label;
import net.xy360.commonutils.models.OSSCredential;
import net.xy360.commonutils.models.OSSFile;
import net.xy360.commonutils.models.PaperBindingPrice;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.contants.Download;
import net.xy360.fragments.YinPanRenameFragment;
import net.xy360.interfaces.YinPanListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jolin on 2016/3/1.
 */
public class YinPanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<File> fileList = null;
    private List<Label> labelList = null;
    private List<Boolean> selectedList = null;
    //private int selectedCount = 0;

    //private YinPanListener yinPanListener = null;

    private ViewPager lastViewPager = null;

    private UserId userId;

    private FileService fileService = null;
    private OSSService ossService = null;

    class TabViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private int position;
        public TabViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (yinPanListener != null)
                    //    yinPanListener.getFilesViaLabels(labelList.get(position).userLabelId);
                    hideLastViewPager();
                }
            });
        }
    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, YinPanRenameFragment.RenameListener{
        private ViewPager vp;
        private ImageView iv_icon;
        private TextView tv_name, tv_time, tv_size;
        private CheckBox cb_selected;
        private File file;
        private OSSFile ossFile;
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
                    if (position == 0) {
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
                    int index = fileList.indexOf(file);
                    selectedList.set(index, isChecked);
                }
            });
            itemView.findViewById(R.id.ll_rename).setOnClickListener(this);
            itemView.findViewById(R.id.ll_download).setOnClickListener(this);
            itemView.findViewById(R.id.ll_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.ll_rename) {
                YinPanRenameFragment yinPanRenameFragment = new YinPanRenameFragment();
                yinPanRenameFragment.setFileName(file, this);
                yinPanRenameFragment.show(((AppCompatActivity)context).getSupportFragmentManager() , "rename");

            } else if (id == R.id.ll_download) {
                download();
            } else if (id == R.id.ll_delete) {
                delete();
            }
        }

        public void delete() {
            final File f = file;
            fileService.deleteFile(userId.userId, "" + f.getInspaceUserFileId(), userId.token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            BaseRequest.ErrorResponse(context, e);
                        }

                        @Override
                        public void onNext(String s) {
                            int index = fileList.indexOf(f);
                            fileList.remove(index);
                            selectedList.remove(index);
                            notifyItemRemoved(labelList.size() + index);
                        }
                    });

        }

        public void download() {
            if (ossService == null)
                ossService = BaseRequest.retrofit.create(OSSService.class);
            File f = file;
            ossService.getFilePathViaId(f.getFileId())
                    .flatMap(new Func1<OSSFile, Observable<OSSCredential>>() {
                        @Override
                        public Observable<OSSCredential> call(OSSFile ossFile) {
                            Log.d("download", BaseRequest.gson.toJson(ossFile));
                            FileViewHolder.this.ossFile = ossFile;
                            return ossService.getOSSCredential(userId.userId, userId.token);
                        }
                    })
                    .map(new Func1<OSSCredential, String>() {
                        @Override
                        public String call(OSSCredential ossCredential) {
                            Log.d("download", BaseRequest.gson.toJson(ossCredential));
                            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossCredential.AccessKeyId, ossCredential.AccessKeySecret, ossCredential.SecurityToken);
                            OSS oss = new OSSClient(context, ossFile.ossEndPoint, credentialProvider);
                            GetObjectRequest get = new GetObjectRequest(ossFile.ossBucketName, ossFile.ossFileName);
                            String result = "success";
                            try {
                                GetObjectResult getResult = oss.getObject(get);
                                java.io.File directory = new java.io.File(Environment.getExternalStorageDirectory() + Download.FILE_PATH);
                                directory.mkdir();
                                java.io.File file = new java.io.File(directory, ossFile.ossFileName);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                InputStream inputStream = getResult.getObjectContent();
                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while ((len = inputStream.read(buffer)) != -1) {
                                    Log.d("download", "" + len);
                                    fileOutputStream.write(buffer, 0, len);
                                }
                                fileOutputStream.close();
                                Log.d("download", getResult.getMetadata().getContentType());
                            } catch (ClientException e) {
                                Log.d("download", e.getMessage());
                                result = e.getMessage();
                            } catch (ServiceException e) {
                                Log.d("download", e.getMessage());
                                result = e.getMessage();
                            } catch (FileNotFoundException e) {
                                Log.d("download", e.getMessage());
                                result = e.getMessage();
                            } catch (IOException e) {
                                Log.d("download", e.getMessage());
                                result = e.getMessage();
                            }
                            return result;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("download", e.getMessage());
                        }

                        @Override
                        public void onNext(String s) {
                            if (s.equals("success"))
                                Toast.makeText(context, context.getString(R.string.yinpan_finish_download), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, context.getString(R.string.yinpan_error_download), Toast.LENGTH_SHORT).show();
                            Log.d("download", "success");
                        }
                    });
        }

        @Override
        public void rename(final String name) {
            final File f = file;
            fileService.renameFile(userId.userId, f.getInspaceUserFileId(), userId.token, name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            f.setFileName(name);
                            int index = fileList.indexOf(f);
                            notifyItemChanged(labelList.size() + index);
                        }

                        @Override
                        public void onError(Throwable e) {
                            BaseRequest.ErrorResponse(context, e);
                        }

                        @Override
                        public void onNext(String s) {
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
        userId = UserData.load(context, UserId.class);
        fileService = BaseRequest.retrofit.create(FileService.class);
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
            myViewHolder.tv_name.setText(labelList.get(position).labelDescription);
        } else if (i == 1) {
            position = position - labelList.size();
            FileViewHolder myViewHolder = (FileViewHolder)holder;
            File file = fileList.get(position);
            myViewHolder.file = file;
            myViewHolder.tv_name.setText(file.getFileName());
            myViewHolder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.getOwnedTime()));
            myViewHolder.tv_size.setText(file.getSize() / 1024 + "KB");
            if (file.getFileType() != null) {
                if (file.getFileType().equals("pdf"))
                    myViewHolder.iv_icon.setImageResource(R.mipmap.pdf);
                else if (file.getFileType().equals("doc"))
                    myViewHolder.iv_icon.setImageResource(R.mipmap.word);
            }
            myViewHolder.cb_selected.setChecked(selectedList.get(position));
        }
    }

    /*public void setYinPanListener(YinPanListener yinPanListener) {
        this.yinPanListener = yinPanListener;
    }*/

    public void setLabelList(List<Label> list) {
        labelList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFileList(List<File> list) {
        //selectedCount = 0;
        //if (yinPanListener != null)
         //   yinPanListener.showWidget(selectedCount);
        fileList.clear();
        fileList.addAll(list);
        selectedList.clear();
        List<Boolean> lb = new ArrayList<>(Arrays.asList(new Boolean[list.size()]));
        Collections.fill(lb, Boolean.FALSE);
        selectedList.addAll(lb);
        notifyDataSetChanged();

    }

    public void addFile(File file) {
        fileList.add(file);
        selectedList.add(Boolean.FALSE);
        notifyItemInserted(labelList.size() + fileList.size() - 1);
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

    public void removeFile(File file) {
        int index = fileList.indexOf(file);
        fileList.remove(index);
        selectedList.remove(index);
        notifyDataSetChanged();
    }

    public void notSelectedAll() {
        selectedList.clear();
        List<Boolean> lb = Arrays.asList(new Boolean[fileList.size()]);
        Collections.fill(lb, Boolean.FALSE);
        selectedList.addAll(lb);
        notifyDataSetChanged();
    }

    private int sortNameOrder = 1;
    public void sortByName() {
        sortNameOrder = -sortNameOrder;
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return sortNameOrder * lhs.getFileName().compareTo(rhs.getFileName());
            }
        });
        notifyDataSetChanged();
    }

    private int sortDateOrder = 1;
    public void sortByDate() {
        sortDateOrder = -sortDateOrder;
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return sortDateOrder * lhs.getOwnedTime().compareTo(rhs.getOwnedTime());
            }
        });
        notifyDataSetChanged();
    }

}
