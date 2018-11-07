package com.sztouyun.advertisingsystem.model.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class OrganizationArchive extends BaseOrganization {

    @Column(nullable = false ,updatable = false)
    private Date archiveTime = new Date();

    public Date getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(Date archiveTime) {
        this.archiveTime = archiveTime;
    }
}
