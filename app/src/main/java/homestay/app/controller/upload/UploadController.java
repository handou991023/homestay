package homestay.app.controller.upload;

import homestay.module.utils.Response;
import homestay.module.utils.UploadUtil;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@RestController
public class UploadController {
    @RequestMapping("upImg")
    public Response<String> upImg(@RequestParam(value = "file")MultipartFile file)  {
        try{
            return new Response<>(1001,UploadUtil.uploadImage(file));
        }catch (IOException e){
            return new Response<>(4004);
        }
    }
}
