package ru.itis.javalab.pdfgenerator.model;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfMemberData {
    private String familyName;
    private String givenName;
    private String middleName;
    private String position;
}
