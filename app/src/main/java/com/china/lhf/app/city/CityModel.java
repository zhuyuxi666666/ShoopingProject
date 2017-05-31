package com.china.lhf.app.city;

import java.util.List;

/**
 * 市
 */
public class CityModel {

    private String name;
    private List<DistrictModel> districtList;

    public CityModel() {

    }

    public CityModel(String name, List<DistrictModel> districtList) {
        this.name = name;
        this.districtList = districtList;
    }

    @Override
    public String toString() {
        return "CityModel [name=" + name + ", districtList=" + districtList + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DistrictModel> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictModel> districtList) {
        this.districtList = districtList;
    }

}
