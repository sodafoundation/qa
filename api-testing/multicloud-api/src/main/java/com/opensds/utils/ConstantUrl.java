package com.opensds.utils;

/**
 *  In this class set URL
 */
public class ConstantUrl {
    private static ConstantUrl mConstantUrl;
    private static  String URL = null;
    private static  String PORT_TENANT_ID = null;
    private static  String PORT = null;

    private ConstantUrl() {
        PORT_TENANT_ID = getPortTenantId();
        PORT = getPort();
        URL = getHostIp();
    }

    public static ConstantUrl getInstance() {
        Logger.logString("**********************************************************************");
        if (mConstantUrl == null) {
            mConstantUrl = new ConstantUrl();
        }
        return mConstantUrl;
    }

    /**
     * Get port: This port used in there is used tenant id url
     */
    public String getPortTenantId() {
        return System.getenv("PORT_TENANT_ID");
    }

    /**
     * Get Port: This port used in S3 services url except login or auth related url.
     */
    public String getPort() {
        return System.getenv("PORT");
    }

    /**
     * Get Host Ip.
     */
    public String getHostIp() {
        return "http://" + System.getenv("HOST_IP");
    }
}
