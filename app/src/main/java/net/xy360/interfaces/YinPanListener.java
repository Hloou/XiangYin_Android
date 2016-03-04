package net.xy360.interfaces;

/**
 * Created by jolin on 2016/3/2.
 */
public interface YinPanListener {
    void getFilesViaLabels(String labelId);

    //0 for hide, else for show
    void showWidget(int show);
}
