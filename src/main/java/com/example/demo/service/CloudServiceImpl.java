package com.example.demo.service;

import com.example.demo.domain.CloudData;
import com.example.demo.externalService.PiSysTokenService;
import com.example.demo.util.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.demo.util.Constants.CLOUD_ERROR_PATH;
import static com.example.demo.util.Constants.CLOUD_REPORT_PATH;
import static com.example.demo.util.Constants.CLOUD_TEST_PATH;
import static com.example.demo.util.Constants.CLOUD_URL;
import static com.example.demo.util.FileWriter.writeToErrorFile;
import static com.example.demo.util.FileWriter.writeToFileOrTest;


@Service
public class CloudServiceImpl implements CloudService {


//    public static final String CLOUD_URL = "http://aggregationservice-pearson-prep-us1-dev.bite.pearsondev.tech/api/cards/cloud";
//    public static String REPORT_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/cloud/log.txt";
//    public static String ERROR_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/cloud/error.txt";
//    public static String TEST_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/testRuns/cloudtestruns.txt";

    @Autowired
    RestClient restClient;

    @Autowired
    PiSysTokenService piSysTokenService;

    public CloudData getCloudData() {

        //change accessToken and DeckId fields if required
        CloudData data = new CloudData();
        data.setAccessToken("ya29.GlvRBp8RFA4jqnBNaRsJ1huqglV3SqRtEaNViYFR8RuMHxAStZCgZ8iNSWJsqyWa1h4NduHbxXwxUzlkqKbhLHUZotNGkK8IO40oyjB75QU486gQQhfkanNGlQRi");
        data.setContextId("Questions");
        data.setCreatorId("Instructor1");
        data.setCreatorPlatform("Mobile");
        data.setCreatoredSource("Gdrive");
        data.setCreatoredType("Auto");
        data.setDeckId("5c90b353fc7bce00104efc00");
        data.setExamDate("");
        data.setExpert(false);
        data.setTitle("");
        data.setType("GoogleDrive");
        data.setUserId("ffffffff59db8f51e4b0c1f4de01b2e2");

        return data;

    }


    @Override
    public void uploadFile() throws IOException {
        int questionCount=0;
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("ASCII A.docx", "1KYV06Y9n2NJ-F64RDUZs82XwcP4EjWvPggOaV71L0DM");
        map.put("ASCII B.docx", "1i_9XlYBl-2CB7OobNzH_mDpaJMNVMe8uKtwly36renY");
        map.put("Bullet1.docx", "18OPg-7AnhMm68znkumKb2ld1bNfZ5HaSFk4-6kzFptc");
        map.put("Bullet2.docx", "1P2xcPORCJtCD-EjVbmZNT5RzrZirg7bvp_zhDjftMRc");
        map.put("Bullet3.docx", "1-4ZfPVgrbiuvZBM5VlzCeaHTZOs-_lKcctOsU9flEh0");
        map.put("Bullet4.docx", "1lHIX5AEhMcmsoqKpnzNbRIPYzXjjYB1iFDp-nDYkIbE");
        map.put("Bullet5.docx", "128iZmatgnKcQEJYMa_BLVCfC24UD5mgpWYaSBnk01Pg");
        map.put("Copy of Demo.docx", "1dcij0HlMdbXgpS4iS-ur7UV0DPk_q6VLMbjm_YMyUTs");
        map.put("Copy of Demo(1).docx", "1kNTVJDYZytgoNqqlqTRgrujGk8OAzcBm5zp4Zypb65c");
        map.put("Demo.docx", "1nBqjPiyhBllBpTUcvodmdRFwzcpO1lArUaqg68INOTs");
        map.put("MySelf.txt", "110oCUZuCYrGwp55Q9FUHR7XIQ2yrDjguL7Ux5S8qtW0");
        map.put("MySight.txt", "1x9-kLJTBSEcUrfgmSs8b1ir5Aofwii460XqnF1GGmO4");
        map.put("sample docx.docx", "1wK_NzzAY-ROdmPRYKsaariCl2-qmysVgnW4jvAHr6sA");
        map.put("Sent to SCS.docx", "1x0OHPlstYiTTJvCNI6AZUlSS90iBEkbEoIshTzbxymA");
        map.put("Sociology Lecture Notes.docx", "1OFxeWAXkGlpH3QgUkYvg2Y6yZ4TzdPCO8M1ft_3jrWY");
        map.put("The Island.docx", "1RO0PaAp5mOeb9x4FTzJBDAPaYG6kHfPsI7lRCbzL9t8");
        map.put("txt.txt", "1E3NFj8ppeteaiaUPTnvs3UrfSCmPWlvJ4K72RqEUJsE");

        CloudData data = getCloudData();

        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("filename " + entry.getKey());
            System.out.println("fileId " + entry.getValue());
            System.out.println("=====================" + "\n");
            data.setFileId(entry.getValue());

            try {
                ResponseEntity<ArrayList> responseEntity = callRestClient(data);
                questionCount+=responseEntity.getBody().size();
                writeToFileOrTest(entry.getKey(), responseEntity, responseEntity.getBody().size(), CLOUD_REPORT_PATH);
            } catch (Exception e) {
                System.out.println("ERROR in file "+entry.getKey());
                writeToErrorFile(entry.getKey(), e, "error occurred", CLOUD_ERROR_PATH);

            }

        }

