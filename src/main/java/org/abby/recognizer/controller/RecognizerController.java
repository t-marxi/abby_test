package org.abby.recognizer.controller;

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


@RestController
public class RecognizerController {

    @Autowired
    RecognizeService recognizeService;

    @GetMapping("/test")
    public ResponseEntity testServer() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("Test is success");
    }

    @PostMapping("/recognize-passport")
    public ResponseEntity uploadFile(
            @RequestParam("file") MultipartFile passportFile) throws Exception {
        File result = recognizeService.recognizePassport(passportFile);
        Path path = Paths.get(result.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(result.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
