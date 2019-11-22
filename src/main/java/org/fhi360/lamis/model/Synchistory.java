/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user10
 */
@Entity
@Table(name = "synchistory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Synchistory.findAll", query = "SELECT s FROM Synchistory s")
    , @NamedQuery(name = "Synchistory.findByHistoryId", query = "SELECT s FROM Synchistory s WHERE s.historyId = :historyId")
    , @NamedQuery(name = "Synchistory.findByFacilityId", query = "SELECT s FROM Synchistory s WHERE s.facilityId = :facilityId")
    , @NamedQuery(name = "Synchistory.findByFacilityName", query = "SELECT s FROM Synchistory s WHERE s.facilityName = :facilityName")
    , @NamedQuery(name = "Synchistory.findByUploadDate", query = "SELECT s FROM Synchistory s WHERE s.uploadDate = :uploadDate")
    , @NamedQuery(name = "Synchistory.findByNumFilesUploaded", query = "SELECT s FROM Synchistory s WHERE s.numFilesUploaded = :numFilesUploaded")
    , @NamedQuery(name = "Synchistory.findByUploadCompleted", query = "SELECT s FROM Synchistory s WHERE s.uploadCompleted = :uploadCompleted")
    , @NamedQuery(name = "Synchistory.findByUploadTimeStamp", query = "SELECT s FROM Synchistory s WHERE s.uploadTimeStamp = :uploadTimeStamp")})
public class Synchistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "history_id")
    private Long historyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "facility_id")
    private long facilityId;
    @Size(max = 180)
    @Column(name = "facility_name")
    private String facilityName;
    @Column(name = "upload_date")
    @Temporal(TemporalType.DATE)
    private Date uploadDate;
    @Lob
    @Size(max = 65535)
    @Column(name = "tables_uploaded")
    private String tablesUploaded;
    @Column(name = "num_files_uploaded")
    private Integer numFilesUploaded;
    @Column(name = "upload_completed")
    private Integer uploadCompleted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "upload_time_stamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTimeStamp;

    public Synchistory() {
    }

    public Synchistory(Long historyId) {
        this.historyId = historyId;
    }

    public Synchistory(Long historyId, long facilityId, Date uploadTimeStamp) {
        this.historyId = historyId;
        this.facilityId = facilityId;
        this.uploadTimeStamp = uploadTimeStamp;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(long facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getTablesUploaded() {
        return tablesUploaded;
    }

    public void setTablesUploaded(String tablesUploaded) {
        this.tablesUploaded = tablesUploaded;
    }

    public Integer getNumFilesUploaded() {
        return numFilesUploaded;
    }

    public void setNumFilesUploaded(Integer numFilesUploaded) {
        this.numFilesUploaded = numFilesUploaded;
    }

    public Integer getUploadCompleted() {
        return uploadCompleted;
    }

    public void setUploadCompleted(Integer uploadCompleted) {
        this.uploadCompleted = uploadCompleted;
    }

    public Date getUploadTimeStamp() {
        return uploadTimeStamp;
    }

    public void setUploadTimeStamp(Date uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyId != null ? historyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Synchistory)) {
            return false;
        }
        Synchistory other = (Synchistory) object;
        if ((this.historyId == null && other.historyId != null) || (this.historyId != null && !this.historyId.equals(other.historyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.fhi360.lamis.model.Synchistory[ historyId=" + historyId + " ]";
    }
    
}
