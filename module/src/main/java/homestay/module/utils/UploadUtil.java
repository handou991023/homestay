package homestay.module.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FileUtils;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
public class UploadUtil {

    //阿里域名
    public static final String ALI_DOMAIN = "https://homestayimage.oss-cn-hangzhou.aliyuncs.com/";

    public static String uploadImage(MultipartFile file) throws IOException {
        //生成文件名
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //获取默认定位到的当前用户目录("user.dir"),也就是当前应用的根路径
        String tempDir = System.getProperty("user.dir");
        //根目录下生成临时文件
        File F = new File(tempDir+File.separator+originalFilename);
        FileUtils.copyInputStreamToFile(file.getInputStream(), F);

        BufferedImage bufferedImage = ImageIO.read(F);
        Image image = bufferedImage.getScaledInstance(-1, -1, Image.SCALE_DEFAULT);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM/dd");
        String date = sdf.format(new Date());
        String fileName = "image" + "/" + date + "/" + uuid + "_" + width + "x" + height + ext;
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
}
