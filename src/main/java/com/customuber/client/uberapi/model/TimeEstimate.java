package com.customuber.client.uberapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeEstimate {
    @JsonProperty("localized_display_name")
    private String localizedDisplayName;

    @JsonProperty("estimate")
    private int estimate;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("product_id")
    private String productId;

    public String getLocalizedDisplayName() {
        return localizedDisplayName;
    }

    public void setLocalizedDisplayName(String localizedDisplayName) {
        this.localizedDisplayName = localizedDisplayName;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
