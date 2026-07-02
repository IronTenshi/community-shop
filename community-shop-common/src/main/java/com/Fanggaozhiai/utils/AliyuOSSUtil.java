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

/**
 * 阿里云OSS文件上传工具类
 * 负责将图片文件上传到阿里云OSS，并返回可访问的URL
 * 使用环境变量 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET 进行身份认证
 */
@Component
public class AliyuOSSUtil {
    @Autowired
    private AliyuConfig aliyunOSSProperties;

    /**
     * 上传文件到阿里云OSS
     * 文件按日期分目录存储（yyyy/MM/dd），文件名使用UUID防止冲突
     *
     * @param file             文件字节数组
     * @param originalFilename 原始文件名（用于提取扩展名）
     * @return 上传后的文件访问URL
     * @throws ClientException OSS上传异常
     */
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
