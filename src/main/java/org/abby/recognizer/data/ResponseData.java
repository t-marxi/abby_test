package org.abby.recognizer.data;

import lombok.Data;

@Data
public class ResponseData {
    private String firstName;
    private String lastName;
    private String middleName;
    private String dateOfBirth;
    private String placeOfBirth;
    private String series;
    private String number;
    private String issuedBy;
    private String dateOfIssue;
    private String departamentCode;
    private String sex;
}
