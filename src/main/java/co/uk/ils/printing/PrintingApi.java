package co.uk.ils.printing;

import net.sourceforge.barbecue.BarcodeException;
import org.fintrace.core.drivers.tspl.commands.label.DataMatrix;
import org.fintrace.core.drivers.tspl.commands.label.TSPLLabel;
import org.fintrace.core.drivers.tspl.commands.system.*;
import org.fintrace.core.drivers.tspl.connection.EthernetConnectionClient;
import org.fintrace.core.drivers.tspl.connection.TSPLConnectionClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import java.awt.*;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PrintingApi {
    @RequestMapping(path = "/print", method = GET)
    public ResponseEntity<String> print() throws PrintException, PrinterException, BarcodeException, IOException, InterruptedException {
        String label = "JOSIE CAMPBELL\n"
                + "FOXHOLLOW\n"
                + "STOKE HILL, CHEW STOKE\n"
                + "BRISTOL\n"
                + "AVON\n"
                + "BS40 8XG\n"
                + "\n"
                + "Return Address: \n"
                + "I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL";

        return ResponseEntity.ok("ok");
    }

    public static void print(final String text) {
        final PrinterJob job = PrinterJob.getPrinterJob();

        Printable contentToPrint = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("Monospaced", Font.BOLD, 7));
                pageFormat.setOrientation(PageFormat.PORTRAIT);

                Paper pPaper = pageFormat.getPaper();
                pPaper.setImageableArea(0, 0, pPaper.getWidth(), pPaper.getHeight() - 2);
                pageFormat.setPaper(pPaper);

                if (pageIndex > 0)
                    return NO_SUCH_PAGE; //Only one page

                g2d.drawString(text, 0, 0);

                return PAGE_EXISTS;
            }
        };

        boolean don = job.printDialog();

        job.setPrintable(contentToPrint);

        try {
            job.print();
        } catch (PrinterException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String label = "JOSIE CAMPBELL\n"
                + "FOXHOLLOW\n"
                + "STOKE HILL, CHEW STOKE\n"
                + "BRISTOL\n"
                + "AVON\n"
                + "BS40 8XG\n"
                + "\n"
                + "Return Address: \n"
                + "I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL";

        print(label);
    }
    @RequestMapping(path = "/health", method = GET)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }


}
