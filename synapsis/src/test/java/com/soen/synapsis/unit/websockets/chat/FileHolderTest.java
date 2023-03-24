package com.soen.synapsis.unit.websockets.chat;

import com.soen.synapsis.websockets.chat.FileHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileHolderTest {

    private String fileName;
    private String fileData;
    private FileHolder underTest;
    @BeforeEach
    void setUp() {
        fileName = "name";
        fileData = "data";
        underTest = new FileHolder(fileName, fileData);
    }

    @Test
    void getFileName() {
        assertEquals(fileName, underTest.getFileName());
    }

    @Test
    void setFileName() {
        String newName = "newName";
        underTest.setFileName(newName);
        assertEquals(newName, underTest.getFileName());
    }

    @Test
    void getFileData() {
        assertEquals(fileData, underTest.getFileData());
    }

    @Test
    void setFileData() {
        String newData = "newData";
        underTest.setFileData(newData);
        assertEquals(newData, underTest.getFileData());
    }
}