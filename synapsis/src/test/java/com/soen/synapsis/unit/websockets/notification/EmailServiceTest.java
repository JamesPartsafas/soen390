package com.soen.synapsis.unit.websockets.notification;

import com.soen.synapsis.websockets.notification.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender;

    private EmailService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EmailService(emailSender);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void sendSimpleMessageSuccess() {
        String to = "test@mail.com";
        String subject = "Test subject";
        String text = "Test text";
        underTest.sendSimpleMessage(to, subject, text);
    }
}
