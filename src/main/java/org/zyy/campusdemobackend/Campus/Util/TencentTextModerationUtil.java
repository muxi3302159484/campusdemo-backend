
package org.zyy.campusdemobackend.Campus.Util;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import com.tencentcloudapi.tms.v20201229.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
/**
 * 腾讯云文本内容安全工具类
 * GitHub: muxi3302159484
 */
@Component
public class TencentTextModerationUtil {

    private static String secretId;
    private static String secretKey;

    @Value("${tencent.secret-id}")
    public void setSecretId(String id) {
        TencentTextModerationUtil.secretId = id;
    }

    @Value("${tencent.secret-key}")
    public void setSecretKey(String key) {
        TencentTextModerationUtil.secretKey = key;
    }

    public static TextModerationResponse checkText(String text) throws TencentCloudSDKException {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        TmsClient client = new TmsClient(cred, "ap-guangzhou", clientProfile);

        TextModerationRequest req = new TextModerationRequest();
        String base64Content = Base64.getEncoder().encodeToString(text.getBytes());
        req.setContent(base64Content);
        req.setBizType("TencentCloudDefault");

        return client.TextModeration(req);
    }
}