package co.uk.ils.printing;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.print.PrintException;
import java.io.UnsupportedEncodingException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PrintingApi {
    @RequestMapping(path = "/print", method = GET)
    public ResponseEntity<String> print() throws PrintException {
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
        printerService.printString(label);
        return ResponseEntity.ok("ok");
    }

    @RequestMapping(path = "/health", method = GET)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}
