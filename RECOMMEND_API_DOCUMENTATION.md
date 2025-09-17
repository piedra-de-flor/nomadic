# 추천 장소 API 문서

## 개요
이 문서는 Triple_clone 프로젝트의 추천 장소(Recommend) 도메인 API들을 정리한 문서입니다. 추천 장소 생성, 조회, 수정, 삭제 및 블록 관리 기능을 제공합니다.

## API 엔드포인트 목록

### 1. 추천 장소 관리 (Command API)

#### 1.1 추천 장소 생성
- **URL**: `POST /recommend`
- **설명**: 새로운 추천 장소를 생성합니다 (이미지 파일 포함)
- **인증**: 필요 (MemberEmailAspect)
- **Content-Type**: `multipart/form-data`

**요청 DTO**: `RecommendCreateRecommendationDto`
```java
public record RecommendCreateRecommendationDto(
    @NotBlank String title,                    // 제목 (필수)
    String subTitle,                           // 부제목
    @NotNull String location,                  // 위치 정보 (JSON 문자열, 필수)
    String price,                              // 가격 정보
    @NotNull RecommendationType type,          // 추천 타입 (PLACE/POST, 필수)
    String tags,                               // 태그 목록 (쉼표로 구분)
    MultipartFile mainImage                    // 메인 이미지 파일
)
```

**응답**: `Recommendation` 엔티티

**예시 요청**:
```
POST /recommend
Content-Type: multipart/form-data

title: "제주도 한라산"
subTitle: "한국의 최고봉"
location: {"address":"제주특별자치도","latitude":33.3617,"longitude":126.5292}
price: "무료"
type: "PLACE"
tags: "산,등산,자연"
mainImage: [이미지 파일]
```

#### 1.2 추천 장소 이미지 추가
- **URL**: `POST /recommend/image`
- **설명**: 추천 장소에 이미지를 추가합니다
- **인증**: 필요

**요청 파라미터**:
- `recommendationId` (Long): 이미지를 추가할 추천 장소 ID
- `image` (MultipartFile): 추가할 이미지

**응답**: `Long` (추천 장소 ID)

#### 1.3 추천 장소 수정
- **URL**: `PATCH /recommend`
- **설명**: 기존 추천 장소를 수정합니다
- **인증**: 필요 (MemberEmailAspect)

**요청 DTO**: `RecommendUpdateRecommendationDto`
```java
public record RecommendUpdateRecommendationDto(
    long placeId,                              // 수정할 장소 ID
    String title,                              // 제목
    String subTitle,                           // 부제목
    Location location,                         // 위치 정보
    String price,                              // 가격 정보
    PostMeta postMeta                          // 포스트 메타 정보
)
```

**응답**: `Recommendation` 엔티티

#### 1.4 추천 장소 삭제
- **URL**: `DELETE /recommend`
- **설명**: 기존 추천 장소를 삭제합니다
- **인증**: 필요 (MemberEmailAspect)

**요청 파라미터**:
- `placeId` (long): 삭제할 추천 장소 ID

**응답**: `Long` (삭제된 장소 ID)

#### 1.5 좋아요 토글
- **URL**: `PUT /recommendation/like`
- **설명**: 추천 장소에 좋아요 또는 좋아요 취소를 합니다

**요청 파라미터**:
- `recommendationId` (long): 추천 장소 ID
- `memberId` (long): 회원 ID

**응답**: `Long` (추천 장소 ID)

### 2. 추천 장소 조회 (Query API)

#### 2.1 추천 장소 전체 조회 (정렬 가능)
- **URL**: `GET /recommendations`
- **설명**: 정렬순에 맞춰 추천 장소를 전체 조회합니다

**요청 파라미터**:
- `sort` (String, 선택): 정렬 타입 (기본값: "")
- `pageable` (Pageable): 페이징 정보

