package pl.remindapp;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.remindapp.cvObjects.Person;

@Getter
@Setter
@AllArgsConstructor
public class PdfGenerator {
    private Person user;
    private String filePath;
    private int templateNumber;

    public boolean generateCv(){
        boolean result = true;
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();

            document.add(new Paragraph(user.getName() + " " + user.getSurname()));
            document.add(new Paragraph(user.getAddress().toString()));
            System.out.println(user.getAddress().toString());
            document.add(new Paragraph(user.getEmailAddress()));
            document.add(new Paragraph(user.getPhoneNumber()));
            document.close();
        }
        catch (DocumentException e){
            e.printStackTrace();
            result = false;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
