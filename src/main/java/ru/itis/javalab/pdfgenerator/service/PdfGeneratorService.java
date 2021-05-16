package ru.itis.javalab.pdfgenerator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;

import org.springframework.stereotype.Service;
import ru.itis.javalab.pdfgenerator.model.PdfData;
import ru.itis.javalab.pdfgenerator.model.PdfHeader;
import ru.itis.javalab.pdfgenerator.model.PdfRow;
import ru.itis.javalab.pdfgenerator.util.Footer;
import ru.itis.javalab.pdfgenerator.util.PdfPageXofEventHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


@Service
public class PdfGeneratorService {
    // Про application.properties забыли? Откуда вдруг тут статические значения
    private static final String EXTENSION = ".pdf";
    private static final String IMAGE_PATH = "src/main/resources/logo.jpeg";
    private static final String FONT = "src/main/resources/arial.ttf";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final SimpleDateFormat dateFormatter1 = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    private PdfPCell createCell(String phrase, Integer type) throws IOException, DocumentException {
        /**
         * 0 - header, далее номер ряда, начиная с 1
         * если type четный, то фон белый, иначе серый
         **/
        BaseFont bf=BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font f1;
        if (type == 0) {
            f1 = new Font(bf,9, Font.BOLD);
        } else {
            f1 = new Font(bf,8, Font.NORMAL);
        }

        PdfPCell cell = new PdfPCell(new Phrase(phrase, f1));

        if (type % 2 == 0){
            cell.setBackgroundColor(GrayColor.WHITE);
        } else {
            cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
        }
        cell.setMinimumHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private List createHeader(PdfData pdfData, Font f) {
        PdfHeader pdfHeader = pdfData.getHeader();
        List list = new List(List.PARAGRAPH);

        list.setListSymbol("");

        // TODO tab add
        list.add(new ListItem(new Phrase("Институт: " + pdfHeader.getInstitute(), f)));
        list.add(new ListItem(new Phrase("Логин: "+ pdfHeader.getLogin(), f)));
        list.add(new ListItem(new Phrase("Количество студентов: "+ pdfData.getRows().size(), f)));
        list.add(new ListItem(new Phrase("Номер отчёта: "+ pdfHeader.getNumber(), f)));
        list.add(new ListItem(new Phrase("Тип отчёта: "+ pdfHeader.getType(), f)));

        Paragraph paragraph2 = new Paragraph("Перечень участников конференции " + dateFormatter1.format(pdfHeader.getDate()) + ":", f);
        paragraph2.setSpacingBefore(10);
        paragraph2.setSpacingAfter(20);

        list.add(new ListItem(paragraph2));

        return list;
    }

    // Метод очень большой, дебажить его очень сложно. Молодец, что вынесла создание ячейки и заголовка, но лучше ещё больше раздробить и раскидать по классам.
    // Если в коде начинаешь комментариями отделять логические состовляющие метода, то это уже сигнал того, что метод перегружен
    public void create(Document document, PdfWriter writer, PdfData pdfData) throws IOException, DocumentException {
        BaseFont bf=BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{1, 3});

        /* -------------------------------- img -------------------------------- */
        Image image = Image.getInstance(IMAGE_PATH);
        PdfPCell imgCell = new PdfPCell(image, true);
        imgCell.setBorder(Rectangle.NO_BORDER);
        imgCell.setPaddingBottom(30);

        /* -------------------------------- id -------------------------------- */
        PdfPCell pCell = new PdfPCell();
        Font f1 = new Font(bf,10, Font.NORMAL);
        Paragraph paragraph1 = new Paragraph("Подготовленный отчет по данным \n по № " + pdfData.getId(), f1);
        paragraph1.setAlignment(Element.ALIGN_RIGHT);
        pCell.addElement(paragraph1);
        pCell.setVerticalAlignment(Element.ALIGN_TOP);
        pCell.setBorder(Rectangle.NO_BORDER);

        headerTable.addCell(imgCell);
        headerTable.addCell(pCell);
        document.add(headerTable);


        /* ------------------------------- header ------------------------------- */
        Font f2 = new Font(bf,10, Font.NORMAL);
        document.add(createHeader(pdfData, f2));


        /* ------------------------------- table ------------------------------- */
        float[] columnWidths = {2, 2, 2, 3, 3, 2};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);


        table.addCell(createCell("Сформирован", 0));
        table.addCell(createCell("Оформлен",0));
        table.addCell(createCell("Зачислил", 0));
        table.addCell(createCell("Комментарий", 0));
        table.addCell(createCell("ФИО, Должность", 0));
        table.addCell(createCell("IP-адрес", 0));

        table.setHeaderRows(6);

        int length = pdfData.getRows().size();
        // Фу, for
        for (int counter = 0; counter < length; counter ++) {
            PdfRow row = pdfData.getRows().get(counter);
            Integer type = counter + 1;
            table.addCell(createCell(dateFormatter.format(row.getCreatedAt()), type));
            table.addCell(createCell(dateFormatter.format(row.getFormedAt()), type));
            table.addCell(createCell(dateFormatter.format(row.getCreditedAt()), type));
            table.addCell(createCell(row.getComment(), type));
            table.addCell(createCell(row.getMemberData().getFamilyName() + " " + row.getMemberData().getGivenName()
                    + " " + row.getMemberData().getMiddleName() + "\n" + row.getMemberData().getPosition(), type));
            table.addCell(createCell(row.getIp(), type));
        }

        document.add(table);

        Font f3 = new Font(bf,9, Font.NORMAL);
        Paragraph paragraph2 = new Paragraph("Примечание: время указано в часовом поясе MSK (UTC+3) в соответствии системными часами сервера или APM.", f3);
        document.add(paragraph2);

        /* ------------------------------ footer ------------------------------ */
        PdfPTable footerTable = new PdfPTable(1);
        Footer footer = new Footer(footerTable);
        footer.setTableFooter(writer);
        document.add(footerTable);

        document.newPage();
    }


    public void generate(HashMap<String, PdfData> hashMap) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = null;

        try {
            writer = PdfWriter.getInstance(document,
                    new FileOutputStream("pdf/" + "string" + PdfGeneratorService.EXTENSION));


            writer.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
            writer.setPageEvent(new PdfPageXofEventHelper());
            document.open();

            for (Map.Entry<String, PdfData> pdfData: hashMap.entrySet()) {
                create(document, writer, pdfData.getValue());
            }

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
            writer.close();
        }
    }
}
