package net.xy360.commonutils.models;

import java.util.Date;
import java.util.List;

/**
 * Created by jolin on 2016/3/4.
 */
public class Order {
    public int orderId;
    public String retailerId;
    public String retailerName;
    public int totalPriceInCent;
    public int actualPriceInCent;
    public int paymentMethod;
    public boolean isRetailerDelivery;
    public int deliveryAddressId;
    public String remark;
    public int status;
    public Date userSubmittedTime;
    public Date userPaidTime;
    public Date retailerAcceptedTime;
    public Date retailerPrintedTime;
    public Date retailerDeliveriedTime;
    public Date completedTime;
    public Date userCancelledTime;
    public Date abortedTime;

    public List<PrintingOrder> printingItems;
    public List<CopyOrder> copyItems;
}
