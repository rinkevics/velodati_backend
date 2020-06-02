package lv.datuskola.file;

import lv.datuskola.place.Place;
import lv.datuskola.place.PlaceType;
import lv.datuskola.services.Recaptcha;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.HtmlUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@Validated
public class FileController {

    @Autowired
    public FilesStore storageService;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public String handleFileUpload(
            @RequestParam("uploadimage") MultipartFile uploadImage,
            @RequestParam("type") @Min(0) @Max(3) Integer type,
            @RequestParam("lat") @Length(min = 1, max = 30) String lat,
            @RequestParam("lon") @Length(min = 1, max = 30) String lon,
            @RequestParam("description") @Length(min = 1, max = 282) String description,
            @RequestParam("email")  @Length(min = 1, max = 200) String email,
            @RequestParam(value = "subscribe", required = false) boolean subscribe,
            @CookieValue(value = "token", required = false) String token,
            @RequestHeader(value = "x-captcha") String captcha,
            HttpServletRequest request) throws Exception {

        if(!Recaptcha.isGoodCaptcha(captcha)) {
            return "blank";
        }

        lat = HtmlUtils.htmlEscape(lat);
        lon = HtmlUtils.htmlEscape(lon);
        description = HtmlUtils.htmlEscape(description);
        email = HtmlUtils.htmlEscape(email);

        if(uploadImage.isEmpty()) {
            throw new Exception("Must provide image");
        }
        String newName = storeImage(uploadImage);

        var place = new Place(PlaceType.values()[type], lat, lon, newName, description, LocalDateTime.now(), token,
                request.getHeader("X-Real-IP"), email, subscribe, false, false);
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
