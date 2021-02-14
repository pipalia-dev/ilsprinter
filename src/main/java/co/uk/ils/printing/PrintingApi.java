package co.uk.ils.printing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTextPane;
import javax.swing.text.*;
import java.awt.Insets;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PrintingApi {
    @RequestMapping(path = "/print", method = GET)
    public ResponseEntity<String> print()  {
        List<String> bigFontText = new ArrayList<>();
        bigFontText.add("JOSIE CAMPBELL\n");
        bigFontText.add("FOXHOLLOW\n");
        bigFontText.add("STOKE HILL, CHEW STOKE\n");
        bigFontText.add("BRISTOL\n");
        bigFontText.add("AVON\n");
        bigFontText.add("BS40 8XG\n");
        bigFontText.add("\n");

        List<String> smallText = new ArrayList<>();
        smallText.add("Return Address: \n");
        smallText.add("I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL");

        try {
            JTextPane textPane = new JTextPane();

            Document document = textPane.getStyledDocument();
            document.insertString(document.getLength(), toString(bigFontText), getFont(12));
            document.insertString(document.getLength(), toString(smallText), getFont(8));

            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(OrientationRequested.LANDSCAPE);
            attributes.add(new MediaPrintableArea(0,0,50, 97, Size2DSyntax.MM));
            textPane.setMargin(new Insets(20, 20, 20, 20));

            textPane.print(null, null, true, PrintServiceLookup.lookupDefaultPrintService(), attributes, true);
            return ResponseEntity.ok("Printed");
        } catch (PrinterException | BadLocationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
