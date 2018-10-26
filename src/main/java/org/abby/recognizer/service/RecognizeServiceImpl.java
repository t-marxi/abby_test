package org.abby.recognizer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class RecognizeServiceImpl implements RecognizeService {

    @Value("${file.exe.path}")
    String fileExe;
    @Value("${file.input.folder}")
    String inputFolder;
    @Value("${file.output.folder}")
    String outputFolder;

    @Override
    public File recognizePassport(MultipartFile passportFile) throws Exception {
        long timestamp = getTimestamp();
        String type = getExtention(passportFile);
        File inputFile = new File(inputFolder + "/" + timestamp + "." + type);
        inputFile.createNewFile();
        Files.copy(passportFile.getInputStream(), inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        log.info(String.format("Create file with name %s for received file with name %s", inputFile.getAbsolutePath(), passportFile.getOriginalFilename()));
        File outputFile = new File(outputFolder + "/" + timestamp + ".xml");
        outputFile.createNewFile();
        long start = System.currentTimeMillis();
        Process process = new ProcessBuilder(fileExe, inputFile.getAbsolutePath(), outputFile.getAbsolutePath()).start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new Exception("process was failed");
        }
        long end = System.currentTimeMillis();
        log.info(String.format("Recognizing took %d milliseconds", (end - start)));
        return outputFile;
    }

    private synchronized long getTimestamp() {
        return System.currentTimeMillis();
    }

    public String getExtention(MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        if (split.length < 2) {
            throw new IllegalArgumentException("File name doesn't contain the extend.");
        }
        return split[split.length - 1];
    }
}
