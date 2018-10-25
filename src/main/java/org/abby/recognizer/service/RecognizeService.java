package org.abby.recognizer.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface RecognizeService {

    File recognizePassport(MultipartFile passportFile) throws Exception;
}
