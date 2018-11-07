package org.peter.chat.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public final class FileUtils {
    /**
     * 根据url拿取file
     *
     * @param url
     * @param suffix 文件后缀名
     */
    public static File createFileByUrl(String url, String suffix) {
        byte[] byteFile = getImageFromNetByUrl(url);
        if (byteFile != null) {
            File file = getFileFromBytes(byteFile, suffix);
            return file;
        } else {
            return null;
        }
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    private static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    // 创建临时文件
    private static File getFileFromBytes(byte[] b, String suffix) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = File.createTempFile("pattern", "." + suffix);
            System.out.println("临时文件位置：" + file.getCanonicalPath());
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static MultipartFile createImg(String url) {
        try {
            // File转换成MutipartFile
            File file = FileUtils.createFileByUrl(url, "jpg");
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            return multipartFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MultipartFile fileToMultipart(String filePath) {
        try {
            // File转换成MutipartFile
            File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), "png", "image/png", inputStream);
            return multipartFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static boolean base64ToFile(String filePath, String base64Data) throws Exception {
        String dataPrix = "";
        String data = "";

        if (base64Data == null || "".equals(base64Data)) {
            return false;
        } else {
            String[] d = base64Data.split("base64,");
            if (d != null && d.length == 2) {
                dataPrix = d[0];
                data = d[1];
            } else {
                return false;
            }
        }

        // 因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
        byte[] bs = Base64Utils.decodeFromString(data);
        // 使用apache提供的工具类操作流
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(filePath), bs);

        return true;
    }

    public static void main(String[] args) throws Exception {
        String folder = System.getProperty("java.io.tmpdir");
        System.out.println(folder);
        base64ToFile(folder,"data:image/jpeg;base64,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");
    }
}
