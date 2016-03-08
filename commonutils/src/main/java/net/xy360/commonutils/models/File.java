package net.xy360.commonutils.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jolin on 2016/3/2.
 */
public class File extends RealmObject{
    @PrimaryKey
    private String fileId;
    private String fileName;
    private String fileType;
    private int inspaceUserFileId;
    private Date ownedTime;
    private int size;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getInspaceUserFileId() {
        return inspaceUserFileId;
    }

    public void setInspaceUserFileId(int inspaceUserFileId) {
        this.inspaceUserFileId = inspaceUserFileId;
    }

    public Date getOwnedTime() {
        return ownedTime;
    }

    public void setOwnedTime(Date ownedTime) {
        this.ownedTime = ownedTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
