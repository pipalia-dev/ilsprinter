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

        TSPLConnectionClient tsplConnectionClient = new EthernetConnectionClient("localhost", 5130);
        tsplConnectionClient.init();
        tsplConnectionClient.connect();
        TSPLLabel tsplLabel = TSPLLabel.builder()
                .element(Size.builder().labelWidth(4f).labelLength(3f).build())
                .element(Gap.builder().labelDistance(0f).labelOffsetDistance(0f).build())
                .element(Direction.builder().printPositionAsFeed(Boolean.TRUE).build())
                .element(ClearBuffer.builder().build())
                .element(DataMatrix.builder().xCoordinate(10).yCoordinate(110).width(400)
                        .height(400).content("DMATRIX EXAMPLE 1").build())
                .element(DataMatrix.builder().xCoordinate(310).yCoordinate(110).width(400)
                        .height(400).moduleSize(6).content("DMATRIX EXAMPLE 2").build())
                .element(DataMatrix.builder().xCoordinate(10).yCoordinate(310).width(400)
                        .height(400).moduleSize(8).nbRows(18).nbCols(18)
                        .content("DMATRIX EXAMPLE 3").build())
                .element(Print.builder().nbLabels(1).nbCopies(1).build())
                .build();

        tsplConnectionClient.send(tsplLabel);

        Thread.sleep(2000);
        tsplConnectionClient.disconnect();
        tsplConnectionClient.shutdown();

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
