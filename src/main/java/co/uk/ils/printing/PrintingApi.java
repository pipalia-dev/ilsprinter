package co.uk.ils.printing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class PrintingApi {
    @RequestMapping(path = "/test-print", method = GET)
    public ResponseEntity<String> testPrint()  {
        List<String> bigFontText = new ArrayList<>();
        bigFontText.add("JOSIE CAMPBELL\n");
        bigFontText.add("FOXHOLLOW\n");
        bigFontText.add("STOKE HILL, CHEW STOKE\n");
        bigFontText.add("BRISTOL\n");
        bigFontText.add("AVON\n");
        bigFontText.add("BS40 8XG\n");
        bigFontText.add("\n");

        List<String> smallFontText = new ArrayList<>();
        smallFontText.add("Return Address: \n");
        smallFontText.add("I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL");

        try {
            print(bigFontText, smallFontText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Printed");
    }

    @RequestMapping(path = "/test-print1", method = GET)
    public ResponseEntity<String> testPrint1()  {
        List<String> bigFontText = new ArrayList<>();
        bigFontText.add("JOSIE CAMPBELL\n");
        bigFontText.add("FOXHOLLOW\n");
        bigFontText.add("STOKE HILL, CHEW STOKE\n");
        bigFontText.add("BRISTOL\n");
        bigFontText.add("AVON\n");
        bigFontText.add("BS40 8XG\n");
        bigFontText.add("\n");

        List<String> smallFontText = new ArrayList<>();
        smallFontText.add("Return Address: \n");
        smallFontText.add("I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL\n");

        try {
            print(smallFontText, bigFontText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Printed");
    }

    @RequestMapping(path = "/test-print2", method = GET)
    public ResponseEntity<String> testPrint2()  {
        List<String> bigFontText = new ArrayList<>();
        bigFontText.add("");

        List<String> smallFontText = new ArrayList<>();
        smallFontText.add("Return Address: \n");
        smallFontText.add("I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL");

        try {
            print(bigFontText, smallFontText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Printed");
    }

    @RequestMapping(path = "/test-print3", method = GET)
    public ResponseEntity<String> testPrint3()  {
        List<String> bigFontText = new ArrayList<>();
        bigFontText.add("");

        List<String> smallFontText = new ArrayList<>();
        smallFontText.add("Return Address: \n");
        smallFontText.add("some value\n");
        smallFontText.add("some other value\n");

        try {
            print(bigFontText, smallFontText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Printed");
    }

    @RequestMapping(path = "/test-print4", method = GET)
    public ResponseEntity<String> testPrint4()  {
        List<String> bigFontText = new ArrayList<>();
        bigFontText.add("JOSIE CAMPBELL\n");
        bigFontText.add("\n");

        List<String> smallFontText = new ArrayList<>();
        smallFontText.add("Return Address: \n");
        smallFontText.add("I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL");
        smallFontText.add("New line at the end. \n");

        try {
            print(bigFontText, smallFontText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Printed");
    }

    @CrossOrigin
    @RequestMapping(path = "/print", method = POST)
    public ResponseEntity<String> print(@RequestBody List<PrintingData> dtos) {
        try {
            for (PrintingData dto : dtos) {
                print(dto.bigFontLines, dto.smallFontLines);
            }

            return ResponseEntity.ok("Printed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public void print(List<String> bigFontText, List<String> smallFontText) throws Exception {
        JTextPane textPane = new JTextPane();

        Document document = textPane.getStyledDocument();
        document.insertString(document.getLength(), toString(bigFontText), getFont(12));
        document.insertString(document.getLength(), toString(smallFontText), getFont(8));

        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(OrientationRequested.LANDSCAPE);
        attributes.add(new MediaPrintableArea(0, 0, 50, 97, Size2DSyntax.MM));
        textPane.setMargin(new Insets(20, 20, 20, 20));

        textPane.print(null, null, false, PrintServiceLookup.lookupDefaultPrintService(), attributes, false);
    }

    private String toString(List<String> lines) {
        StringBuilder result = new StringBuilder();
        for(String line : lines) {
            result.append(line);
        }
        return result.toString();
    }

    public static SimpleAttributeSet getFont(int size)
    {
        SimpleAttributeSet result = new SimpleAttributeSet();
        StyleConstants.setFontFamily(result, "SansSerif");
        StyleConstants.setFontSize(result, size);

        return result;
    }


}
