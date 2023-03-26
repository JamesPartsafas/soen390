package com.soen.synapsis.websockets.chat;

/**
 * Holds information related to uploaded file data and names
 */
public class FileHolder {
    private String fileName;
    private String fileData;

    public FileHolder(String fileName, String fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
