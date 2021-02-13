package co.uk.ils.printing;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PrintingApi {
    @RequestMapping(path = "/print", method = GET)
    public ResponseEntity<String> print() throws PrinterException, BarcodeException {
        String label = "JOSIE CAMPBELL\n"
                + "FOXHOLLOW\n"
                + "STOKE HILL, CHEW STOKE\n"
                + "BRISTOL\n"
                + "AVON\n"
                + "BS40 8XG\n"
                + "\n"
                + "Return Address: \n"
                + "I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL";

        Barcode barcode = BarcodeFactory.create2of7(label);
        barcode.setBarHeight(60);
        barcode.setBarWidth(2);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(barcode);
        job.print();
        return ResponseEntity.ok("ok");
    }

    public static void print(final String text) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        if (printerJob.printDialog()) {
            PageFormat pageFormat = printerJob.defaultPage();
            Paper paper = pageFormat.getPaper();

            double width = fromCMToPPI(1.9);
            double height = fromCMToPPI(4.5);

            double horizontalMargin = fromCMToPPI(0.25);
            double verticalMargin = fromCMToPPI(0.1);

            paper.setSize(width, height);

            paper.setImageableArea(
                    horizontalMargin,
                    verticalMargin,
                    width,
                    height);

            pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
            pageFormat.setPaper(paper);

            printerJob.setPrintable(new MyPrintable(), pageFormat);
            try {
                printerJob.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static double fromCMToPPI(double cm) {
        return toPPI(cm * 0.393700787);
    }

    private static double toPPI(double inch) {
        return inch * 72d;
    }

    public static class MyPrintable implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                         int pageIndex) {
            System.out.println(pageIndex);
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;

                double width = pageFormat.getImageableWidth();
                double height = pageFormat.getImageableHeight();
                double x = pageFormat.getImageableX();
                double y = pageFormat.getImageableY();

                System.out.println("x = " + x);
                System.out.println("y = " + y);

                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
                g2d.draw(new Rectangle2D.Double(x, y, width - x, height - y));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString("AxB", Math.round(x), fm.getAscent());
                result = PAGE_EXISTS;
            }
            return result;
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
