package net.xy360.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.xy360.R;

/**
 * Created by jolin on 2016/2/24.
 */
public class CustomTabViewExt extends FrameLayout {

    private LayoutInflater inflater;

    public CustomTabViewExt(Context context) {
        super(context);
        this.inflater = inflater.from(context);
    }

    public CustomTabViewExt(Context context, String iconText, int imageResourceId) {
        this(context);
        View view = inflater.inflate(R.layout.tab_widget, null);
        ((ImageView)view.findViewById(R.id.tab_icon)).setImageResource(imageResourceId);
        ((TextView)view.findViewById(R.id.tab_text)).setText(iconText);
        addView(view);
    }
}
