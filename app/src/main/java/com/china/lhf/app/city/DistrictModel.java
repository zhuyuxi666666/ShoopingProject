package com.china.lhf.app.city;

/**
 * Created by C罗 on 2016/10/28.
 */
public class DistrictModel {
    private String name;
    private String zipcode;

    public DistrictModel() {
        super();
    }

    public DistrictModel(String name, String zipcode) {
        this.name = name;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
