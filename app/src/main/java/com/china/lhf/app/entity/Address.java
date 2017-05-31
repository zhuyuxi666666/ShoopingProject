package com.china.lhf.app.entity;

/**
 *  实现Comparable接口
 */
public class Address extends BaseBean implements Comparable<Address> {
    private String consignee;
    private String phone;
    private String addr;
    private String zipCode;
    private Boolean isDefault;

    public Address() {

    }

    public Address(String consignee, String phone, String addr, String zipCode, Boolean isDefault) {
        this.consignee = consignee;
        this.phone = phone;
        this.addr = addr;
        this.zipCode = zipCode;
        this.isDefault = isDefault;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public int compareTo(Address another) {
        //比较是否是默认字段
        return another.getIsDefault().compareTo(this.getIsDefault());
    }

}
