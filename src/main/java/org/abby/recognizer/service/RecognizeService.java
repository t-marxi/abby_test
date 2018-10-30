package org.abby.recognizer.service;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface RecognizeService {

    @lombok.Data
    class Data {
        private final String error;
        private final File file;
        public Data(File file){
            this.file = file;
            error = null;
        }
        public Data(String error) {
            this.error = error;
            file = null;
        }
        public Data(File file, String error) {
            this.file = file;
            this.error = error;
        }
    }

    Data recognizePassport(MultipartFile passportFile) throws Exception;
}
