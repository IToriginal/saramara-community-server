package com.kakao.saramaracommunity.attach.service;


import com.kakao.saramaracommunity.attach.dto.request.AttachRequest;
import com.kakao.saramaracommunity.attach.dto.response.AttachResponse;
import com.kakao.saramaracommunity.common.dto.Payload;

/**
 * AttachService: 이미지 첨부파일 관련 비즈니스 로직을 수행할 서비스 인터페이스
 *
 * @author Taejun
 * @version 0.0.1
 */
public interface AttachService {

    AttachResponse.UploadBucketResponse uploadS3BucketImages(AttachRequest.UploadBucketRequest request);

    AttachResponse.UploadResponse uploadImages(AttachRequest.UploadRequest request);

    AttachResponse.GetImageResponse getBoardImages(AttachRequest.GetBoardImageRequest request);

    AttachResponse.GetAllImageResponse getAllBoardImages();

    AttachResponse.UpdateResponse updateImage(AttachRequest.UpdateRequest request);

    AttachResponse.DeleteResponse deleteImage(Long attachId);

}
