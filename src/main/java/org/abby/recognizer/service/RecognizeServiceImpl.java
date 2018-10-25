package org.abby.recognizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

@Service
public class RecognizeServiceImpl implements RecognizeService {

    @Value("file.exe.path")
    String fileExe;
    @Value("file.input.folder")
    String inputFolder;
    @Value("file.output.folder")
    String outputFolder;

    @Override
    public File recognizePassport(MultipartFile passportFile) throws Exception {
        long timestamp = getTimestamp();
        int type = passportFile.getName().lastIndexOf(".");
        File inputFile = new File(inputFolder + "/" + timestamp + "." + type);
        inputFile.createNewFile();
        File outputFile = new File(outputFolder + "/" + timestamp + ".xml");
        outputFile.createNewFile();
        Files.copy(passportFile.getInputStream(), inputFile.toPath());
        Process process = new ProcessBuilder(fileExe, inputFile.getAbsolutePath(), outputFile.getAbsolutePath()).start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new Exception("process was failed");
        }
        return outputFile;
    }

    private synchronized long getTimestamp() {
        return System.currentTimeMillis();
    }
}
