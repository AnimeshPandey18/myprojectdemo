package com.example.documentmanagement.controller;

import com.example.documentmanagement.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    private static final Logger LOGGER = Logger.getLogger(FolderController.class.getName());
    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 5000;

    @PostMapping("/create")
    public ResponseEntity<String> createFolder(@RequestParam String folderName, @RequestParam String parentFolderId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/folders"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"folderName\":\"" + folderName + "\", \"parentFolderId\":\"" + parentFolderId + "\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.CREATED.value()) {
                return new ResponseEntity<>("Folder created successfully", HttpStatus.CREATED);
            } else {
                logError("createFolder", "Failed to create folder: " + response.body());
                return new ResponseEntity<>("Failed to create folder", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logError("createFolder", e.getMessage());
            return new ResponseEntity<>("An error occurred while creating the folder", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{folderId}/contents")
    public ResponseEntity<String> getFolderContents(@PathVariable String folderId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/folders/" + folderId + "/contents"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                return new ResponseEntity<>(response.body(), HttpStatus.OK);
            } else {
                logError("getFolderContents", "Failed to retrieve folder contents: " + response.body());
                return new ResponseEntity<>("Failed to retrieve folder contents", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logError("getFolderContents", e.getMessage());
            return new ResponseEntity<>("An error occurred while retrieving folder contents", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{folderId}/update")
    public ResponseEntity<String> updateFolder(@PathVariable String folderId, @RequestParam String newFolderName) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/folders/" + folderId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString("{\"newFolderName\":\"" + newFolderName + "\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                return new ResponseEntity<>("Folder updated successfully", HttpStatus.OK);
            } else {
                logError("updateFolder", "Failed to update folder: " + response.body());
                return new ResponseEntity<>("Failed to update folder", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logError("updateFolder", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating the folder", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{folderId}/delete")
    public ResponseEntity<String> deleteFolder(@PathVariable String folderId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/folders/" + folderId))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.NO_CONTENT.value()) {
                return new ResponseEntity<>("Folder deleted successfully", HttpStatus.NO_CONTENT);
            } else {
                logError("deleteFolder", "Failed to delete folder: " + response.body());
                return new ResponseEntity<>("Failed to delete folder", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logError("deleteFolder", e.getMessage());
            return new ResponseEntity<>("An error occurred while deleting the folder", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void logError(String methodName, String errorMessage) {
        LOGGER.log(Level.SEVERE, "Error in method {0}: {1}", new Object[]{methodName, errorMessage});
    }
}
