package ru.itis.javalab.pdfgenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.itis.javalab.pdfgenerator.model.PdfData;
import ru.itis.javalab.pdfgenerator.service.PdfGeneratorService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
public class PdfController {

    private final PdfGeneratorService pdfGeneratorService;

    @Autowired
    public PdfController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseStatus(code = HttpStatus.OK)
    public String generatePdf(@RequestBody HashMap<String,PdfData> pdfData) throws IOException {
        System.out.println(pdfData.get("1").getId());
        pdfGeneratorService.generate(pdfData);
        return "Success";
    }
}
