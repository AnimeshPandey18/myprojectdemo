package com.example.documentmanagement.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderService {

    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 5000; // Timeout in milliseconds
    private static final Logger LOGGER = Logger.getLogger(FolderService.class.getName());
    private final HttpClient httpClient;

    public FolderService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public void createFolder(String folderName, String parentFolderId) {
        try {
            URI uri = new URI(UDMS_HOST + "/folders");
            String requestBody = String.format("{\"name\": \"%s\", \"parentId\": \"%s\"}", folderName, parentFolderId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                logError("createFolder", "Failed to create folder: " + response.body());
            }
        } catch (Exception e) {
            logError("createFolder", e.getMessage());
        }
    }

    public String getFolderContents(String folderId) {
        try {
            URI uri = new URI(UDMS_HOST + "/folders/" + folderId + "/contents");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                logError("getFolderContents", "Failed to retrieve folder contents: " + response.body());
            }
        } catch (Exception e) {
            logError("getFolderContents", e.getMessage());
        }
        return null;
    }

    public void updateFolder(String folderId, String newFolderName) {
        try {
            URI uri = new URI(UDMS_HOST + "/folders/" + folderId);
            String requestBody = String.format("{\"name\": \"%s\"}", newFolderName);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logError("updateFolder", "Failed to update folder: " + response.body());
            }
        } catch (Exception e) {
            logError("updateFolder", e.getMessage());
        }
    }

    public void deleteFolder(String folderId) {
        try {
            URI uri = new URI(UDMS_HOST + "/folders/" + folderId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 204) {
                logError("deleteFolder", "Failed to delete folder: " + response.body());
            }
        } catch (Exception e) {
            logError("deleteFolder", e.getMessage());
        }
    }

    private void logError(String methodName, String errorMessage) {
        LOGGER.log(Level.SEVERE, "Error in method {0}: {1}", new Object[]{methodName, errorMessage});
    }
}
