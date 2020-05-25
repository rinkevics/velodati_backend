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
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.ArrayList;
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
        xlsWriter.write(getColumns(), prepareData(), FilesStore.IMG_FOLDER + File.separator + "veloslazdi-export.xls");
        return "redirect:" + propertyProvider.get(SERVER_URL) + "app/admin/new";
    }

    private List<Column> getColumns() {
        return Arrays.asList(
                new Column("ID", 6 * 256),
                new Column("Apraksts", 20 * 256),
                new Column("Vietas tips", 30 * 256),
                new Column("Balsu skaits", 10 * 256),
                new Column("Atrašanās vieta", 36 * 256),
                new Column("RDSD atbildes statuss", 36 * 256),
                new Column("RDSD atbilde", 36 * 256),
                new Column("Attēls", 255 * 256)
        );
    }

    private List<CellData[]> prepareData() {
        List<CellData[]> data = new ArrayList<>();
        for(var place : placeService.getPlaceData()) {
            data.add(new CellData[] {
                    new TextCellData(String.valueOf(place.id)),
                    new TextCellData(place.description),
                    new TextCellData(place.placeType != null ? place.placeType.label : ""),
                    new TextCellData(String.valueOf(place.voteCount)),
                    new TextCellData(place.lat + ", " + place.lon),
                    new TextCellData(place.townHallReplyState != null ? place.townHallReplyState.label : ""),
                    new TextCellData(place.replyFromTownHall),
                    getImageCell(place)
            });
        }
        return data;
    }

    private CellData getImageCell(PlaceDataExportDTO place) {
        CellData imageCellData;
        if(place.img != null && !place.img.isEmpty()) {
            imageCellData = new ImageCellData(FilesStore.IMG_FOLDER + File.separator + place.img, imageTransformer.getFileExtension(place.img));
        } else {
            imageCellData = new TextCellData("");
        }
        return imageCellData;
    }
}
