package net.xy360.commonutils.models;

import java.util.Date;

/**
 * Created by jolin on 2016/3/4.
 */
public class RetailerField {
    public String retailerId;
    public int totalPriceInCent;
    public int actualPriceInCent;
    public int paymentMethod;
    public boolean isRetailerDelivery;
    public String deliveryAddressId;
    public String remark;
    public int status;
    public Date userSubmittedTime;
    public Date userPaidTime;
}
