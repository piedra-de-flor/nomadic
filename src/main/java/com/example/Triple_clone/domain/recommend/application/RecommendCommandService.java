package com.example.Triple_clone.domain.recommend.application;

import com.example.Triple_clone.common.file.FileManager;
import com.example.Triple_clone.common.file.Image;
import com.example.Triple_clone.common.logging.logMessage.RecommendLogMessage;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.domain.Role;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.recommend.domain.*;
import com.example.Triple_clone.domain.recommend.infra.RecommendationBlockRepository;
import com.example.Triple_clone.domain.recommend.infra.RecommendationRepository;
import com.example.Triple_clone.domain.recommend.web.dto.*;
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
public class RecommendCommandService {
    private final RecommendationRepository repository;
    private final UserService userService;
    private final RecommendationBlockRepository blockRepository;
    private final FileManager fileManager;
    private final ObjectMapper objectMapper;
    private final RecommendLikeMap likes;

    @Transactional
    public Recommendation createRecommendation(RecommendationCreateDto createRecommendationRequestDto, String email) {
        try {
            Location location = objectMapper.readValue(createRecommendationRequestDto.location(), Location.class);

            Member author = userService.findByEmail(email);

            if (createRecommendationRequestDto.type().equals(RecommendationType.PLACE) && !author.getRoles().contains(Role.ADMIN.role)) {
                log.warn(RecommendLogMessage.RECOMMEND_PLACE_COMMAND_AUTH_FAILED.format());
                throw new IllegalArgumentException("추천 장소 생성 권한 오류 - 관리자만 추천 장소를 포스팅할 수 있습니다.");
            }
            
            Set<String> tags = null;
            if (createRecommendationRequestDto.tags() != null && !createRecommendationRequestDto.tags().trim().isEmpty()) {
                tags = Arrays.stream(createRecommendationRequestDto.tags().split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet());
            }

            Recommendation recommendation = Recommendation.builder()
                    .title(createRecommendationRequestDto.title())
                    .subTitle(createRecommendationRequestDto.subTitle())
                    .location(location)
                    .price(createRecommendationRequestDto.price())
                    .type(createRecommendationRequestDto.type())
                    .author(author)
                    .blocks(null)
                    .build();

            if (tags != null) {
                recommendation.addTags(tags);
            }

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
    public RecommendReadDto updateRecommendation(RecommendationUpdateDto updateRecommendationRequestDto, String email) {
        Member author = userService.findByEmail(email);
        Recommendation recommendation = repository.findById(updateRecommendationRequestDto.placeId())
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 컨텐츠 수정 실패", updateRecommendationRequestDto.placeId()));
                    return new EntityNotFoundException("no place entity for update");
                });

        if (recommendation.isMine(author)) {
            recommendation.update(updateRecommendationRequestDto.title(),
                    updateRecommendationRequestDto.subTitle(),
                    updateRecommendationRequestDto.location(),
                    updateRecommendationRequestDto.price());

            return new RecommendReadDto(recommendation, author, recommendation.isLikedBy(author.getId()));
        }

        throw new IllegalArgumentException("추천 포스팅 수정 실패");
    }

    @Transactional
    public long deleteRecommendation(Long recommendationId, String email) {
        Member author = userService.findByEmail(email);
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> {
                    log.warn(RecommendLogMessage.RECOMMEND_SEARCH_FAILED.format("추천 장소 삭제 실패", recommendationId));
                    return new EntityNotFoundException("no place entity for delete");
                });

        if (recommendation.isMine(author)) {
            repository.delete(recommendation);
            fileManager.deleteExistingImage(recommendation.getMainImage().getStoredFileName());
            return recommendationId;
        }

        throw new IllegalArgumentException("추천 포스팅 삭제 실패");
    }

    @Transactional
    public RecommendationBlock addBlock(Long recommendationId, RecommendationBlockCreateDto createDto, String email) {
        Member author = userService.findByEmail(email);
        Recommendation recommendation = repository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("no place entity"));

        if (!recommendation.isMine(author)) throw new IllegalArgumentException("추천 포스팅 컨텐츠 추가 실패");

        if (createDto.getType() == null || createDto.getType().trim().isEmpty()) {
            log.error("블록 타입이 null이거나 비어있음: {}", createDto.getType());
            throw new IllegalArgumentException("블록 타입은 필수입니다");
        }
        if (createDto.getOrderIndex() == null || createDto.getOrderIndex().trim().isEmpty()) {
            log.error("순서 인덱스가 null이거나 비어있음: {}", createDto.getOrderIndex());
            throw new IllegalArgumentException("순서 인덱스는 필수입니다");
        }

        BlockType blockType;
        try {
            blockType = BlockType.valueOf(createDto.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 블록 타입입니다: " + createDto.getType() + ". TEXT 또는 IMAGE를 사용하세요.");
        }

        Image image = null;
        if (blockType == BlockType.IMAGE && createDto.getImageFile() != null) {
            image = fileManager.uploadImage(createDto.getImageFile());
        }

        int orderIndex;
        try {
            orderIndex = Integer.parseInt(createDto.getOrderIndex());
            if (orderIndex < 0) {
                throw new IllegalArgumentException("순서 인덱스는 0 이상이어야 합니다");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("순서 인덱스는 숫자여야 합니다: " + createDto.getOrderIndex());
        }

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
    public void removeBlock(Long blockId, String email) {
        Member author = userService.findByEmail(email);
        RecommendationBlock block = blockRepository.findById(blockId)
                .orElseThrow(() -> new EntityNotFoundException("no block entity"));

        if (!block.getRecommendation().isMine(author)) throw new IllegalArgumentException("추천 포스팅 컨텐츠 삭제 실패");

        if (block.getImage() != null) {
            fileManager.deleteExistingImage(block.getImage().getStoredFileName());
        }
        
        block.getRecommendation().removeBlock(block);
        blockRepository.delete(block);
    }

    @Transactional
    public RecommendationBlockUpdateDto updateBlock(Long blockId, RecommendationBlockUpdateDto updateDto, String email) {
        Member author = userService.findByEmail(email);
        RecommendationBlock block = blockRepository.findById(blockId)
                .orElseThrow(() -> new EntityNotFoundException("no block entity"));

        if (!block.getRecommendation().isMine(author)) throw new IllegalArgumentException("추천 포스팅 컨텐츠 수정 실패");

        BlockType blockType;
        try {
            blockType = BlockType.valueOf(updateDto.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 블록 타입입니다: " + updateDto.getType() + ". TEXT 또는 IMAGE를 사용하세요.");
        }

        if (updateDto.getImageFile() != null && block.getImage() != null) {
            fileManager.deleteExistingImage(block.getImage().getStoredFileName());
        }

        Image image = block.getImage();
        if (blockType == BlockType.IMAGE && updateDto.getImageFile() != null) {
            image = fileManager.uploadImage(updateDto.getImageFile());
        }

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

        blockRepository.save(block);
        return updateDto;
    }

    public void toggleLike(Long recommendationId, Long memberId) {
        likes.insert(memberId, recommendationId);
    }
}
