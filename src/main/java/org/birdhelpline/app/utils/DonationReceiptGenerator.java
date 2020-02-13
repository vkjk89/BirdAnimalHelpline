package org.birdhelpline.app.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DonationReceiptGenerator {


    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        try {
            Document document = new Document();
            FileOutputStream fileOutputStream = new FileOutputStream("receipt.pdf");
            PdfWriter.getInstance(document, fileOutputStream);

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            // Build the data-model
            Map<String, Object> data = new HashMap<>();
            data.put("name", "Vimal");
            data.put("mode", "Mode");
            data.put("bank", "Bank");
            data.put("date", "09/02/2020");
            data.put("id", "123");
            data.put("address", "TestAddr");
            data.put("contact", "902192121");
            data.put("pan", "ajasasal");
            data.put("amount", "1213311");
            NumberToWord obj = new NumberToWord();
            data.put("amountWords", obj.convertNumberToWords(1213311));


            /* ------------------------------------------------------------------------ */
            /* You should do this ONLY ONCE in the whole application life-cycle:        */

            /* Create and adjust the configuration singleton */
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
            // Recommended settings for new projects:
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);

            /* ------------------------------------------------------------------------ */
            /* You usually do these for MULTIPLE TIMES in the application life-cycle:   */


            /* Get the template (uses cache internally) */
            Template template = cfg.getTemplate("ReceiptTemplate.txt");

            /* Merge data-model with template */
            StringWriter stringWriter = new StringWriter();
            template.process(data, stringWriter);

            Chunk chunk = new Chunk(stringWriter.toString(), font);
            Paragraph paragraph = new Paragraph(chunk);

            document.add(paragraph);
            document.close();
            // Note: Depending on what `out` is, you may need to call `out.close()`.
            // This is usually the case for file output, but not for servlet output.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