**정렬 옵션**:
- `CREATED_DESC`: 생성일 내림차순
- `CREATED_ASC`: 생성일 오름차순
- `LIKES_DESC`: 좋아요 수 내림차순
- `LIKES_ASC`: 좋아요 수 오름차순
- `VIEWS_DESC`: 조회수 내림차순
- `VIEWS_ASC`: 조회수 오름차순
- `REVIEWS_DESC`: 리뷰 수 내림차순
- `REVIEWS_ASC`: 리뷰 수 오름차순

**응답**: `Page<RecommendReadDto>`

#### 2.2 추천 장소 단일 조회
- **URL**: `GET /recommendation`
- **설명**: 기존 추천 장소를 단일 조회합니다
- **인증**: 필요 (MemberEmailAspect)

**요청 파라미터**:
- `placeId` (long): 추천 장소 ID

**응답**: `RecommendReadDto`

#### 2.3 추천 장소 이미지 조회
- **URL**: `GET /recommendation/image`
- **설명**: 추천 장소의 이미지를 가져옵니다

**요청 파라미터**:
- `recommendationId` (long): 이미지를 가져올 추천 장소 ID

**응답**: `byte[]` (이미지 데이터, Content-Type: image/png)

#### 2.4 인기 상위 10개 추천 장소 조회
- **URL**: `GET /recommendations/top10`
- **설명**: 좋아요 상위 10개의 추천 장소들을 조회합니다

**응답**: `List<RecommendReadTop10Dto>`

#### 2.5 추천 장소 검색
- **URL**: `GET /recommendations/search`
- **설명**: 키워드와 타입으로 추천 장소를 검색합니다

**요청 파라미터**:
- `q` (String, 선택): 검색 키워드
- `type` (RecommendationType, 선택): 추천 타입 (PLACE/POST)

**응답**: `List<RecommendReadDto>`

#### 2.6 랜덤 추천 장소 조회
- **URL**: `GET /recommendations/random`
- **설명**: 타입별로 랜덤한 추천 장소를 조회합니다

**요청 파라미터**:
- `type` (RecommendationType, 선택): 추천 타입 (PLACE/POST)
- `limit` (int, 기본값: 10): 조회할 개수

**응답**: `List<RecommendReadDto>`

#### 2.7 계획에 추천 장소 포함
- **URL**: `GET /recommendation/user/plan`
- **설명**: 추천 장소를 기존 계획에 포함합니다 (리다이렉트)

**요청 파라미터**:
- `target` (long): 타겟 계획 ID
- `placeId` (long): 계획에 추가할 추천 장소 ID

**응답**: 리다이렉트 URL

### 3. 블록 관리 API

#### 3.1 블록 추가
- **URL**: `POST /recommend/{recommendationId}/blocks`
- **설명**: 추천 장소에 새로운 블록을 추가합니다 (이미지 파일 포함)
- **인증**: 필요 (MemberEmailAspect)
- **Content-Type**: `multipart/form-data`

**요청 DTO**: `RecommendationBlockCreateDto`
```java
public class RecommendationBlockCreateDto {
    private String type;                       // 블록 타입 (TEXT/IMAGE, 필수)
    private String text;                       // 텍스트 내용 (TEXT 타입일 때)
    private MultipartFile imageFile;           // 이미지 파일 (IMAGE 타입일 때)
    private String caption;                    // 이미지 캡션 (IMAGE 타입일 때)
    private String orderIndex;                 // 블록 순서 인덱스 (필수)
}
```

**응답**: `RecommendationBlock` 엔티티

#### 3.2 블록 수정
- **URL**: `PATCH /recommend/blocks/{blockId}`
- **설명**: 기존 블록을 수정합니다
- **인증**: 필요 (MemberEmailAspect)
- **Content-Type**: `multipart/form-data`

**요청 DTO**: `RecommendationBlockUpdateDto`
```java
public class RecommendationBlockUpdateDto {
    private String type;                       // 블록 타입 (TEXT/IMAGE, 필수)
    private String text;                       // 텍스트 내용 (TEXT 타입일 때)
    private MultipartFile imageFile;           // 이미지 파일 (IMAGE 타입일 때, 선택사항)
    private String caption;                    // 이미지 캡션 (IMAGE 타입일 때)
    private String orderIndex;                 // 블록 순서 인덱스 (필수)
}
```

