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
import java.awt.print.PrinterException;
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

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

        StringBuilder builder = new StringBuilder();
        builder.append(label);
        builder.append(EscapeCodeUtil.createEscapeCode(10));

        PrintRequestAttributeSet aset= new HashPrintRequestAttributeSet();
        aset.add(new MediaPrintableArea(100,400,210,160, Size2DSyntax.MM));


        InputStream is = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));

        Doc mydoc = new SimpleDoc(is, flavor, null);

        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

        DocPrintJob job = defaultService.createPrintJob();
        job.print(mydoc, aset);

        return ResponseEntity.ok("ok");
    }
    @RequestMapping(path = "/health", method = GET)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    static class EscapeCodeUtil {
        public static String createEscapeCode(int ... codes)
        {
            StringBuilder sb = new StringBuilder();

            for(int code : codes)
                sb.append((char) code);

            return sb.toString();
        }
    }
}
