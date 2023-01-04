package com.example.school.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.example.school.service.oss.service.FileService;
import com.example.school.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private OssProperties ossProperties;
    /**
     * 阿里云oss文件上传
     *
     * @param inputStream      输入流
     * @param module           文件夹名称
     * @param originalFilename 原始文件名
     * @return 文件在oss服务器上的url地址
     */
    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {
        // 读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String bucketname = ossProperties.getBucketname();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        //如果不存在声明的bucket，则自己创建一个
        if (!ossClient.doesBucketExist(bucketname)) {
            ossClient.createBucket(bucketname);
            // 赋予公共读权限
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        // 自建objectName， 文件路径avatar/2020/04/15/default.jpg
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = module + "/" + folder + "/" + fileName + fileExtension;

        // 上传文件流
        ossClient.putObject(bucketname, key, inputStream);

        // 关闭OSSClient
        ossClient.shutdown();

        // 返回url
        return "https://" + bucketname + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        // 读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String bucketname = ossProperties.getBucketname();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        // 删除文件
        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucketname, objectName);

        // 关闭OSSClient
        ossClient.shutdown();
    }
}
