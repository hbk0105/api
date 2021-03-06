package com.rest.api.rabbitMQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("supplierId")
    private String supplierId;

    @JsonProperty("supplierName")
    private String supplierName;

    @JsonProperty("supplierUrl")
    private String supplierUrl;

    @Override
    public String toString() {
        return "CrawlSupplierData [supplierId=" + supplierId + ", supplierName=" + supplierName + ", supplierUrl=" + supplierUrl + "]";
    }
}
