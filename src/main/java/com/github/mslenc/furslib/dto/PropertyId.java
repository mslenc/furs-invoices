package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mslenc.furslib.validation.IntegerValidator;

public class PropertyId {
    private Integer cadastralNumber;
    private Integer buildingNumber;
    private Integer buildingSectionNumber;

    public PropertyId() {

    }

    public PropertyId(int cadastralNumber, int buildingNumber, int buildingSectionNumber) {
        setCadastralNumber(cadastralNumber);
        setBuildingNumber(buildingNumber);
        setBuildingSectionNumber(buildingSectionNumber);
    }

    /**
     * @see #setCadastralNumber(Integer)
     */
    @JsonProperty("CadastralNumber")
    public Integer getCadastralNumber() {
        return cadastralNumber;
    }

    /**
     * The number of the cadastral community of the building/premise.
     */
    @JsonProperty("CadastralNumber")
    public PropertyId setCadastralNumber(Integer cadastralNumber) {
        this.cadastralNumber = CADASTRAL_NUMBER.validate(cadastralNumber);
        return this;
    }

    /**
     * @see #setBuildingNumber(Integer)
     */
    @JsonProperty("BuildingNumber")
    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    /**
     * The number of the building/premise within the cadastral community.
     */
    @JsonProperty("BuildingNumber")
    public PropertyId setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = BUILDING_NUMBER.validate(buildingNumber);
        return this;
    }

    /**
     * @see #setBuildingSectionNumber(Integer)
     */
    @JsonProperty("BuildingSectionNumber")
    public Integer getBuildingSectionNumber() {
        return buildingSectionNumber;
    }

    /**
     * The number of the section within the building.
     */
    @JsonProperty("BuildingSectionNumber")
    public PropertyId setBuildingSectionNumber(Integer buildingSectionNumber) {
        this.buildingSectionNumber = BUILDING_SECTION_NUMBER.validate(buildingSectionNumber);
        return this;
    }

    private static final
    IntegerValidator CADASTRAL_NUMBER = new IntegerValidator("cadastralNumber", 0, 9999);

    private static final
    IntegerValidator BUILDING_NUMBER = new IntegerValidator("buildingNumber", 0, 99999);

    private static final
    IntegerValidator BUILDING_SECTION_NUMBER = new IntegerValidator("buildingSectionNumber", 0, 9999);
}
