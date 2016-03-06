package net.xy360.commonutils.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jolin on 2016/3/5.
 */
public class Cart extends RealmObject{
    @PrimaryKey
    private String retailerId;
    private String retailerName;
    private RealmList<File> printintItems;
    private RealmList<Copy> copyItems;

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public RealmList<File> getPrintintItems() {
        return printintItems;
    }

    public void setPrintintItems(RealmList<File> printintItems) {
        this.printintItems = printintItems;
    }

    public RealmList<Copy> getCopyItems() {
        return copyItems;
    }

    public void setCopyItems(RealmList<Copy> copyItems) {
        this.copyItems = copyItems;
    }
}
