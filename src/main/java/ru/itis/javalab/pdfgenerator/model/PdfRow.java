package ru.itis.javalab.pdfgenerator.model;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfRow {
    private Date createdAt;
    private Date formedAt;
    private Date creditedAt;
    private String comment;
    private PdfMemberData memberData;
    private String ip;
}
