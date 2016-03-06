package net.xy360.interfaces;

import android.view.View;

/**
 * Created by jolin on 2016/3/6.
 */
public interface PrintOrderViewListener {
    //type 0 for copy, 1 for printing
    public void delete(View view, int type, Object o);
}
