package com.parkit.parkingsystem.model;

public class Credentials {
    private final String datasourceUsername;
    private final String datasourcePassword;

    public Credentials(String datasourceUsername, String datasourcePassword) {
        this.datasourceUsername = datasourceUsername;
        this.datasourcePassword = datasourcePassword;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }


}
