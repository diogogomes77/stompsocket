package dev.dgomes.backend.fileupload;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    /*
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }*/

    @PostMapping("/")
    public String handleFileUpload(final HttpServletRequest request, RedirectAttributes redirectAttributes) {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            // consider raising an error here if desired
        }

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload();

        FileItemIterator iter;
        InputStream fileStream = null;
        String name = null;
        try {
            // retrieve the multi-part constituent items parsed from the request
            iter = upload.getItemIterator(request);

            // loop through each item
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                name = item.getName();
                fileStream = item.openStream();

                // check if the item is a file
                if (!item.isFormField()) {
                    System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
                    break; // break here so that the input stream can be processed
                }
            }
        } catch (FileUploadException | IOException e) {
            // log / handle the error here as necessary
            e.printStackTrace();
        }

        if (fileStream != null) {
            // a file has been sent in the http request
            // pass the fileStream to a method on the storageService so it can be persisted
            // note the storageService will need to be modified to receive and process the fileStream
            storageService.store_stream(fileStream, name);
        }

        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + name + "!");

        return "redirect:/";
    }

    private InputStream getInputStream(final HttpServletRequest request) throws IOException, FileUploadException {
        final ServletFileUpload upload = new ServletFileUpload();
        final FileItemIterator iterator = upload.getItemIterator(request);

        InputStream is = null;

        while (iterator.hasNext()) {
            final FileItemStream item = iterator.next();

            if (!item.isFormField()) {
                is = item.openStream();

                break;
            }
        }

        return is;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}