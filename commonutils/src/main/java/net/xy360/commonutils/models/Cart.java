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
    private RealmList<PrintingCart> printingItems;
    private RealmList<CopyCart> copyItems;
    private String retailerPrice;
    private boolean isRetailerDelivery;

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

    public RealmList<PrintingCart> getPrintingItems() {
        return printingItems;
    }

    public void setPrintingItems(RealmList<PrintingCart> printingItems) {
        this.printingItems = printingItems;
    }

    public RealmList<CopyCart> getCopyItems() {
        return copyItems;
    }

    public void setCopyItems(RealmList<CopyCart> copyItems) {
        this.copyItems = copyItems;
    }

    public String getRetailerPrice() {
        return retailerPrice;
    }

    public void setRetailerPrice(String retailerPrice) {
        this.retailerPrice = retailerPrice;
    }

    public boolean getIsRetailerDelivery() {
        return isRetailerDelivery;
    }

    public void setIsRetailerDelivery(boolean isRetailerDelivery) {
        this.isRetailerDelivery = isRetailerDelivery;
    }
}
