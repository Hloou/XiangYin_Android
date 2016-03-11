package net.xy360.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.xy360.R;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.FileService;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jolin on 2016/3/11.
 */
public class YinPanRenameFragment extends DialogFragment {

    public interface RenameListener {
        public void rename(String name);
    }

    private File file;
    private RenameListener mListener;
    private EditText et_name;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_yin_pan_rename, null);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_name.setText(file.getFileName());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.yinpan_rename)
                .setView(view)
                .setPositiveButton(R.string.yinpan_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.rename(et_name.getText().toString());
                    }
                })
                .setNegativeButton(R.string.yinpan_cancel, null);
        return builder.create();
    }


    public void setFileName(File file, RenameListener listener) {
        this.file = file;
        this.mListener = listener;
    }
}
