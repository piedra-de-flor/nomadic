package com.example.Triple_clone.domain.recommend.application;

import com.example.Triple_clone.common.file.FileManager;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.recommend.domain.BlockType;
import com.example.Triple_clone.domain.recommend.domain.PostMeta;
import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.domain.RecommendationBlock;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import com.example.Triple_clone.domain.recommend.infra.RecommendationBlockRepository;
import com.example.Triple_clone.domain.recommend.web.dto.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.domain.recommend.web.dto.AdminRecommendUpdateRecommendationDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendationBlockCreateDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendationBlockUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRecommendService {
    private final RecommendationRepository repository;
    private final RecommendationBlockRepository blockRepository;
    private final FileManager fileManager;
    private final ObjectMapper objectMapper;

    @Transactional
    public Recommendation createRecommendation(AdminRecommendCreateRecommendationDto createRecommendationRequestDto) {
        try {
            // JSON 문자열 파싱
            Location location = objectMapper.readValue(createRecommendationRequestDto.location(), Location.class);
            
            PostMeta postMeta = null;
            if (createRecommendationRequestDto.postMeta() != null && !createRecommendationRequestDto.postMeta().trim().isEmpty()) {
                postMeta = objectMapper.readValue(createRecommendationRequestDto.postMeta(), PostMeta.class);
            } else {
                // postMeta가 없을 때 기본값 설정
                postMeta = PostMeta.builder()
                        .authorName("관리자")
                        .authorProfileUrl(null)
                        .build();
            }
            
            Set<String> tags = null;
            if (createRecommendationRequestDto.tags() != null && !createRecommendationRequestDto.tags().trim().isEmpty()) {
                tags = Arrays.stream(createRecommendationRequestDto.tags().split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet());
            }
            
            // Recommendation 엔티티 생성
            Recommendation recommendation = Recommendation.builder()
                    .title(createRecommendationRequestDto.title())
                    .subTitle(createRecommendationRequestDto.subTitle())
                    .location(location)
                    .price(createRecommendationRequestDto.price())
                    .type(createRecommendationRequestDto.type())
                    .postMeta(postMeta)
                    .blocks(null)
                    .build();
            
            // 태그 추가
            if (tags != null) {
                recommendation.addTags(tags);
            }
            
            // 메인 이미지 업로드 처리
            if (createRecommendationRequestDto.mainImage() != null) {
                Image mainImage = fileManager.uploadImage(createRecommendationRequestDto.mainImage());
                recommendation.setImage(mainImage);
            }
            
            repository.save(recommendation);
            return recommendation;
            
        } catch (Exception e) {
            log.error("추천 장소 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new IllegalArgumentException("추천 장소 생성에 실패했습니다: " + e.getMessage());
        }
    }


    @Transactional
    public Long setMainImageOfRecommendation(Long recommendationId, MultipartFile image) {
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 이미지 수정 실패", recommendationId));
                    return new EntityNotFoundException("no place entity for update");
                });
        Image mainImage = fileManager.uploadImage(image);
        recommendation.setImage(mainImage);
        return recommendationId;
    }

    @Transactional
    public Recommendation updateRecommendation(AdminRecommendUpdateRecommendationDto updateRecommendationRequestDto) {
        Recommendation recommendation = repository.findById(updateRecommendationRequestDto.placeId())
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 컨텐츠 수정 실패", updateRecommendationRequestDto.placeId()));
                    return new EntityNotFoundException("no place entity for update");
                });

        recommendation.update(updateRecommendationRequestDto.title(),
                updateRecommendationRequestDto.subTitle(),
                updateRecommendationRequestDto.location(),
                updateRecommendationRequestDto.price(),
                updateRecommendationRequestDto.postMeta());

        return recommendation;
    }

    @Transactional
    public long deleteRecommendation(Long recommendationId) {
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 삭제 실패", recommendationId));
                    return new EntityNotFoundException("no place entity for delete");
                });

        repository.delete(recommendation);
        fileManager.deleteExistingImage(recommendation.getMainImage().getStoredFileName());
        return recommendationId;
    }

    @Transactional
    public RecommendationBlock addBlock(Long recommendationId, RecommendationBlockCreateDto createDto) {
        log.info("addBlock 호출됨 - recommendationId: {}, createDto: {}", recommendationId, createDto);
        
        // 수동 검증
        if (createDto.getType() == null || createDto.getType().trim().isEmpty()) {
            log.error("블록 타입이 null이거나 비어있음: {}", createDto.getType());
            throw new IllegalArgumentException("블록 타입은 필수입니다");
        }
        if (createDto.getOrderIndex() == null || createDto.getOrderIndex().trim().isEmpty()) {
            log.error("순서 인덱스가 null이거나 비어있음: {}", createDto.getOrderIndex());
            throw new IllegalArgumentException("순서 인덱스는 필수입니다");
        }
        
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("no place entity"));
        
        // type 파싱
        BlockType blockType;
        try {
            blockType = BlockType.valueOf(createDto.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 블록 타입입니다: " + createDto.getType() + ". TEXT 또는 IMAGE를 사용하세요.");
        }
        
        // 이미지 업로드 처리
        Image image = null;
        if (blockType == BlockType.IMAGE && createDto.getImageFile() != null) {
            image = fileManager.uploadImage(createDto.getImageFile());
        }
        
        // orderIndex 파싱
        int orderIndex;
        try {
            orderIndex = Integer.parseInt(createDto.getOrderIndex());
            if (orderIndex < 0) {
                throw new IllegalArgumentException("순서 인덱스는 0 이상이어야 합니다");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("순서 인덱스는 숫자여야 합니다: " + createDto.getOrderIndex());
        }
        
        // 블록 생성
        RecommendationBlock block = RecommendationBlock.builder()
                .type(blockType)
                .text(createDto.getText())
                .image(image)
                .caption(createDto.getCaption())
                .orderIndex(orderIndex)
                .recommendation(recommendation)
                .build();
        
        recommendation.addBlock(block);
        return blockRepository.save(block);
    }


    @Transactional
    public void removeBlock(Long blockId) {
        RecommendationBlock block = blockRepository.findById(blockId)
                .orElseThrow(() -> new EntityNotFoundException("no block entity"));
        
        // 기존 이미지 삭제
        if (block.getImage() != null) {
            fileManager.deleteExistingImage(block.getImage().getStoredFileName());
        }
        
        block.getRecommendation().removeBlock(block);
        blockRepository.delete(block);
    }

    @Transactional
    public RecommendationBlock updateBlock(Long blockId, RecommendationBlockUpdateDto updateDto) {
        RecommendationBlock block = blockRepository.findById(blockId)
                .orElseThrow(() -> new EntityNotFoundException("no block entity"));
        
        // type 파싱
        BlockType blockType;
        try {
            blockType = BlockType.valueOf(updateDto.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 블록 타입입니다: " + updateDto.getType() + ". TEXT 또는 IMAGE를 사용하세요.");
        }
        
        // 기존 이미지 삭제 (새 이미지가 업로드되는 경우)
        if (updateDto.getImageFile() != null && block.getImage() != null) {
            fileManager.deleteExistingImage(block.getImage().getStoredFileName());
        }
        
        // 새 이미지 업로드
        Image image = block.getImage(); // 기존 이미지 유지
        if (blockType == BlockType.IMAGE && updateDto.getImageFile() != null) {
            image = fileManager.uploadImage(updateDto.getImageFile());
        }
        
        // orderIndex 파싱
        int orderIndex;
        try {
            orderIndex = Integer.parseInt(updateDto.getOrderIndex());
            if (orderIndex < 0) {
                throw new IllegalArgumentException("순서 인덱스는 0 이상이어야 합니다");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("순서 인덱스는 숫자여야 합니다: " + updateDto.getOrderIndex());
        }
        
        block.update(blockType, updateDto.getText(), 
                    image, updateDto.getCaption(), 
                    orderIndex);
        
        return blockRepository.save(block);
    }
}
