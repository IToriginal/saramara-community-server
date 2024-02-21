package com.kakao.saramaracommunity.bucket.service;

import com.kakao.saramaracommunity.bucket.dto.business.response.BucketUploadResponse;
import com.kakao.saramaracommunity.bucket.exception.BucketBusinessException;
import com.kakao.saramaracommunity.util.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_MAX_RANGE_OUT;
import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_MIN_RANGE_OUT;

/**
 * AWS S3 버킷에 클라이언트의 요청 이미지를 업로드하기 위한 비즈니스 로직을 수행할 서비스 인터페이스의 구현체 클래스입니다.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final AwsS3Uploader awsS3Uploader;

    @Value("${image-rule.max}")
    private int MAX_IMAGE_COUNT;

    @Override
    public BucketUploadResponse uploadImages(List<MultipartFile> requestImageFiles) {
        if(checkImageIsEmpty(requestImageFiles)) {
            log.info("[BucketServiceImpl] 이미지는 최소 1장 이상 업로드해야 합니다.");
            throw new BucketBusinessException(BUCKET_IMAGE_MIN_RANGE_OUT);
        }
        if (checkImageCount(requestImageFiles.size())) {
            log.info("[BucketServiceImpl] 업로드할 이미지 개수가 너무 많습니다. 요청으로 받은 이미지 목록 개수: {}", requestImageFiles.size());
            throw new BucketBusinessException(BUCKET_IMAGE_MAX_RANGE_OUT);
        }
        List<String> imagePathList = uploadS3BucketWithGetUrls(requestImageFiles);
        log.info("[BucketServiceImpl] AWS S3 버킷에 이미지를 정상적으로 등록했습니다. S3 버킷 등록 이미지 목록: {}", imagePathList);
        return BucketUploadResponse.of(imagePathList);
    }

    private boolean checkImageIsEmpty(List<MultipartFile> images) {
        return images.isEmpty();
    }

    private boolean checkImageCount(int imageCount) {
        return imageCount > MAX_IMAGE_COUNT;
    }

    private List<String> uploadS3BucketWithGetUrls(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(awsS3Uploader::upload)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
