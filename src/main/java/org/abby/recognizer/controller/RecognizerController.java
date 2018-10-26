package org.abby.recognizer.controller;

import lombok.extern.slf4j.Slf4j;
import org.abby.recognizer.service.RecognizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
        File result = recognizeService.recognizePassport(passportFile);
        Path path = Paths.get(result.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(result.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
