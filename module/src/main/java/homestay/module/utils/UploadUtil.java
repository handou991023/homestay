package homestay.module.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadUtil {
    //阿里域名
    public static final String ALI_DOMAIN = "https://homestayimage.oss-cn-hangzhou.aliyuncs.com/";

    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名
        String originalFilename = file.getOriginalFilename();//原来的图片名
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileName = uuid + ext;
        //地域节点
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "LTAI5tSHRmphpmPqtYA8rVHh";
        String accessKeySecret = "9X0Tqv15UmzxbVEeZbS80RlFSy1EEm";
        //oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        ApplicationHome applicationHome = new ApplicationHome(ossClient.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
                "\\src\\main\\resources\\static\\images\\";
        String path = pre + fileName;
        ossClient.putObject(
                "homestayimage",//仓库名
                fileName,//文件名
                file.getInputStream()
        );
        ossClient.shutdown();
        return ALI_DOMAIN + fileName;
    }







    public static String uploadImg(MultipartFile file) throws IOException {
        File f = new File(String.valueOf(file));
        BufferedImage bi = ImageIO.read(f);
        int height = 500;
        int width = 500;
        //生成文件名
        String originalFilename = file.getOriginalFilename();//原来的图片名
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;
        double wRatio = (new Integer(width)).doubleValue() / bi.getWidth(); //宽度的比例
        double hRatio = (new Integer(height)).doubleValue() / bi.getHeight(); //高度的比例
        Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH); //设置图像的缩放大小
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(wRatio, hRatio), null); //设置图像的缩放比例
        image = op.filter(bi, null);

        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "LTAI5tSHRmphpmPqtYA8rVHh";
        String accessKeySecret = "9X0Tqv15UmzxbVEeZbS80RlFSy1EEm";
        //oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        ossClient.putObject(
                "homestayimage",//仓库名
                fileName,//文件名
                file.getInputStream()
        );
        ossClient.shutdown();;
        int lastLength = fileName.lastIndexOf(".");
        String subFilePath = fileName.substring(0, lastLength); //得到图片输出路径
        String fileType = fileName.substring(lastLength); //图片类型
        File zoomFile = new File(subFilePath + "_" + width + "*" + height + fileType);
        ImageIO.write((BufferedImage) image, "jpg", zoomFile);
        String filepath = String.valueOf(new ImageIcon(zoomFile.getPath()));
        return ALI_DOMAIN + filepath;
    }
}
