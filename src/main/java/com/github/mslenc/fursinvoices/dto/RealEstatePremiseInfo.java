package com.github.mslenc.fursinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RealEstatePremiseInfo {
    private PropertyId propertyId;
    private PropertyAddress address;

    @JsonProperty("PropertyID")
    public PropertyId getPropertyId() {
        return propertyId;
    }

    @JsonProperty("PropertyID")
    public RealEstatePremiseInfo setPropertyId(PropertyId propertyId) {
        if (propertyId == null)
            throw new IllegalArgumentException("null propertyId");

        this.propertyId = propertyId;

        return this;
    }


    @JsonProperty("Address")
    public PropertyAddress getAddress() {
        return address;
    }

    @JsonProperty("Address")
    public RealEstatePremiseInfo setAddress(PropertyAddress address) {
        if (address == null)
            throw new IllegalArgumentException("null address");

        this.address = address;

        return this;
    }
}
