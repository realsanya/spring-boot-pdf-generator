package ru.itis.javalab.pdfgenerator.model;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfData implements Serializable {
    //Я бы предпочел jackson
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "header")
    private PdfHeader header;
    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "element")
    private List<PdfRow> rows;
}
