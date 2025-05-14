package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @PostMapping
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        // 模拟上传，返回随机 URL
        return "https://example.com/images/" + UUID.randomUUID() + ".jpg";
    }
}