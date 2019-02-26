package org.peter.chat.service.app.impl;

import org.peter.chat.enums.exceptionStatus.ChatExceptionStatus;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.service.app.QRCodeService;
import org.peter.chat.utils.FastDFSClient;
import org.peter.chat.utils.FileUtils;
import org.peter.chat.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    public String generateQrCodeAndUpload(String qrFilename, String qrContent) {
        String tmpdir = System.getProperty("java.io.tmpdir");
        String qrCodeFilePath = tmpdir + File.pathSeparator + qrFilename;
        // 在临时文件生成二维码图片文件
        qrCodeUtils.createQRCode(qrCodeFilePath, qrContent);
        MultipartFile qrMultipartFile = FileUtils.fileToMultipart(qrCodeFilePath);
        String serverQrcodeFilePath;
        try {
            // 上传文件到fdfs
            serverQrcodeFilePath = fastDFSClient.uploadQRCode(qrMultipartFile);
        } catch (IOException e) {
            throw new BusinessException(ChatExceptionStatus.UPLOAD_QRCODE_FAIL,
                    "MultipartFile={}", qrMultipartFile);
        }
        return serverQrcodeFilePath;
    }
}
