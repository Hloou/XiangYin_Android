package net.xy360.commonutils.models;

import java.util.List;

/**
 * Created by jolin on 2016/3/4.
 */
public class Order {
    public String orderId;
    public String retailerId;
    public String retailerName;
    public Integer totalPriceInCent;
    public List<PrintingItem> printingItems;
    public List<CopyItem> copyItems;
}
