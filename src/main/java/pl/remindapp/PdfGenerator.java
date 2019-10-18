package pl.remindapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.remindapp.cvObjects.LifeEvent;
import pl.remindapp.cvObjects.Person;

import static java.lang.Math.PI;

@Getter
@Setter
public class PdfGenerator {
    private Person user;
    private String filePath;
    private int templateNumber, fontSize;
    private BaseFont urName;
    private Font font;


    public PdfGenerator(Person user, String filePath, int templateNumber, int fontSize) {
        this.user = user;
        this.filePath = filePath;
        this.templateNumber = templateNumber;
        this.fontSize = fontSize;
        try {
            this.urName = BaseFont.createFont("/res/font/font.ttf", BaseFont.IDENTITY_H, BaseFont.CACHED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.font = new Font(urName, fontSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int generateCv(){
        int numberOfPage;
        this.font = new Font(urName, fontSize);
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.setMargins(0,0,0,0);
            document.add(createMainContainer());
            document.close();

            PdfReader reader = new PdfReader(filePath);
            numberOfPage = reader.getNumberOfPages();
            reader.close();
        }
        catch (DocumentException e){
            e.printStackTrace();
            numberOfPage = 0;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            numberOfPage = 0;
        }
        catch(IOException e){
            e.printStackTrace();
            numberOfPage = 0;
        }

        return numberOfPage;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private PdfPTable createMainContainer() {
        float [] widths = {1,2};
        PdfPTable mainContainer = new PdfPTable(widths);
        mainContainer.setWidthPercentage(100);
        mainContainer.addCell(addPrivateDataContainer());
        mainContainer.addCell(addCrucialInfoContainer());
        return mainContainer;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private PdfPCell addCrucialInfoContainer() {
        int i = 0;
        PdfPCell data = new PdfPCell();
        data.setBorder(Rectangle.NO_BORDER);
        data.setPadding(30);

        data.addElement(crucialDataParagraph(user.getShortInfo()));

        if(!user.getExperience().isEmpty()) {
            Paragraph p1 = crucialDataTitle("• DOŚWIADCZENIE");
            data.addElement(p1);
            for (LifeEvent e : user.getExperience()) {
                if(i == 0){
                    Paragraph temp = lifeEventTitleParagraph(e);
                    temp.setSpacingBefore(3);
                    data.addElement(temp);
                }else {
                    data.addElement(lifeEventTitleParagraph(e));
                }
                data.addElement(lifeEventDescriptionParagraph(e));
                i++;
            }
        }

        i=0;
        if(!user.getEducation().isEmpty()) {
            data.addElement(crucialDataTitle("• WYKSZTAŁCENIE"));
            for (LifeEvent e : user.getEducation()) {
                if(i == 0){
                    Paragraph temp = lifeEventTitleParagraph(e);
                    temp.setSpacingBefore(3);
                    data.addElement(temp);
                }else {
                    data.addElement(lifeEventTitleParagraph(e));
                }
                data.addElement(lifeEventDescriptionParagraph(e));
                i++;
            }
        }

        i=0;
        if(!user.getCourses().isEmpty()) {
            data.addElement(crucialDataTitle("• KURSY I SZKOLENIA"));
            for (LifeEvent e : user.getCourses()) {
                if(i == 0){
                    Paragraph temp = lifeEventTitleParagraph(e);
                    temp.setSpacingBefore(3);
                    data.addElement(temp);
                }else {
                    data.addElement(lifeEventTitleParagraph(e));
                }
                data.addElement(lifeEventDescriptionParagraph(e));
                i++;
            }
        }

        if(!user.getSkills().isEmpty()) {
            data.addElement(crucialDataTitle("• UMIEJĘTNOŚCI"));//.setMarginBottom(fontSize));
            list(user.getSkills(), data);
        }

        if(!user.getInterest().isEmpty()) {
            data.addElement(crucialDataTitle("• ZAINTERESOWANIA"));//.setMarginBottom(fontSize));
            list(user.getInterest(), data);
        }

        Paragraph p = new Paragraph("Wyrażam zgodę na przetwarzanie moich danych osobowych dla potrzeb niezbędnych do realizacji" +
                " procesu tej oraz przyszłych rekrutacji (zgodnie z ustawą z dnia 10 maja 2018 roku o ochronie danych osobowych" +
                " (Dz. Ustaw z 2018, poz. 1000) oraz zgodnie z Rozporządzeniem Parlamentu Europejskiego i Rady (UE) 2016/679 z " +
                "dnia 27 kwietnia 2016 r. w sprawie ochrony osób fizycznych w związku z przetwarzaniem danych osobowych " +
                "i w sprawie swobodnego przepływu takich danych oraz uchylenia dyrektywy 95/46/WE (RODO)).");
        if(fontSize > 7) {
            Font tempFont = new Font(urName, 7);
            p.setFont(tempFont);
            p.setLeading(7);
        }
        else{
            if(fontSize > 2) {
                Font tempFont = new Font(urName, fontSize-2);
                p.setFont(tempFont);
                p.setLeading(fontSize - 2);
            }
            else{
                Font tempFont = new Font(urName, 1);
                p.setFont(tempFont);
                p.setLeading(1);
            }
        }
        p.setSpacingBefore(30);
        data.addElement(p);

        return data;
    }

    private void list(List<String> elements, PdfPCell data) {
        Paragraph p1 = new Paragraph(" ");
        Font tempFont;
        if(fontSize > 4)
            tempFont = new Font(urName, fontSize-4);
        else
            tempFont = new Font(urName, 1);
        p1.setFont(tempFont);
        data.addElement(p1);
        for(String e:elements){
            Paragraph p = new Paragraph("- " + e);
            p.setLeading(fontSize);
            p.setFont(font);
            data.addElement(p);
        }
    }

    private Paragraph lifeEventDescriptionParagraph(LifeEvent event) {
        Paragraph lifeEventDescriptionParagraph = new Paragraph();
        if(event.getDescription().equals("")){
            ;//lifeEventDescriptionParagraph.setHeight(0);
        }
        else {
            lifeEventDescriptionParagraph.setFont(font);
            lifeEventDescriptionParagraph.setLeading(fontSize+1);
            lifeEventDescriptionParagraph.setAlignment(Element.ALIGN_LEFT);
            lifeEventDescriptionParagraph.add(event.getDescription());
        }
        return lifeEventDescriptionParagraph;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Paragraph lifeEventTitleParagraph(LifeEvent event) {
        Paragraph lifeEventTitleParagraph = new Paragraph();
        Font tempFont = new Font(urName, fontSize);
        tempFont.setStyle(Font.BOLD);
        lifeEventTitleParagraph.setFont(tempFont);
        lifeEventTitleParagraph.setLeading(fontSize);
        lifeEventTitleParagraph.setAlignment(Element.ALIGN_LEFT);
        lifeEventTitleParagraph.setSpacingBefore(fontSize);
        if(event.getEnd() != null && event.getBegin() != null) {
            lifeEventTitleParagraph.add(event.getTitle() + "   ( " + event.getBegin().
                    format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "  -  " + event.getEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " )");
        }
        else if(event.getBegin() != null){
            lifeEventTitleParagraph.add(event.getTitle() + "   od   " + event.getBegin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        }
        else{
            lifeEventTitleParagraph.add(event.getTitle());
        }

        return lifeEventTitleParagraph;
    }

    private Paragraph crucialDataTitle(String titleText) {
        Paragraph title = new Paragraph();
        Font tempFont = new Font(urName, fontSize+8);
        tempFont.setStyle(Font.BOLD);
        title.setFont(tempFont);
        title.setAlignment(Element.ALIGN_LEFT);
        title.setLeading(fontSize + 8);

        title.add(titleText);

        title.setSpacingBefore(fontSize+4);
        return title;
    }

    private Paragraph crucialDataParagraph(String text) {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(font);
        paragraph.setLeading(fontSize);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        paragraph.add(text);
        return paragraph;
    }

    private PdfPCell addPrivateDataContainer() {
        PdfPCell data = new PdfPCell();
        data.setBorder(Rectangle.NO_BORDER);
        data.setBackgroundColor(new BaseColor(53,53,53));
        data.setMinimumHeight(PageSize.A4.getHeight());
        data.setPaddingTop(100);

        Paragraph birth = titlePersonalData("Data Urodzenia:");
        birth.setSpacingBefore(25);

        Paragraph birthday = personalData(user.getDateOfBirth().toString());

        Paragraph address = titlePersonalData("Adres:");
        address.setLeading(12);

        Paragraph livingPlace = personalData(user.getAddress().toString());
        livingPlace.setLeading(12);

        Paragraph phoneNumberTitle = titlePersonalData("Telefon:");

        Paragraph phoneNumber = personalData(String.valueOf(user.getPhoneNumber()));

        Paragraph emailTitle = titlePersonalData("E-mail:");

        Paragraph email = personalData(user.getEmailAddress());

        nameAndSurname(data);
        Image image;
        byte[] imageBytes = Base64.decode(user.getImageFile());
        try {
            image = Image.getInstance(imageBytes);
            image.scaleToFit(new Rectangle(150,200));
            image.setAlignment(Element.ALIGN_CENTER);
            if(image.getHeight() < image.getWidth())
                 image.setRotation((float)((-PI * user.getRotationAngle()/90.0) / 2.0 ));
            data.addElement(image);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.addElement(birth);
        data.addElement(birthday);
        data.addElement(address);
        data.addElement(livingPlace);
        data.addElement(phoneNumberTitle);
        data.addElement(phoneNumber);
        data.addElement(emailTitle);
        data.addElement(email);

        return data;
    }

    private Paragraph titlePersonalData(String titleText) {
        Paragraph title = new Paragraph(titleText);

        Font tempFont = new Font(urName, 12);
        tempFont.setStyle(Font.BOLD);
        tempFont.setColor(BaseColor.WHITE);

        title.setFont(tempFont);
        //title.setLeading(5);
        title.setIndentationLeft(30);


        //title.setCharacterSpacing((float) 1.6); iText7
        return title;
    }

    private Paragraph personalData(String dataText) {
        Paragraph data = new Paragraph(dataText);

        Font tempFont = new Font(urName, 12);
        tempFont.setColor(BaseColor.WHITE);

        data.setFont(tempFont);
        data.setIndentationLeft(30);

        data.setSpacingAfter(20);
        return data;
    }

    private void nameAndSurname(PdfPCell cell){
        Paragraph name = new Paragraph(user.getName());
        Font tempFont = new Font(urName, 20);
        tempFont.setColor(BaseColor.WHITE);

        name.setFont(tempFont);
        name.setIndentationLeft(30);
        name.setLeading(20);

        Paragraph surname = new Paragraph(user.getSurname());
        Font tempFont2 = new Font(urName, 24);
        tempFont2.setColor(BaseColor.WHITE);
        tempFont2.setStyle(Font.BOLD);

        surname.setIndentationLeft(30);
        surname.setFont(tempFont2);
        surname.setLeading(24);
        surname.setSpacingAfter(25);

        cell.addElement(name);
        cell.addElement(surname);
    }
}
