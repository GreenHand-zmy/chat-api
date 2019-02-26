package org.peter.chat.service.app;

public interface QRCodeService {
    /**
     * 生成二维码图,上传到服务器并返回服务器二维码地址
     *
     * @param qrFilename
     * @param qrContent
     * @return
     */
    String generateQrCodeAndUpload(String qrFilename, String qrContent);
}
