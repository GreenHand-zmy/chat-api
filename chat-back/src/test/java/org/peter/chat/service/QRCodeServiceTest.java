package org.peter.chat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.peter.chat.service.app.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QRCodeServiceTest {
    @Autowired
    private QRCodeService qrCodeService;

    @Test
    public void generateQrCodeAndUpload() {
        String url = qrCodeService.generateQrCodeAndUpload("xxx", "123");
        System.out.println(url);
    }
}