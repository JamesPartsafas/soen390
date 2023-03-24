package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Handles all logic relating to file download in the chat page
 */
@RestController
public class FileDownloadController {

    private MessageService messageService;
    private AuthService authService;

    @Autowired
    public FileDownloadController(MessageService messageService, AuthService authService) {
        this.messageService = messageService;
        this.authService = authService;
    }

    /**
     * Downloads file from message
     * @param messageId The message the file is associated to
     * @return The file data to be downloaded
     */
    @GetMapping("/downloadFile/{messageId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Long messageId) {
        if (!authService.isUserAuthenticated()) {
            return null;
        }

        FileHolder fileHolder = messageService.retrieveFileHolderFromMessage(messageId, authService.getAuthenticatedUser());
        String fileData = fileHolder.getFileData();
        if (fileData == null)
            return null;

        String partSeparator = ",";

        if (fileData.contains(partSeparator)) {
            fileData = fileData.split(partSeparator)[1];
        }

        byte[] base64Data = Base64.getDecoder().decode(fileData.getBytes(StandardCharsets.UTF_8));
        Resource resource = new ByteArrayResource(base64Data);

        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileHolder.getFileName() + "\"")
                .body(resource);
    }
}
