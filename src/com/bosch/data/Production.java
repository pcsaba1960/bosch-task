package com.bosch.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Production implements Serializable {

    private Integer id;
    private Integer pcbId;
    private Integer quantity;
    private Date startDate;
    private Date endDate;

    public void setPcbId(Integer pcbId) {
        this.pcbId = pcbId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPcbId() {
        return pcbId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String toString() {
        return new StringBuilder(pcbId.toString())
                .append("|")
                .append(quantity)
                .append("|")
                .append(startDate)
                .append("|")
                .append(endDate)
                .append("\n")
                .toString();
    }
}
