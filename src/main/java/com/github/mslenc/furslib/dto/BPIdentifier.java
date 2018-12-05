package com.github.mslenc.furslib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BPIdentifier {
    private MobilePremiseType mobilePremiseType;
    private RealEstatePremiseInfo realEstatePremiseInfo;

    public BPIdentifier() {

    }

    public BPIdentifier(MobilePremiseType mobilePremiseType) {
        setMobilePremiseType(mobilePremiseType);
    }

    public BPIdentifier(RealEstatePremiseInfo realEstatePremiseInfo) {
        setRealEstateBPInfo(realEstatePremiseInfo);
    }

    /**
     * @see #setMobilePremiseType(MobilePremiseType)
     */
    @JsonProperty("PremiseType")
    public MobilePremiseType getMobilePremiseType() {
        return mobilePremiseType;
    }

    /**
     * The mobile premise type is used for premises which don't classify as
     * real estate (the other option is setting realEstateBPInfo).
     * FURS says:
     * <blockquote>
     *     The type of business premises is entered if the person liable
     *     issues invoices in movable business premises:
     *     <ul>
     *         <li><em>A</em> (<tt>MOBILE_OBJECT</tt>) - movable object (e.g. vehicle, movable stand)</li>
     *         <li><em>B</em> (<tt>FIXED_OBJECT</tt>) - object at a permanent location (e.g. market stand, newsstand)</li>
     *         <li><em>C</em> (<tt>INDIVIDUAL_DEVICE</tt>) - individual electronic device
     *             for issuing invoices or pre-numbered invoice book in cases when the person
     *             liable doesn't use other business premises</li>
     *     </ul>
     * </blockquote>
     *
     * @param mobilePremiseType
     */
    @JsonProperty("PremiseType")
    public void setMobilePremiseType(MobilePremiseType mobilePremiseType) {
        this.mobilePremiseType = mobilePremiseType;
    }


    @JsonProperty("RealEstateBP")
    public RealEstatePremiseInfo getRealEstatePremiseInfo() {
        return realEstatePremiseInfo;
    }

    @JsonProperty("RealEstateBP")
    public void setRealEstateBPInfo(RealEstatePremiseInfo realEstateBPInfo) {
        this.realEstatePremiseInfo = realEstateBPInfo;
    }
}
