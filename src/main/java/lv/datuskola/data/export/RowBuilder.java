package lv.datuskola.data.export;

import lv.datuskola.file.FilesStore;
import lv.datuskola.file.ImageTransformer;
import lv.datuskola.place.PlaceDataExportDTO;

import java.io.File;

record RowBuilder(ImageTransformer imageTransformer, PlaceDataExportDTO place) {

    CellBuilder[] buildRowCells() {
        return new CellBuilder[]{
                new TextCellBuilder(String.valueOf(place.id)),
                new TextCellBuilder(place.description),
                new TextCellBuilder(place.placeType != null ? place.placeType.label : ""),
                new TextCellBuilder(String.valueOf(place.voteCount)),
                new TextCellBuilder(place.lat + ", " + place.lon),
                new TextCellBuilder(place.townHallReplyState != null ? place.townHallReplyState.label : ""),
                new TextCellBuilder(place.replyFromTownHall),
                new TextCellBuilder(place.img),
                buildImageCell(place)};
    }

    private CellBuilder buildImageCell(PlaceDataExportDTO place) {
        CellBuilder imageCellBuilder;
        if (place.img != null && !place.img.isEmpty()) {
            imageCellBuilder = new ImageCellBuilder(FilesStore.IMG_FOLDER + File.separator + place.img,
                    imageTransformer.getFileExtension(place.img));
        } else {
            imageCellBuilder = new TextCellBuilder("");
        }
        return imageCellBuilder;
    }

}
