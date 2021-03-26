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
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class PrintingApi {
    private static final Integer LINE_SIZE_THRESHOLD = 12;

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

        boolean exceedsLineLimit = false;
        for (String line : bigFontText) {
            if (line.length() > LINE_SIZE_THRESHOLD) {
                exceedsLineLimit = true;
                break;
            }
        }

        document.insertString(document.getLength(), toString(bigFontText), getFont(exceedsLineLimit ? 8 : 11));
        document.insertString(document.getLength(), toString(smallFontText), getFont(7));

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
