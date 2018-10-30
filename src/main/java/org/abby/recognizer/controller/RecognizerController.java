package org.abby.recognizer.controller;

import lombok.extern.slf4j.Slf4j;
import org.abby.recognizer.service.RecognizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
public class RecognizerController {

    @Autowired
    RecognizeService recognizeService;

    @GetMapping("/test")
    public ResponseEntity testServer() {
        log.info("Receive test request.");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("Test is success");
    }

    @PostMapping("/recognize-passport")
    public ResponseEntity uploadFile(
            @RequestParam("file") MultipartFile passportFile) throws Exception {
        log.info("Received file for recognizing with name " + passportFile.getOriginalFilename());
        RecognizeService.Data data = recognizeService.recognizePassport(passportFile);
        if (data.getError() != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(data.getError());
        }
        File result = data.getFile();
        Path path = Paths.get(result.getAbsolutePath());
        byte[] byteArray = Files.readAllBytes(path);
        if (byteArray.length == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not be recognized");
        }
        ByteArrayResource resource = new ByteArrayResource(byteArray);
        return ResponseEntity.ok()
                .contentLength(result.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
