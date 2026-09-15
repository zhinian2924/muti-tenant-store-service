package com.example.storesaas.media;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file, String scene);

    void deleteUrl(String url);
}
