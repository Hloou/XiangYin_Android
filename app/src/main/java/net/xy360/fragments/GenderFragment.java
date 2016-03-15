package net.xy360.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import net.xy360.R;

/**
 * Created by jolin on 2016/3/15.
 */
public class GenderFragment extends DialogFragment implements View.OnClickListener{

    public interface GenderListener {
        public void selectedGender(int i);
    }

    private GenderListener genderListener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_gender, null);
        view.findViewById(R.id.iv_male).setOnClickListener(this);
        view.findViewById(R.id.iv_female).setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        genderListener = (GenderListener)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        genderListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_male) {
            genderListener.selectedGender(1);
            dismiss();
        } else if (id == R.id.iv_female) {
            genderListener.selectedGender(2);
            dismiss();
        }
    }
}
