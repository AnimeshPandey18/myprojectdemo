package com.example.documentmanagement.service;

import com.example.documentmanagement.model.Folder;
import com.example.documentmanagement.exception.FolderNotFoundException;
import com.example.documentmanagement.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FolderServiceTests {

    @Mock
    private FolderRepository folderRepository;

    @InjectMocks
    private FolderService folderService;

    private Folder folder;

    @BeforeEach
    public void setUp() {
        folder = new Folder();
        folder.setId(1L);
        folder.setName("Test Folder");
    }

    @Test
    public void testGetFolderById_Success() {
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));

        Folder result = folderService.getFolderById(1L);

        assertNotNull(result);
        assertEquals("Test Folder", result.getName());
        verify(folderRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetFolderById_NotFound() {
        when(folderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(FolderNotFoundException.class, () -> {
            folderService.getFolderById(1L);
        });

        String expectedMessage = "Folder not found with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(folderRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateFolder_Success() {
        when(folderRepository.save(any(Folder.class))).thenReturn(folder);

        Folder result = folderService.createFolder(folder);

        assertNotNull(result);
        assertEquals("Test Folder", result.getName());
        verify(folderRepository, times(1)).save(folder);
    }

    @Test
    public void testCreateFolder_ServiceException() {
        when(folderRepository.save(any(Folder.class))).thenThrow(new ServiceException("Service error"));

        Exception exception = assertThrows(ServiceException.class, () -> {
            folderService.createFolder(folder);
        });

        String expectedMessage = "Service error";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(folderRepository, times(1)).save(folder);
    }

    @Test
    public void testDeleteFolder_Success() {
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        doNothing().when(folderRepository).deleteById(1L);

        folderService.deleteFolder(1L);

        verify(folderRepository, times(1)).findById(1L);
        verify(folderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteFolder_NotFound() {
        when(folderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(FolderNotFoundException.class, () -> {
            folderService.deleteFolder(1L);
        });

        String expectedMessage = "Folder not found with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(folderRepository, times(1)).findById(1L);
    }
}
