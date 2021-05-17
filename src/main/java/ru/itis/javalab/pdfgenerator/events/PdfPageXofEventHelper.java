package ru.itis.javalab.pdfgenerator.events;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;


public class PdfPageXofEventHelper extends PdfPageEventHelper {

    public PdfTemplate total;
    public BaseFont baseFont;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(500, 500);
        try {
            ClassPathResource cpr = new ClassPathResource("arial.ttf");
            baseFont = BaseFont.createFont(cpr.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new ExceptionConverter(e);

        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte pdfContentByte = writer.getDirectContent();
        pdfContentByte.saveState();
        String text = String.valueOf(writer.getPageNumber());
        float textSize = baseFont.getWidthPoint(text, 9);
        pdfContentByte.beginText();
        pdfContentByte.setFontAndSize(baseFont, 9);

        float x = document.right();
        float y = 20f;
        pdfContentByte.setTextMatrix(x, y);
        pdfContentByte.showText(text);
        pdfContentByte.endText();

        pdfContentByte.addTemplate(total, x + textSize, y);

        pdfContentByte.restoreState();
    }
}
