package net.xy360.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import net.xy360.R;

import rx.Subscription;

/**
 * Created by jolin on 2016/3/14.
 */
public class LoadingFragment extends DialogFragment {

    private Subscription subscription = null;

    public LoadingFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_loading, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        return dialog;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public static LoadingFragment showLoading(FragmentManager fragmentManager) {
        LoadingFragment f = new LoadingFragment();
        f.show(fragmentManager, "loading");
        return f;
    }

    @Override
    public void onDetach() {
        if (subscription != null)
            subscription.unsubscribe();
        super.onDetach();
    }
}
