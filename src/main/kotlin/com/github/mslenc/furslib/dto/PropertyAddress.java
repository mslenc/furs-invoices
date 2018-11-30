package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.validation.StringValidator;

import static com.github.mslenc.furslib.validation.StringValidator.CharsAllowed.ANY;
import static com.github.mslenc.furslib.validation.StringValidator.CharsAllowed.DIGITS_ONLY;
import static com.github.mslenc.furslib.validation.StringValidator.NullEmptyMode.*;

public class PropertyAddress {
    private String street;
    private String houseNumber;
    private String houseNumberAdditional;
    private String community;
    private String city;
    private String postalCode;

    public PropertyAddress() {

    }


    @JsonProperty("Street")
    public String getStreet() {
        return street;
    }

    @JsonProperty("Street")
    public PropertyAddress setStreet(String street) {
        this.street = STREET.validate(street);
        return this;
    }


    @JsonProperty("HouseNumber")
    public String getHouseNumber() {
        return houseNumber;
    }

    @JsonProperty("HouseNumber")
    public PropertyAddress setHouseNumber(String houseNumber) {
        this.houseNumber = HOUSE_NUMBER.validate(houseNumber);
        return this;
    }


    @JsonProperty("HouseNumberAdditional")
    public String getHouseNumberAdditional() {
        return houseNumberAdditional;
    }

    @JsonProperty("HouseNumberAdditional")
    public PropertyAddress setHouseNumberAdditional(String houseNumberAdditional) {
        this.houseNumberAdditional = HOUSE_NUMBER_ADDITIONAL.validate(houseNumberAdditional);
        return this;
    }


    @JsonProperty("Community")
    public String getCommunity() {
        return community;
    }

    @JsonProperty("Community")
    public PropertyAddress setCommunity(String community) {
        this.community = COMMUNITY.validate(community);
        return this;
    }


    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public PropertyAddress setCity(String city) {
        this.city = CITY.validate(city);
        return this;
    }


    @JsonProperty("PostalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("PostalCode")
    public PropertyAddress setPostalCode(String postalCode) {
        this.postalCode = POSTAL_CODE.validate(postalCode);
        return this;
    }


    private static final StringValidator
    STREET = new StringValidator("street", 1, 100, ANY, NO_NULLS);

    private static final StringValidator
    HOUSE_NUMBER = new StringValidator("houseNumber", 1, 10, ANY, NO_NULLS);

    private static final StringValidator
    HOUSE_NUMBER_ADDITIONAL = new StringValidator("houseNumberAdditional", 1, 10, ANY, EMPTY_TO_NULL);

    private static final StringValidator
    COMMUNITY = new StringValidator("community", 1, 100, ANY, NO_NULLS);

    private static final StringValidator
    CITY = new StringValidator("city", 1, 100, ANY, NO_NULLS);

    private static final StringValidator
    POSTAL_CODE = new StringValidator("postalCode", 4, 4, DIGITS_ONLY, NO_NULLS);
}
