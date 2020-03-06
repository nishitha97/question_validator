package com.example.demo.service;

import com.example.demo.domain.Data;
import com.example.demo.domain.Question;
import com.example.demo.externalService.PiSysTokenService;
import com.example.demo.util.RestClient;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.util.Constants.AGGREGATION_URL;
import static com.example.demo.util.Constants.LOCAL_ERROR_PATH;
import static com.example.demo.util.Constants.LOCAL_FILEPATH;
import static com.example.demo.util.Constants.LOCAL_REPORT_PATH;
import static com.example.demo.util.Constants.LOCAL_TEST_PATH;
import static com.example.demo.util.FileWriter.writeToErrorFile;
import static com.example.demo.util.FileWriter.writeToFileOrTest;


@Service
public class DataServiceImpl implements DataService {


//    public static String FILEPATH = "/home/user/Desktop/api-testing/file-read/src/main/resources/docs/";
//    public static String AGGREGATION_URL = "http://aggregationservice-pearson-prep-us1-dev.bite.pearsondev.tech/api/aggregated_questions/text";
//    public static String REPORT_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/local/log.txt";
//    public static String ERROR_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/local/error.txt";
//    public static String TEST_PATH = "/home/user/Desktop/api-testing/file-read/src/main/reports/testRuns/testruns.txt";

    String text = "";
    String text1 = "";

    @Autowired
    RestClient restClient;

    @Autowired
    PiSysTokenService piSysTokenService;

    public Data getData() {
        Question question = new Question();
        question.setImageUrl("http://somehost/someimg.jpg");
        question.setMedia("TEXT");
        question.setPromptType("TEXT");
        question.setTimeOut("200");

        Data data = new Data();
        data.setCreatorId("sffs");
        data.setCreatorPlatform("Mobile");
        data.setCreatoredSource("Clipper");
        data.setCreatoredType("Auto");
        data.setDeckId("5c9b0cd3fc7bce00104efc02");
        data.setExpert(false);
        data.setQuestion(question);
        data.setTitle("Title");
        data.setUserId("ffffffff59db8f51e4b0c1f4de01b2e2");
        return data;

    }


