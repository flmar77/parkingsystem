package com.parkit.parkingsystem.model;

public class DataBaseConfig {
    private final String dataBaseUrl;
    private final String dataBaseUsername;
    private final String dataBasePassword;

    public DataBaseConfig(String dataBaseUrl, String dataBaseUsername, String dataBasePassword) {
        this.dataBaseUrl = dataBaseUrl;
        this.dataBaseUsername = dataBaseUsername;
        this.dataBasePassword = dataBasePassword;
    }

    public String getDataBaseUrl() {
        return dataBaseUrl;
    }

    public String getDataBaseUsername() {
        return dataBaseUsername;
    }

    public String getDataBasePassword() {
        return dataBasePassword;
    }


}
