package ru.itis.javalab.pdfgenerator.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class Footer extends PdfPageEventHelper {
    public static PdfPTable footer;

    public Footer(PdfPTable footer) {
        this.footer = footer;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        footer.writeSelectedRows(0, -1, 38, 50, writer.getDirectContent());
    }

    public void setTableFooter(PdfWriter writer) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(520f);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(-2f);
        table.addCell(cell);
        Footer event = new Footer(table);
        writer.setPageEvent(event);
    }

}
