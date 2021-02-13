package co.uk.ils.printing;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PrintingApi {
    @RequestMapping(path =  "/print", method = GET)
    public ResponseEntity<String> print() {
        String label = "JOSIE CAMPBELL\n"
                + "FOXHOLLOW\n"
                + "STOKE HILL, CHEW STOKE\n"
                + "BRISTOL\n"
                + "AVON\n"
                + "BS40 8XG\n"
                + "\n"
                + "Return Address: \n"
                + "I.L.S Schools, Unit 2 Sovereign Park, Laporte Way, Luton, Beds, LU4 8EL";

        PrinterService printerService = new PrinterService();

        System.out.println(printerService.getPrinters());

        //print some stuff. Change the printer name to your thermal printer name.
        printerService.printString("TECLABEL TT027-70", label);

        // cut that paper!
        byte[] cutP = new byte[]{0x1d, 'V', 1};

        printerService.printBytes("TECLABEL TT027-70", cutP);

        return ResponseEntity.ok("ok");
    }

    @RequestMapping(path =  "/health", method = GET)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}
