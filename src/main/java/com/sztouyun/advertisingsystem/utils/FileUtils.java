package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.Buffer;

/**
 * Created by wenfeng on 2017/8/1.
 */
public class FileUtils {

    private static Double ONE_MB_KB = 1024D;

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static boolean clearFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("删除文件" + fileName + "成功！");
                return true;
            } else {
                logger.info("删除文件" + fileName + "失败！");
                return false;
            }
        } else {
            logger.info("删除文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static String ReadFile(File file) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    public static String getAssertsBasePath(String pkgType) {
        String basePath = "";
        if (pkgType.equals(Constant.WAR)) {
            basePath = FileUtils.class.getClassLoader().getResource("").getPath();
            if (isWindows()) {
                basePath = basePath.substring(1, basePath.length());
            }
        } else {
            basePath = new FileUtils().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            if (isWindows()) {
                basePath = basePath.substring(6);
            } else {
                basePath = basePath.substring(5);
            }
        }
        return pathDecode(basePath);

    }

    public static String getTmpPath(String pkgType) {
        String baseTempPath = "";
        if (pkgType.equals(Constant.JAR)) {
            baseTempPath = getJarPath();
        } else {
            baseTempPath = FileUtils.class.getClassLoader().getResource("").getPath();
            if (isWindows()) {
                baseTempPath = baseTempPath.substring(1, baseTempPath.length());
            }
            baseTempPath = pathDecode(baseTempPath);
        }
        return baseTempPath;
    }


    /**
     * 获取JAR包当前所在目录
     *
     * @return
     */
    public static String getJarPath() {
        String filePath = new FileUtils().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (isWindows()) {
            filePath = filePath.substring(6, filePath.lastIndexOf(Constant.JAR) + 3);
        } else {
            filePath = filePath.substring(5, filePath.lastIndexOf(Constant.JAR) + 3);
        }
        return pathDecode(new File(filePath).getParent() + File.separator);

    }

    public static File getFileRelativeToPath(String shortPath) {
        String filePath = new FileUtils().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (isWindows()) {
            filePath = filePath.substring(6, filePath.lastIndexOf(Constant.JAR) + 3);
        } else {
            filePath = filePath.substring(5, filePath.lastIndexOf(Constant.JAR) + 3);
        }
        File file=new File(filePath+shortPath);
        if(!file.exists())
            throw new BusinessException("获取文件路径失败");
        return file;

    }

    /**
     * 递归创建文件夹
     *
     * @param file
     */
    public static void mkDir(File file) {
        if (!file.exists()) {
            if (file.getParentFile().exists()) {
                file.mkdir();
            } else {
                mkDir(file.getParentFile());
                file.mkdir();
            }
        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win"))
            return true;
        return false;
    }

    public static String toUTF8(String stc) {
        String dest = "";
        try {
            dest = new String(stc.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("编码异常", e);
            throw new BusinessException("预览失败");
        }
        return dest;
    }

    /**
     * TODO 下载文件到本地
     *
     * @param fileUrl   远程地址
     * @param fileLocal 本地路径
     * @throws Exception
     */
    public static void downloadFile(String fileUrl, String fileLocal) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(Constant.DOWNLOAD_TIMEOUT);
        urlCon.setReadTimeout(Constant.DOWNLOAD_TIMEOUT);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        //读文件流
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        out.close();
        in.close();
        logger.info("文件下载成功");
    }

    public static String pathDecode(String src) {
        String path = "";
        try {
            path = URLDecoder.decode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("路径解码失败");
            throw new BusinessException("预览失败");
        }
        return path;
    }


    public static String getFileExtension(String filaName) {
        return filaName.substring(filaName.lastIndexOf(".") + 1).toLowerCase();//文件扩展名
    }


    /**
     * @param fileSize 参数的单位为字节
     * @return
     */
    public static String convertFileSize(Long fileSize) {

        double fileSizeKB = BigDecimal.valueOf(fileSize).divide(BigDecimal.valueOf(ONE_MB_KB)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (fileSizeKB < ONE_MB_KB) {
            return fileSizeKB + "KB";
        } else {
            long fileSizeMB = BigDecimal.valueOf(fileSizeKB).divide(BigDecimal.valueOf(ONE_MB_KB)).setScale(1, BigDecimal.ROUND_HALF_UP).longValue();
            return fileSizeMB + "MB";
        }
    }

    /**
     * 获取文件尺寸
     */
    public static Double getFileByteSize(String fileSize) {
        String size = fileSize.substring(0, fileSize.length() - 2);
        if (fileSize.endsWith("KB")) {
            return Double.valueOf(size) * ONE_MB_KB;
        } else {
            return Double.valueOf(size) * ONE_MB_KB * ONE_MB_KB;
        }
    }

    public static void downloadTemplate(ContractTemplate contractTemplate, String templatePath) {
        File templateFile;
        templateFile = new File(templatePath);
        if (!templateFile.exists()) {
            try {
                FileUtils.downloadFile(FileUtils.pathDecode(contractTemplate.getUrl()), templatePath);
            } catch (Exception e) {
                logger.error("下载模板文件失败", e);
                throw new BusinessException("下载模板文件失败");
            }
        }
    }

    public static   String getFileHeader(BufferedInputStream inputStream, int bytes) throws IOException {
        byte[] b = new byte[bytes];
        inputStream.mark(b.length+1);
        try {
            inputStream.read(b, 0, b.length);
        } finally {
            inputStream.reset();
        }
        return bytesToHex(b);
    }

    private static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toLowerCase();
    }

    public static void closeOutputStream(OutputStream out){
        if(out!=null){
            try {
                out.close();
            } catch (IOException e) {
                logger.error("关闭文件输出流异常",e);
                throw new BusinessException("关闭文件异常");
            }
        }

    }

    public static void closeInputStream(InputStream in){
        if(in!=null){
            try {
                in.close();
            } catch (IOException ex) {
                logger.error("关闭文件输入流异常",ex);
                throw new BusinessException("关闭文件异常");
            }
        }

    }

    public static String getResourcePath(String shortPath){
        String path=FileUtils.class.getClassLoader().getResource("").getPath()+shortPath;
        File file = new File(path);
        if (!file.exists()) {
            throw new BusinessException("找不到资源文件!");
        }
        return file.getAbsolutePath();
    }

    public static File getResourceFile(String shortPath){
        String path=FileUtils.class.getClassLoader().getResource("").getPath()+shortPath;
        File file = new File(path);
        if (!file.exists()) {
            throw new BusinessException("找不到资源文件!");
        }
        return file;
    }


    public static String getFileDir(String filePath){
        int indexOfLastPathSeparator=filePath.indexOf(File.separator);
        if(indexOfLastPathSeparator<0)
            throw new BusinessException("文件路径有误");
        return filePath.substring(0,indexOfLastPathSeparator);
    }

    public static InputStream getFileInputStreamInJar(String shortPath){
        return org.springframework.util.ClassUtils.class.getClassLoader().getResourceAsStream(shortPath);
    }

}
