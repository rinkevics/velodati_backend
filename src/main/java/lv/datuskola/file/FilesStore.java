package lv.datuskola.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesStore {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String IMG_FOLDER = "/home/kaspars/upload-dir";
    private final Path rootLocation = Paths.get(IMG_FOLDER);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskExecutor taskExecutor;

    public void store(MultipartFile file, String newName, String extension) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new IOException("Failed to store empty file " + file.getOriginalFilename());
            }
            var tmpFile = this.rootLocation.resolve("tmp" + newName + extension);
            var newFile = this.rootLocation.resolve(newName + extension);
            var newFile2 = this.rootLocation.resolve("2" + newName + extension);

            Files.copy(file.getInputStream(), tmpFile);

            ImageTransformer imageTransformer = applicationContext.getBean(ImageTransformer.class);
            imageTransformer.init(tmpFile, newFile, newFile2);
            taskExecutor.execute(imageTransformer);
        } catch (Exception e) {
            throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public Stream<Path> loadAll() throws IOException {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new IOException("Failed to read stored files", e);
        }
    }

    public Resource loadAsResource(String filename) throws IOException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                logger.error("Could not read file: " + filename);
                return null;
            }
        } catch (MalformedURLException e) {
            throw new IOException("Could not read file: " + filename, e);
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}