**응답**: `RecommendationBlock` 엔티티

#### 3.3 블록 삭제
- **URL**: `DELETE /recommend/blocks/{blockId}`
- **설명**: 기존 블록을 삭제합니다

**경로 변수**:
- `blockId` (Long): 삭제할 블록 ID

**응답**: `Void`

#### 3.4 블록 조회
- **URL**: `GET /recommendation/{recommendationId}/blocks`
- **설명**: 추천 장소의 모든 블록을 순서대로 조회합니다

**경로 변수**:
- `recommendationId` (Long): 추천 장소 ID

**응답**: `List<RecommendationBlockReadDto>`

## DTO 상세 정보

### 응답 DTO

#### RecommendReadDto
```java
public record RecommendReadDto(
    long id,                                   // 추천 장소 ID
    String title,                              // 제목
    String subTitle,                           // 부제목
    String author,                             // 작성자명
    Location location,                         // 위치 정보
    LocalDateTime createdAt,                   // 생성일시
    String price,                              // 가격 정보
    Set<String> tags,                          // 태그 목록
    int likesCount,                            // 좋아요 수
    int reviewsCount,                          // 리뷰 수
    int viewsCount,                            // 조회수
    boolean like                               // 현재 사용자의 좋아요 여부
)
```

#### RecommendReadTop10Dto
```java
public record RecommendReadTop10Dto(
    long id,                                   // 추천 장소 ID
    Image image,                               // 메인 이미지
    String title                               // 제목
)
```

#### RecommendationBlockReadDto
```java
public class RecommendationBlockReadDto {
    private Long id;                           // 블록 ID
    private BlockType type;                    // 블록 타입 (TEXT/IMAGE)
    private String text;                       // 텍스트 내용
    private Image image;                       // 이미지 정보 (IMAGE 타입일 때)
    private String caption;                    // 이미지 캡션
    private Integer orderIndex;                // 블록 순서 인덱스
}
```

#### RecommendLikeDto
```java
public record RecommendLikeDto(
    Long userId,                               // 사용자 ID
    Long placeId                               // 추천 장소 ID
)
```

## 열거형(Enum) 타입

### RecommendationType
- `PLACE`: 장소 추천
- `POST`: 포스트 추천

### BlockType
- `TEXT`: 텍스트 블록
- `IMAGE`: 이미지 블록

### RecommendOrderType
- `CREATED_DESC`: 생성일 내림차순
- `CREATED_ASC`: 생성일 오름차순
- `LIKES_DESC`: 좋아요 수 내림차순
- `LIKES_ASC`: 좋아요 수 오름차순
- `VIEWS_DESC`: 조회수 내림차순
- `VIEWS_ASC`: 조회수 오름차순
- `REVIEWS_DESC`: 리뷰 수 내림차순
- `REVIEWS_ASC`: 리뷰 수 오름차순

## 공통 응답 코드

- `200`: 성공
- `400`: 잘못된 요청 형식
- `401`: 권한 인증 오류
- `500`: 내부 서버 오류

## 인증 요구사항

대부분의 API는 `@MemberEmailAspect`를 통한 이메일 기반 인증이 필요합니다. 추천 장소 생성 시에는 관리자 권한이 필요할 수 있습니다.

## 파일 업로드

이미지 파일은 `multipart/form-data` 형식으로 업로드되며, `MultipartFile` 타입으로 처리됩니다.

## 페이징

조회 API는 Spring Data의 `Pageable`을 사용하여 페이징을 지원합니다. 기본 페이지 크기는 5개입니다.

## 검색 기능

제목, 부제목, 태그를 대상으로 키워드 검색이 가능하며, 추천 타입별 필터링도 지원합니다.
