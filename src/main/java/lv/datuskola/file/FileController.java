package lv.datuskola.file;

import lv.datuskola.place.Place;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class FileController {

    @Autowired
    public FilesStore storageService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationEventMulticaster applicationEventMulticaster;

    @PostMapping(value="/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public String handleFileUpload(
            @RequestParam("uploadimage") MultipartFile uploadImage,
            @RequestParam("type") Integer type,
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon,
            @RequestParam("description") String description) throws IOException {

        if(type < 1 || type > 3 ) {
            return "blank";
        }
        description = StringEscapeUtils.escapeEcmaScript(description);
        description = StringEscapeUtils.escapeHtml4(description);

        var newName = getRandomName();
        if(uploadImage != null && !uploadImage.isEmpty()) {
            var extension = getFileExtension(uploadImage.getOriginalFilename());
            storageService.store(uploadImage, newName, extension);
            newName = newName + extension;
        } else {
            newName = "";
        }

        var rand = new Random();
        boolean isUniqueLocation = false;
        for(int i = 0; i < 3; i++) {
            // TODO do without sql
            var count = (Long) entityManager.createQuery("SELECT count(p) FROM Place p WHERE lat = :lat AND lon = :lon")
                    .setParameter("lat", lat)
                    .setParameter("lon", lon)
                    .getSingleResult();

            if (count > 0) {
                var shift = rand.nextInt(10);
                lat = lat.substring(0, lat.length() - 2) + shift;
                lon = lon.substring(0, lon.length() - 2) + shift;
            } else {
                isUniqueLocation = true;
                break;
            }
        }

        if(isUniqueLocation) {
            var place = new Place(type, lat, lon, newName, description, LocalDateTime.now(), "");
            entityManager.persist(place);
        }

        return "blank";
    }

    @GetMapping(value="/files")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    // TODO replace with nginx
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        var file = storageService.loadAsResource(filename);
        if(file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    private static String getRandomName() {
        var leftLimit = 97; // letter 'a'
        var rightLimit = 122; // letter 'z'
        var targetStringLength = 10;
        var random = new Random();
        var buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private static String getFileExtension(final String path) {
        if (path != null && path.lastIndexOf('.') != -1) {
            return path.substring(path.lastIndexOf('.'));
        }
        return null;
    }

}
