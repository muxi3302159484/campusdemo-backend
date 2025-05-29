package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.Util.TencentTextModerationUtil;
import org.zyy.campusdemobackend.Campus.Util.TencentImageModerationUtil;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationResponse;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TextModerationController {
    @PostMapping("/text-moderation")
    public Map<String, Object> moderate(@RequestBody Map<String, String> body) throws TencentCloudSDKException {
        String content = body.get("content");
        TextModerationResponse resp = TencentTextModerationUtil.checkText(content);
        return Map.of(
                "Label", resp.getLabel(),
                "Suggestion", resp.getSuggestion(),
                "Score", resp.getScore()
        );
    }
    @PostMapping("/image-moderation")
    public Map<String, Object> imageModerate(@RequestBody Map<String, String> body) throws Exception {
        String imagePath = body.get("imagePath");
        ImageModerationResponse resp = TencentImageModerationUtil.checkImage(imagePath);
        return Map.of(
                "Label", resp.getLabel(),
                "Suggestion", resp.getSuggestion(),
                "Score", resp.getScore()
        );
    }
}