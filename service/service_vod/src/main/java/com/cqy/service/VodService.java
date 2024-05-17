package com.cqy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface VodService {
    String uploadAliVideo(MultipartFile file);

    void removeAlyVideo(List<String> ids);
}
