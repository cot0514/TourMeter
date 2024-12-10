package com.example.project;

public class SettingData {

    private String id;
    private String address;
    private String city;
    private String district;

    public SettingData(String id, String address, String city, String district) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }
}
