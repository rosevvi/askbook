package com.rosevvi.controller;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.rosevvi.domain.Image;
import com.rosevvi.tools.Code;
import com.rosevvi.tools.ImageResult;
import com.rosevvi.tools.Result;
import com.rosevvi.tools.TencentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author: rosevvi
 * @date: 2023/3/27 15:00
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/images")
@Slf4j
public class MutilPartController {

    @Value("${imagePath}")
    private String filePath;

    @PostMapping("/upLoad")
    public Result<String> upload(MultipartFile file){
        log.info(file.getSize()+"大小");
        log.info(filePath);
        //传过来的是一个临时文件
        String filename = file.getOriginalFilename();
        log.info("图片文件名{}",filename);
        //获取到图片的后缀
        String suffer = filename.substring(filename.indexOf("."));
        //查看我们自己的存放图片的文件是否存在，不存在直接创建目录
        File abstractpath=new File(filePath);
        log.info(abstractpath.exists()+"是否存在");
        if (!abstractpath.exists()){
            //不存在就创建
            abstractpath.mkdirs();
        }
        //用uuid来创建一个图片名 确保每个图片都是不一样的
        UUID uuid = UUID.randomUUID();
        //路径拼接为最后的路径
        String imageName=uuid+suffer;
        String finalPath=filePath+"\\"+imageName;
        log.info(finalPath);
        //将临时文件转移到我们创建的路径下面
        try {
            file.transferTo(new File(finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(Code.OK,"",finalPath);
    }

    @GetMapping("/downLoad")
    public void downLoad(String file, HttpServletResponse response){
        try {
            //创建文件输入流
            log.error(file);
            FileInputStream fileInputStream=new FileInputStream(filePath+"\\"+file);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len =0;
            //将图片读出来
            if ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片到腾讯云cos
     * @param image
     * @return
     */
    @PostMapping("/uploadCos")
    public ImageResult<Image> uploadCos(MultipartFile image){
        String url=null;
        InputStream inputStream = null;
        Image images=new Image();
        try {
            // 1 初始化用户身份信息（secretId, secretKey）。
            // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
            String secretId = System.getenv(TencentUtils.SECRET_ID);//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            String secretKey = System.getenv(TencentUtils.SECRET_KEY);//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            COSCredentials cred = new BasicCOSCredentials(TencentUtils.SECRET_ID, TencentUtils.SECRET_KEY);
            // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
            // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
            Region region = new Region(TencentUtils.REGION);
            ClientConfig clientConfig = new ClientConfig(region);
            // 这里建议设置使用 https 协议
            // 从 5.6.54 版本开始，默认使用了 https
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3 生成 cos 客户端。
            COSClient cosClient = new COSClient(cred, clientConfig);

            //获取临时文件名
            String originalFilename = image.getOriginalFilename();
            int index = originalFilename.indexOf(".");
            //获取文件后缀
            String suffer = originalFilename.substring(index);
            UUID uuid = UUID.randomUUID();
            //路径拼接为最后的路径
            String imageName=uuid+suffer;
            // 指定文件将要存放的存储桶
            String bucketName = TencentUtils.BUCKER_NAME;
            // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
            String key = "askbook/images/"+imageName;
    //        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
    //        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            inputStream =image.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    key,
                    inputStream,
                    objectMetadata);
            // 高级接口会返回一个异步结果Upload
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            //返回上传文件的路径
            url = "https://"+bucketName+"."+"cos"+"."+ TencentUtils.REGION+".myqcloud.com"+"/"+key;
            images.setUrl(url);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ImageResult.success(1,"上传失败");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ImageResult.success(0,"上传成功",images);
    }
}
