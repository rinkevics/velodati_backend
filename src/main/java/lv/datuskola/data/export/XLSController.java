package lv.datuskola.data.export;

import lv.datuskola.auth.PropertyProvider;
import lv.datuskola.file.FilesStore;
import lv.datuskola.file.ImageTransformer;
import lv.datuskola.place.PlaceDataExportDTO;
import lv.datuskola.place.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static lv.datuskola.MainApp.SERVER_URL;

@Controller
public class XLSController {

    @Autowired
    private PropertyProvider propertyProvider;

    @Autowired
    public XLSWriter xlsWriter;

    @Autowired
    public PlaceService placeService;

    @Autowired
    public ImageTransformer imageTransformer;

    @GetMapping("/xls")
    public String admin(
            @CookieValue("admintoken") String adminToken,
            Model model) {
        if(adminToken == null || !adminToken.equals(propertyProvider.get("admin"))) {
            return "adminlogin";
        }

        List<PlaceDataExportDTO> placeData = placeService.getPlaceData();

        xlsWriter.buildXLS(getColumns(),
                placeData.iterator(), FilesStore.IMG_FOLDER + File.separator + "veloslazdi-export.xls");
        return "redirect:" + propertyProvider.get(SERVER_URL) + "app/admin/new";
    }

    public List<Column> getColumns() {
        return Arrays.asList(new Column("ID", 6 * 256),
                new Column("Apraksts", 20 * 256),
                new Column("Vietas tips", 30 * 256),
                new Column("Balsu skaits", 10 * 256),
                new Column("Atrašanās vieta", 36 * 256),
                new Column("RDSD atbildes statuss", 36 * 256),
                new Column("RDSD atbilde", 36 * 256),
                new Column("Attēls", 255 * 256));
    }


}
