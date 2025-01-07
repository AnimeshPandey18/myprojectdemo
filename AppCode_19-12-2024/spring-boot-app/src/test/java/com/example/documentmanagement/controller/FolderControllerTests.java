package com.example.documentmanagement.controller;

import com.example.documentmanagement.service.FolderService;
import com.example.documentmanagement.model.Folder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FolderControllerTests {

    @Mock
    private FolderService folderService;

    @InjectMocks
    private FolderController folderController;

    private Folder folder1;
    private Folder folder2;

    @BeforeEach
    public void setUp() {
        folder1 = new Folder(1L, "Folder1");
        folder2 = new Folder(2L, "Folder2");
    }

    @Test
    public void testGetAllFolders() {
        List<Folder> folders = Arrays.asList(folder1, folder2);
        when(folderService.getAllFolders()).thenReturn(folders);

        ResponseEntity<List<Folder>> response = folderController.getAllFolders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(folderService, times(1)).getAllFolders();
    }

    @Test
    public void testGetFolderById() {
        when(folderService.getFolderById(1L)).thenReturn(folder1);

        ResponseEntity<Folder> response = folderController.getFolderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Folder1", response.getBody().getName());
        verify(folderService, times(1)).getFolderById(1L);
    }

    @Test
    public void testGetFolderById_NotFound() {
        when(folderService.getFolderById(3L)).thenReturn(null);

        ResponseEntity<Folder> response = folderController.getFolderById(3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(folderService, times(1)).getFolderById(3L);
    }

    @Test
    public void testCreateFolder() {
        when(folderService.createFolder(folder1)).thenReturn(folder1);

        ResponseEntity<Folder> response = folderController.createFolder(folder1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Folder1", response.getBody().getName());
        verify(folderService, times(1)).createFolder(folder1);
    }

    @Test
    public void testUpdateFolder() {
        when(folderService.updateFolder(1L, folder1)).thenReturn(folder1);

        ResponseEntity<Folder> response = folderController.updateFolder(1L, folder1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Folder1", response.getBody().getName());
        verify(folderService, times(1)).updateFolder(1L, folder1);
    }

    @Test
    public void testDeleteFolder() {
        doNothing().when(folderService).deleteFolder(1L);

        ResponseEntity<Void> response = folderController.deleteFolder(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(folderService, times(1)).deleteFolder(1L);
    }

    @Test
    public void testDeleteFolder_NotFound() {
        doThrow(new RuntimeException("Folder not found")).when(folderService).deleteFolder(3L);

        ResponseEntity<Void> response = folderController.deleteFolder(3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(folderService, times(1)).deleteFolder(3L);
    }
}
