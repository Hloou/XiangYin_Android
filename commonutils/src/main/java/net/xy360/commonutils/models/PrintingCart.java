package net.xy360.commonutils.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jolin on 2016/3/9.
 */
public class PrintingCart extends RealmObject{

    @PrimaryKey
    private String id;

    private File file;

    private int copies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
