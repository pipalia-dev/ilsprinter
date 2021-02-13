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
import java.awt.print.PrinterException;
import java.io.IOException;

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

        PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = pservice.createPrintJob();
        String commands = "^XA\n\r^MNM\n\r^FO050,50\n\r^B8N,100,Y,N\n\r^FD1234567\n\r^FS\n\r^PQ3\n\r^XZ";
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(commands.getBytes(), flavor, null);
        job.print(doc, null);

        return ResponseEntity.ok("ok");
    }
    @RequestMapping(path = "/health", method = GET)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    private static boolean feedPrinter(byte[] b) {
        try {
            DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);

            job.print(doc, null);
            Thread.sleep(1000);
            System.out.println("Done !");
        } catch (javax.print.PrintException pex) {
            System.out.println("Printer Error " + pex.getMessage());
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