    @Override
    public void addFile() {
        int questionCount=0;

        Data data = getData();


        try {
            final File folder = new File(LOCAL_FILEPATH);

            List<String> filelist = listFilesForFolder(folder);
            List<String> documentList = new ArrayList<>();
            List<String> textList = new ArrayList<>();


            for (String s : filelist) {
                String extension = getExtensionByStringHandling(s).get();
                if (extension.equals("docx")) {
                    documentList.add(s);
                } else if (extension.equals("txt")) {
                    textList.add(s);
                }
            }

            //read all documents
            System.out.println("======================== reading all documents ======================== \n");
            for (String files : documentList) {

                System.out.println("====== started reading " + files + " ======\n");

                File file = new File(LOCAL_FILEPATH + files);
                FileInputStream fis = new FileInputStream(file.getAbsolutePath());

                XWPFDocument document = new XWPFDocument(fis);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);

                String text1=text;
                text=text1 +extractor.getText();
                System.out.println("Calling aggregation service");
                data.setText(text);
                System.out.println(extractor.getText());

                try {
                    ResponseEntity<ArrayList> responseEntity = callRestClient(data);
                    questionCount+=responseEntity.getBody().size();

                    System.out.println("Response code id " + responseEntity.getStatusCode());
                    System.out.println("============ done reading " + files + " ============\n");
                    text = "";
                    writeToFileOrTest(files, responseEntity, responseEntity.getBody().size(), LOCAL_REPORT_PATH);
                } catch (Exception e) {
                    writeToErrorFile(files, e, "UNEXPECTED ERROR IN DOCUMENT FILE " + files, LOCAL_ERROR_PATH);
                    System.out.println("ERROR OCCURRED, SEE ERROR FILE FOR DETAILS");
                    continue;
                }

                fis.close();
            }

            //read all text
            System.out.println("=========================== reading all text files ===========================");
            for (String files : textList) {
                System.out.println("====== started reading " + files + " ======\n");

                String fileName = LOCAL_FILEPATH + files;
                File file = new File(fileName);
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);

                String line;
                while ((line = br.readLine()) != null) {

                    String temp = text1;
                    text1 = temp + line;
                }

                System.out.println("Calling aggregation service");
                data.setText(text1);
                System.out.println(text1);
                try {
                    ResponseEntity<ArrayList> responseEntity = callRestClient(data);
                    System.out.println("Response code id " + responseEntity.getStatusCode());
                    System.out.println("========== done reading " + files + " ==========\n");
                    text1 = "";
                    questionCount+=responseEntity.getBody().size();
                    writeToFileOrTest(files, responseEntity, responseEntity.getBody().size(), LOCAL_REPORT_PATH);
                    System.out.println("done");

                } catch (Exception e) {
                    if (files.equals("txt.txt")) {
                        writeToErrorFile(files, e, "EXPECTED ERROR FOR FILE " + file, LOCAL_ERROR_PATH);
                    } else {
                        writeToErrorFile(files, e, "UNEXPECTED ERROR RESPONSE IN FILE " + files, LOCAL_ERROR_PATH);
                    }
                    System.out.println("ERROR OCCURRED, SEE ERROR FILE FOR DETAILS");
                    continue;
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("total questions "+questionCount);
        questionCount = 0;

    }

    //run test according to test cases
    @Override
    public void runTest() {
        int questionCount = 0;
        Data data = getData();
        HashMap<String, String> map = new LinkedHashMap<>();

        map.put("Create cards using a sentence that not contain any bullets, new line or period. Sentence need to satisfy the minimum requirment.", "The island is home to many cultures");
        map.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters. Both the setences need to satisfy the minimum requirment.", "We are going to create a question and answering mobile application. The mobile application mainly targets students in Sri Lanka");
        map.put("Create cards using two sentences(End with period) that not contain any bullets or new line charcters.  First sentence  need to satisfy the minimum requirment and second sentence need to not satisfy the minimum requirment.", "We are going to create a question and answering mobile application. Hello");
        map.put("Create cards using multiple(More than two) sentences(End with period) that not contain any bullets or any new line characters.", "Then the users who are selected that subject as their talented subjects will see this question and they will answer. That way all the users can ask questions as well as give answers to others questions. Since the application has a rating system for all the answers automatically the best answer will come to the top of the answers.");
        map.put("Create cards using a text that contain two sentences that start with bullets and not ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line", " • Hello my name is Aaa \n" + "    • The island is home to many cultures, languages and ethnicities \n");
        map.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and not ending with period.", "• Hello my name is Aaa \n" + " • I am twenty years old \n" + "  • The island is home to many cultures, languages and ethnicities \n");
        map.put("Create cards using a text that contain two sentences that start with bullets and ending with period. Both the setences need to satisfy the minimum requirment. Both the sentences need to end with new line", "• Hello my name is Aaa. \n" + " • I am twenty years old. \n");
        map.put("Create cards using a text that contain multiple(More than two) sentences that start with bullets and ending with period. Both the sentences need to end with new line", "• Hello my name is Aaa. \n" + " • I am twenty years old. \n" + "  • The island is home to many cultures, languages and ethnicities. \n");
        map.put("Create cards using a text that contain new line characters. Text should not contain any bullets or periods. Text should contain only two setences that satisfy minimum requirment. There should be a new line character between two settences.", " Hello my name is Aaa \n" + "I am twenty years old \n");
        map.put("Create cards using a text that contain non ascii characters. Text should contain only one sententece that satisfy the minimum requirment. Sentence need to contain ascii and non ascii chracters.", " I brought 500£ and ¥2000 at Abc™ ");
        map.put("Send a text that contain characters more than 4000","This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. This rather long paragraph consists of exactly 4,000 characters. Spaces, punctuation marks and carriage returns do count. With regard to those spaces, note that if you typically type a double-space at the end of a sentence as I do before beginning a new sentence in the same paragraph, the second space will be deleted automatically, i.e., all but the first space will be ignored. The total number of words in this paragraph is 733 and the average word length is 4.4 characters. The average number of words per sentence is 22. At the end of this paragraph is simply the alphabet starting with the letter A and ending with the letter H, in bold, to make it come out to exactly 4,000 characters and so you'll know when to stop scrolling down in order to see how big 4,000 character is if you want to add a message to the guestbook. Whew. This paragraph begins to repeat at this point, so you can stop reading now if you want to, which I would suggest, or you can choose to read it all again. ABCDEFGHIJKLMNOP");
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println(entry.getKey());
            data.setText(entry.getValue());
            try {
                ResponseEntity<ArrayList> responseEntity = callRestClient(data);
                writeToFileOrTest(entry.getKey(), responseEntity, responseEntity.getBody().size(), LOCAL_TEST_PATH);
                questionCount += responseEntity.getBody().size();
                System.out.println("finish");
            } catch (Exception e) {
                System.out.println("error " + e);
            }

        }
        System.out.println("total questions " + questionCount);
        questionCount = 0;
    }

    public static List<String> listFilesForFolder(final File folder) {
        List<String> files = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {

                files.add(fileEntry.getName());

            }
        }
        return files;
    }

    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public ResponseEntity<ArrayList> callRestClient(Data data) throws AuthenticationException {
        return restClient.restExchange(AGGREGATION_URL, piSysTokenService.getPiSysToken(), data, HttpMethod.POST, ArrayList.class);
    }
}


