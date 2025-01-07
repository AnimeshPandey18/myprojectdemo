package com.example.documentmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Folder entity in the document management system.
 * This class defines the properties and methods necessary to manage folder entities,
 * including attributes like folder ID, name, and parent folder reference for hierarchical data display.
 */
public class Folder {

    private Long folderId;
    private Long parentFolderId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public Folder(Long folderId, Long parentFolderId, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.folderId = folderId;
        this.parentFolderId = parentFolderId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getFolderId() {
        return folderId;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Override equals and hashCode for proper comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(folderId, folder.folderId) &&
                Objects.equals(parentFolderId, folder.parentFolderId) &&
                Objects.equals(name, folder.name) &&
                Objects.equals(createdAt, folder.createdAt) &&
                Objects.equals(updatedAt, folder.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folderId, parentFolderId, name, createdAt, updatedAt);
    }

    // Override toString for better logging and debugging
    @Override
    public String toString() {
        return "Folder{" +
                "folderId=" + folderId +
                ", parentFolderId=" + parentFolderId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
