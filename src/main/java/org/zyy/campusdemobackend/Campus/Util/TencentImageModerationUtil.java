package org.zyy.campusdemobackend.Campus.Util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ims.v20201229.ImsClient;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationRequest;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationResponse;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class TencentImageModerationUtil {

    private static String secretId;
    private static String secretKey;

    @Value("${tencent.secret-id}")
    public void setSecretId(String id) {
        TencentImageModerationUtil.secretId = id;
    }

    @Value("${tencent.secret-key}")
    public void setSecretKey(String key) {
        TencentImageModerationUtil.secretKey = key;
    }

    // 本地图片内容审核
    public static ImageModerationResponse checkImage(String imagePath) throws Exception {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ims.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        ImsClient client = new ImsClient(cred, "ap-guangzhou", clientProfile);

        ImageModerationRequest req = new ImageModerationRequest();
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        req.setFileContent(base64Image);
        req.setBizType("TencentCloudDefault");

        return client.ImageModeration(req);
    }
}