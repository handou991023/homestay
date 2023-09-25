package homestay.app.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

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
        ossClient.putObject(
                "homestayimage ",//仓库名
                fileName,//文件名
                file.getInputStream()
        );
        ossClient.shutdown();;
        return ALI_DOMAIN + fileName;
    }


}
