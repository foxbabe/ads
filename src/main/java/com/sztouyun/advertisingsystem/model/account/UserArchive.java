package com.sztouyun.advertisingsystem.model.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Riber on 2017/8/21 0021.
 */
@Entity
public class UserArchive extends BaseUser {
    @Column(nullable = false,updatable = false)
    private Date archiveTime = new Date();

    public Date getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(Date archiveTime) {
        this.archiveTime = archiveTime;
    }
}
