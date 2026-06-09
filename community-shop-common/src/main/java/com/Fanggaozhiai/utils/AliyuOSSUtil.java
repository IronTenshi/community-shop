package com.Fanggaozhiai.utils;

import com.Fanggaozhiai.config.AliyuConfig;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliyuOSSUtil {
    @Autowired
    private AliyuConfig aliyunOSSProperties;
//    @Value("${aliyun.oss.endpoint}")
//    private String endpoint;
//    @Value("${aliyun.oss.bucketName}")
//    private String bucketName;
//    @Value("${aliyun.oss.region}")
//    private String region;


    //上传文件 file  返回阿里云地址
    //用于存放图片和预览
    public String upload(byte[] file , String originalFilename) throws ClientException {
        //从环境中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(aliyunOSSProperties.getEndpoint())
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(aliyunOSSProperties.getRegion())
                .build();

        String dir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String newFilename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = dir + "/" + newFilename;
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliyunOSSProperties.getBucketName(), objectName, new ByteArrayInputStream(file));
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        }finally {
            ossClient.shutdown();
        }

        return aliyunOSSProperties.getEndpoint().split("//")[0] + "//" + aliyunOSSProperties.getBucketName() + "." + aliyunOSSProperties.getEndpoint().split("//")[1] + "/" + objectName;
    }
}
