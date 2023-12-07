package com.muslimlife.app.data.model.parameters;

public class LoadCitiesParameters {

    private String country_id;

    public LoadCitiesParameters(String countryId) {
        this.country_id = countryId;
    }

    public String getCountryId() {
        return country_id;
    }

    public void setCountryId(String country_id) {
        this.country_id = country_id;
    }
}

