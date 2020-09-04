package lv.datuskola.data.export;

import lv.datuskola.file.FilesStore;
import lv.datuskola.file.ImageTransformer;
import lv.datuskola.place.PlaceDataExportDTO;
import lv.datuskola.place.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class XLSMain {

    @Autowired
    public XLSWriter xlsWriter;

    @Autowired
    public PlaceService placeService;

    @Autowired
    public ImageTransformer imageTransformer;

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        //xlsWriter.write(getColumns(), prepareData(), FilesStore.IMG_FOLDER + File.separator + "veloslazdi-export.xls");
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

    private List<CellBuilder[]> prepareData() {
        List<CellBuilder[]> data = new ArrayList<>();
        for(var place : placeService.getPlaceData()) {
            data.add(new CellBuilder[] {
                    new TextCellBuilder(String.valueOf(place.id)),
                    new TextCellBuilder(place.description),
                    new TextCellBuilder(place.placeType != null ? place.placeType.label : ""),
                    new TextCellBuilder(String.valueOf(place.voteCount)),
                    new TextCellBuilder(place.lat + ", " + place.lon),
                    new TextCellBuilder(place.townHallReplyState != null ? place.townHallReplyState.label : ""),
                    new TextCellBuilder(place.replyFromTownHall),
                    getImageCell(place)
            });
        }
        return data;
    }

    private CellBuilder getImageCell(PlaceDataExportDTO place) {
        CellBuilder imageCellBuilder;
        if(place.img != null && !place.img.isEmpty()) {
            imageCellBuilder = new ImageCellBuilder(FilesStore.IMG_FOLDER + File.separator + place.img, imageTransformer.getFileExtension(place.img));
        } else {
            imageCellBuilder = new TextCellBuilder("");
        }
        return imageCellBuilder;
    }
}
