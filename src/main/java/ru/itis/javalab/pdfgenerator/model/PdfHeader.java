package ru.itis.javalab.pdfgenerator.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfHeader implements Serializable {
    private String institute;
    private String login;
    private Long number;
    private String type;
    private Date date;
}
