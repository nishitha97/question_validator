package com.example.demo.util;

public final class Constants {

    //CHANGE PATHS HERE
    public static final String LOCAL_FILEPATH = "/home/user/Desktop/api-testing/file-read/src/main/resources/docs/";
    public static final String AGGREGATION_URL = "http://aggregationservice-pearson-prep-us1-dev.bite.pearsondev.tech/api/aggregated_questions/text";
    public static final String LOCAL_REPORT_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/local/log.txt";
    public static final String LOCAL_ERROR_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/local/error.txt";
    public static final String LOCAL_TEST_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/testRuns/testruns.txt";

    public static final String CLOUD_URL = "http://aggregationservice-pearson-prep-us1-dev.bite.pearsondev.tech/api/cards/cloud";
    public static final String CLOUD_REPORT_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/cloud/log.txt";
    public static final String CLOUD_ERROR_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/cloud/error.txt";
    public static final String CLOUD_TEST_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/testRuns/cloudtestruns.txt";

    public final class Headers {
        private Headers() {
        }

        public static final String X_AUTHORIZATION = "X-Authorization";
        public static final String PIID = "orgExtPiid";
        public static final String CORRELATIONID = "correlation-id";
        public static final String X_TENANT_ID = "x-tenantId";
    }

    private Constants() {
    }
}