        System.out.println("question count "+questionCount);
        System.out.println("Finished");
        questionCount=0;


    }

    @Override
    public void runTest() throws IOException {
        int questionCount = 0;


        HashMap<String, String> map2 = new LinkedHashMap<>();

        map2.put("Upload a txt file that conatin a sentence that satisfy the minimum requirment", "1NwIjRVzuovPfqymBaSlyYrED-cqGp_Cp-fnOB2jlVS0");
        map2.put("Upload a docx file that conatin a sentence that satisfy the minimum requirment", "1VLMRdXrq6hFGVvKQNIGFZgqA7h1q9phb6oMVKSZCLKA");
        map2.put("Upload a doc file that conatin a sentence that satisfy the minimum requirment", "1SPiChp_W3LMDCTQuF6FGV315mcSP4O574KLUgSUcDKs");
        //text scenarios
        map2.put("Send a text that contain characters more than 4000", "1-pwrqy2V6RHjo3QCNOo_eThGbYLJxKWF3LATvdSBdF4");
        map2.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and ending with period. Both the sentences need to end with new line", "1g-DneWtdyPAoQdhyenteMrz5Mvu58tosZrOAiPYmw0w");
        map2.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and not ending with period", "1FurrHBl5OVLNeCVEFk0MTWvHUV5p6lZI4gEaLX_loZs");
        map2.put("Create cards using a text that contain new line characters. Text should not contain any bullets or periods. Text should contain only two setences that satisfy minimum requirment. There should be a new line character between two settences", "1t3XwRISljBAtGFwTjLdCQoUqkTP1GgdPpwKAOJUPcUU");
        map2.put("Create cards using a text that contain non ascii characters. Text should contain only one sententece that satisfy the minimum requirment. Sentence need to contain ascii and non ascii chracters", "1YwAgwlF_Wr2S7VFABrh86bifh6LwZppyI7e1CRl18wQ");
        map2.put("Create cards using a text that contain two sentences that start with bullets and ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line", "1hxxW5uvag16YQzUmDBFYaDJAhFxcHjOgQPvR6Lhmcs0");
        map2.put("Create cards using a text that contain two sentences that start with bullets and not ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line", "1SPCIIBtLbUBCkqZD3m5ixmgeKz70euHMPC5IE4pnOCQ");
        map2.put("Create cards using multiple(More than two) sentences(End with period) that not contain any bullets or any new line characters", "17XXiZ4A0lxBC_upa395xx0S3MPlXH3P8cJe0Bc25B64");
        map2.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters.  First sentence  need to satisfy the minimum requirment and second sentence need to not satisfy the minimum requirment", "1B7osFZ0_-4fvsKVs71tlq1CGAFsl7o8hya9EJKOTnuc");
        map2.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters. Both the setences need to satisfy the minimum requirment", "1uC04KkIcA6szrOkdyzYIr6pZtCPnUosCa0G-O28a_1Y");
        //docx files
        map2.put("Create cards using a sentence that not contain any bullets, new line or period. Sentence need to satisfy the minimum requirment", "1sLDr7JtNXjvKUkSIzzuTV69Ko5rl5_MRkcvkge_Imb0");
        map2.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and ending with period. Both the sentences need to end with new line-docx", "12AXwsRrVOcghD_iCFnWt2qcSuKYShB9An7tUrosbLoM");
        map2.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and not ending with period-docx", "1HrUBJ5IrMU2ejJWnbcYtS4QAW8pffRiCnARmaSGA284");
        map2.put("Create cards using a text that contain new line characters. Text should not contain any bullets or periods. Text should contain only two setences that satisfy minimum requirment. There should be a new line character between two settences-docx", "1mxgeWs5SHqtON-IszZfnZGzULu74m556igoAbqlq2mg");
        map2.put("Create cards using a text that contain non ascii characters. Text should contain only one sententece that satisfy the minimum requirment. Sentence need to contain ascii and non ascii chracters-docx", "1IAgD3zv9RXu-CwHurVtxutFEjiVM8BDVNkBclXP2y0Q");
        map2.put("Create cards using a text that contain two sentences that start with bullets and ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line-docx", "1XLhs8sf7znH-aO9TaG2fsu7jHsMxNdV5It7H1SQ9MAE");
        map2.put("Create cards using a text that contain two sentences that start with bullets and not ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line-docx", "1oPdjBnuDRrrSCAvjcj0cY5IhvS3FZYCTlfkzscavHhk");
        map2.put("Create cards using multiple(More than two) sentences(End with period) that not contain any bullets or any new line characters-docx", "1lrrQqxlXosGsr2hy6rQgSle3WgiPTBqObN2D2j64PiY");
        map2.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters.  First sentence  need to satisfy the minimum requirment and second sentence need to not satisfy the minimum requirment-docx", "1Aqi7w5aX5Sk13NBbFmZGofKSgeGny6-F84oeGl7BZlI");
        map2.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters. Both the setences need to satisfy the minimum requirment-docx", "1YwgEiX43Yhpn95Re2ZXbgNcqN-SAiINnXBH1R3GJPIk");
        map2.put("Send a text that contain characters more than 4000-docx", "19Sb8RDDpjmUWaoNI-gtyBr23GiA1Uie9zDtBkQ1tWq8");
        //doc files
        map2.put("Create cards using a sentence that not contain any bullets, new line or period. Sentence need to satisfy the minimum requirment-doc", "1ybrNTb1LYK1C1TdYpR_N-hqyT1c217WnvMXC62aDM6k");
        map2.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and ending with period. Both the sentences need to end with new line-doc", "13dFp3I3MvpfzM11jYDux23_eAynJLLaUd2wFgpF9VKU");
        map2.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and not ending with period-doc", "1qGemn5B4Uhhg_lnJVlkCpySqNIcMyQeYlmv89hGvZuU");
        map2.put("Create cards using a text that contain new line characters. Text should not contain any bullets or periods. Text should contain only two setences that satisfy minimum requirment. There should be a new line character between two settences-doc", "1sN-Y4K7LaYg-ocmBL16AgLoAeOTMDfxQvhqMNGTZ8Gk");
        map2.put("Create cards using a text that contain non ascii characters. Text should contain only one sententece that satisfy the minimum requirment. Sentence need to contain ascii and non ascii chracters-doc", "1gfIgPyS31OMWdTmmtb1jdiR4kTI6uzou_rWsOiJXX38");
        map2.put("Create cards using a text that contain two sentences that start with bullets and ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line-doc", "16CFclLz2AllbjrgGa8MvE7Sctvw3Aj_w26_HsRXZJk8");
        map2.put("Create cards using a text that contain two sentences that start with bullets and not ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line-doc", "1KrFGgUrbLA8b_wdiyvxLuwXarg2h67Nnq9jzNsj3ji4");
        map2.put("Create cards using multiple(More than two) sentences(End with period) that not contain any bullets or any new line characters-doc", "1uEegOAgCbZCYG0MMPlyU2qBmGAwRyi-YG740fjAlW-U");
        map2.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters.  First sentence  need to satisfy the minimum requirment and second sentence need to not satisfy the minimum requirment-doc", "1MJrosAJRRRwna67l5MdE3sW7yPoY9m0eubKea_Svkn8");
        map2.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters. Both the setences need to satisfy the minimum requirment-doc", "1bP25FqqgCmxpKp1NjD6AsrZqEHJFveSSZPWq4bkzlAI");
        map2.put("Send a text that contain characters more than 4000-doc", "14V9yDmu-SoW4PwpNb8QMooGAC-iHE5L2oZ-LED7TafU");
        //image
        // map2.put("image file","1VNfBnerm5KaSZ3L-wUYSNZmQ53XNramv");
        //video
        //map2.put("video file","1VNfBnerm5KaSZ3L-wUYSNZmQ53XNramv");
        //pdf
        //map2.put("pdf", "1Hn9f9nXfv2FRQvbQ-oBUgm7vwzg5t4C6mPINiWyXuo0");
        //map2.put("1mb file","1yGR1bU1_4bYVC-60YbLZ4SVsdb4QKWIEwEXD6z5MEjs");


        CloudData data = getCloudData();

        for (Map.Entry<String, String> entry : map2.entrySet()) {

            System.out.println("Test Case " + entry.getKey());
            System.out.println("fileId " + entry.getValue());
            System.out.println("=====================" + "\n");
            data.setFileId(entry.getValue());

            try {
                ResponseEntity<ArrayList> responseEntity = callRestClient(data);
                writeToFileOrTest(entry.getKey(), responseEntity, responseEntity.getBody().size(), CLOUD_TEST_PATH);
                questionCount += responseEntity.getBody().size();
            } catch (ResourceAccessException | AuthenticationException e) {
                //System.out.println(responseEntity);
                System.out.println("ERROR ON FILE " + entry.getKey() + " with exception : " + e + "\n");
                System.out.println("====================================================");
                //questionCount+=responseEntity.getBody().size();
            }


        }
        System.out.println("total questions " + questionCount);
        System.out.println("finish");
        questionCount = 0;

    }


    public ResponseEntity<ArrayList> callRestClient(CloudData data) throws AuthenticationException {
        return restClient.restExchange(CLOUD_URL, piSysTokenService.getPiSysToken(), data, HttpMethod.POST, ArrayList.class);
    }

}
