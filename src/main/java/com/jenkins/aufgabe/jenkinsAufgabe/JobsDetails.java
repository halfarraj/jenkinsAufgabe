package com.jenkins.aufgabe.jenkinsAufgabe;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class bring the jobs from the API of Jenkins who they have more than
 * one year did not activated and delete them .
 */

public class JobsDetails {
    public JobsDetails() {

    }

    /**
     * this Method open the communication with Jenkins
     * @param theLinkOfRestApiOfJenkins
     * @return the general API
     */
    public StringBuffer connection(String theLinkOfRestApiOfJenkins)  {
        StringBuffer response = null;

        try {
            URL urlOfRestApiOfJenkins = new URL(theLinkOfRestApiOfJenkins);

            HttpURLConnection connectionWithRestApiOfJenkins = (HttpURLConnection) urlOfRestApiOfJenkins.openConnection();
            connectionWithRestApiOfJenkins.setRequestMethod("GET");
            connectionWithRestApiOfJenkins.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader readFromRestApiOfJenkins = new BufferedReader(new InputStreamReader(connectionWithRestApiOfJenkins.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = readFromRestApiOfJenkins.readLine()) != null) {
                response.append(inputLine);
            }
            readFromRestApiOfJenkins.close();

            connectionWithRestApiOfJenkins.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;

    }

    /**
     * this Method used to get the Job's names from the Jenkins API
     * @return a Map who contain the Job's names
     * @throws Exception
     */
    public Map<Integer, String> connectToJenkinsToGetTheJobs() throws Exception {

        String theLinkOfRestApiOfJenkins = "http://localhost:8081/api/json?pretty=true/build?token=11721fa3feee5fa01aa1859ec4e21577a9";
        connection(theLinkOfRestApiOfJenkins);

        JSONObject objectFromTheRestApiOfJenkinsToGetTheJobs = new JSONObject(connection(theLinkOfRestApiOfJenkins).toString());
        JSONArray objectArrayFromTheRestApiOfJenkins = objectFromTheRestApiOfJenkinsToGetTheJobs.getJSONArray("jobs");

        Map<Integer, String> jobsNames = new HashMap<Integer, String>();

        for (int i = 0; i < objectArrayFromTheRestApiOfJenkins.length(); i++) {
            JSONObject objectFromTheRestApiOfJenkinsToGetTheJobsNames = objectArrayFromTheRestApiOfJenkins.getJSONObject(i);
            jobsNames.put(i, objectFromTheRestApiOfJenkinsToGetTheJobsNames.getString("name"));
        }

        return jobsNames;
    }

    /**
     * this Method used to get the last Build of each Job
     * @param jobName
     * @return the specific API of each Last-Build-Job
     */
    public StringBuffer connectToJenkinsToGetLastBuild(String jobName) {
        String theLinkOfRestApiOfJenkins = "http://localhost:8081/job/" + jobName + "/api/json?pretty=true/build?token=11721fa3feee5fa01aa1859ec4e21577a9";
        connection(theLinkOfRestApiOfJenkins);
        return connection(theLinkOfRestApiOfJenkins);
    }

    /**
     * this Method used to get the last build number of each Job
     * @param link
     * @return Last Build Number Of each Job
     */
    public StringBuffer connectToJenkinsToGetLastBuildNumberOfTheJob(String link)  {

        connection(link);

        return connection(link);

    }

    /**
     * this Method used to get the timestamp of each last build of each Job
     * @return the timestamp for each last build of each Job
     * @throws Exception
     */
    public Map<String, String> jobsAndTimestamp() throws Exception {

        Map<String, String> jobsNamesAndTimestamp = new HashMap<String, String>();

        for (Map.Entry m : connectToJenkinsToGetTheJobs().entrySet()) {

            String jobName = (String) m.getValue();
            System.out.println("The name of the Job is : " + jobName);
            connectToJenkinsToGetLastBuild(jobName);
            JSONObject objectFromTheRestApiOfJenkinsToGetTheJobsLastBuild = new JSONObject(connectToJenkinsToGetLastBuild(jobName).toString());
            JSONArray objectArrayFromTheRestApiOfJenkinsOfJobs = objectFromTheRestApiOfJenkinsToGetTheJobsLastBuild.getJSONArray("builds");
            JSONObject objectFromTheRestApiOfJenkinsToGetTheJobsLastBuildNumber = objectArrayFromTheRestApiOfJenkinsOfJobs.getJSONObject(0);
            System.out.println("_______________________________________");
            jobsNamesAndTimestamp.put(objectFromTheRestApiOfJenkinsToGetTheJobsLastBuildNumber.getString("url"), jobName);
        }

        return jobsNamesAndTimestamp;
    }

    /**
     * this Method used to convert the Timestamp to a date
     * @param timestamp
     * @return a date
     */
    private Date convertTimestampToDate(Long timestamp) {

        Date timestampToDate = new Timestamp(timestamp);

        return timestampToDate;
    }

    /**
     * this Method used to delete the Jobs who they have since more than one year did not build
     * @throws Exception
     */
    public void deleteJobs() throws Exception {

        for (Map.Entry m : jobsAndTimestamp().entrySet()) {
            String theLinkOfRestApiOfJenkinsTheLastBuildOfTheJob = m.getKey() + "api/json?pretty=true/build?token=11721fa3feee5fa01aa1859ec4e21577a9";
            connectToJenkinsToGetLastBuildNumberOfTheJob(theLinkOfRestApiOfJenkinsTheLastBuildOfTheJob);


            JSONObject objectFromLastBuildOfTheJob = new JSONObject(connectToJenkinsToGetLastBuildNumberOfTheJob(theLinkOfRestApiOfJenkinsTheLastBuildOfTheJob).toString());
            Long timestamp = objectFromLastBuildOfTheJob.getLong("timestamp");

            Date currentTime = new Date();
            String jobNameToDelete;
            if (currentTime.getYear() - convertTimestampToDate(timestamp).getYear() >= 1 || currentTime.getMonth() - convertTimestampToDate(timestamp).getMonth() > 12) {

                jobNameToDelete = m.getValue().toString();

                System.out.println("The job woh will be delete is : " + jobNameToDelete);
                Runtime.getRuntime().exec("curl -X POST http://husaen88:123456@localhost:8081/job/" + jobNameToDelete + "/doDelete");

            } else System.out.println("Gibt keine mehr alte Jobs ");


        }
    }
}