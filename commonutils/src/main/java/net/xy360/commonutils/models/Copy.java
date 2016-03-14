package net.xy360.commonutils.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jolin on 2016/2/28.
 */
public class Copy extends RealmObject{
    @PrimaryKey
    private String copyId;
    private String name;
    private String samplePicture;
    private int priceInCent;
    private String retailerId;
    private String retailerName;
    private String retailerAddress;
    private String longitude;
    private String latitude;
    private boolean isOpen;
    private String description;
    private int pageNumber;
    private int paperSpecificationId;
    private int paperBindingId;
    private boolean isForSale;
    private boolean isValid;
    private String thumbnail;

    public String getCopyId() {
        return copyId;
    }

    public void setCopyId(String copyId) {
        this.copyId = copyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSamplePicture() {
        return samplePicture;
    }

    public void setSamplePicture(String samplePicture) {
        this.samplePicture = samplePicture;
    }

    public int getPriceInCent() {
        return priceInCent;
    }

    public void setPriceInCent(int priceInCent) {
        this.priceInCent = priceInCent;
    }

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

    public String getRetailerAddress() {
        return retailerAddress;
    }

    public void setRetailerAddress(String retailerAddress) {
        this.retailerAddress = retailerAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPaperSpecificationId() {
        return paperSpecificationId;
    }

    public void setPaperSpecificationId(int paperSpecificationId) {
        this.paperSpecificationId = paperSpecificationId;
    }

    public int getPaperBindingId() {
        return paperBindingId;
    }

    public void setPaperBindingId(int paperBindingId) {
        this.paperBindingId = paperBindingId;
    }

    public boolean getIsForSale() {
        return isForSale;
    }

    public void setIsForSale(boolean isForSale) {
        this.isForSale = isForSale;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }



}
