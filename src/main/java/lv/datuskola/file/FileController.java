package lv.datuskola.file;

import lv.datuskola.place.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@Validated
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FilesStore storageService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationEventMulticaster applicationEventMulticaster;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public String handleFileUpload(
            @RequestParam("uploadimage") MultipartFile uploadImage,
            @RequestParam("type") @Min(1) @Max(2) Integer type,
            @RequestParam("lat") @NotBlank String lat,
            @RequestParam("lon") @NotBlank String lon,
            @RequestParam("description") @NotBlank String description,
            @RequestParam("email") @NotBlank String email,
            @RequestParam(value = "subscribe", required = false) boolean subscribe,
            @CookieValue(value = "token", required = false) String token,
            HttpServletRequest request) throws IOException {

        String newName = storeImage(uploadImage);

        var place = new Place(type, lat, lon, newName, description, LocalDateTime.now(), token,
                request.getRemoteAddr(), email, subscribe, false, false);
        entityManager.persist(place);

        return "blank";
    }

    private String storeImage(MultipartFile uploadImage) throws IOException {
        var newName = getRandomName();
        if (uploadImage != null && !uploadImage.isEmpty()) {
            var extension = getFileExtension(uploadImage.getOriginalFilename());
            storageService.store(uploadImage, newName, extension);
            newName = newName + extension;
        } else {
            newName = "";
        }
        return newName;
    }

    @GetMapping(value = "/files")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(path -> MvcUriComponentsBuilder
                .fromMethodName(FileController.class, "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    // TODO replace with nginx
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        var file = storageService.loadAsResource(filename);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    private static String getRandomName() {
        var leftLimit = 97; // letter 'a'
        var rightLimit = 122; // letter 'z'
        var targetStringLength = 10;
        var random = new Random();
        var buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
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
