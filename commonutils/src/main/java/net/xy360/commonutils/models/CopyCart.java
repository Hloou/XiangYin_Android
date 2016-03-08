package net.xy360.commonutils.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jolin on 2016/3/8.
 */
public class CopyCart extends RealmObject {

    @PrimaryKey
    private String id;

    private Copy copy;

    private int copies;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
