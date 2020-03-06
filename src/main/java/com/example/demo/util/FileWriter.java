package com.example.demo.util;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriter {


    public static File checkFileExists(String path) throws IOException {

        File log = new File(path);
        try {
            if (log.exists() == false) {
                System.out.println("New File was created.");
                log.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("COULD NOT LOG!!");

        }
        return log;

    }

    public static void writeToErrorFile(String files, Exception ex, String message,String path) throws IOException {

        File log = checkFileExists(path);

        PrintWriter out = new PrintWriter(new java.io.FileWriter(log, true));
        out.append("Filename : " + files + "\n");
        out.append("exception thrown : " + ex + "\n");
        out.append("message : " + message + "\n");
        out.append("===================================\n");
        out.close();


    }

    public static void writeToFileOrTest(String filename, ResponseEntity responseEntity, int size,String path) throws IOException {

        File log = checkFileExists(path);

        PrintWriter out = new PrintWriter(new java.io.FileWriter(log, true));
        out.append("FileName/TestCase : " + filename + "\n");
        out.append("Status code : " + responseEntity.getStatusCode() + "\n");
        out.append("response : " + responseEntity.getBody() + "\n");
        out.append("number of questions " + size + "\n");
        out.append("====================================\n");
        out.close();


    }

}
