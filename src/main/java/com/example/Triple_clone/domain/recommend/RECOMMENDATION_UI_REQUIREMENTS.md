# 추천 페이지 UI 요구사항 정리

## 📋 개요
추천 페이지는 **추천 여행지**와 **여행기** 두 가지 콘텐츠를 제공하는 통합 페이지입니다.
**블록 기반 구조**로 텍스트와 이미지가 순서대로 배치되어 풍부한 콘텐츠를 제공합니다.

## 🎯 주요 기능

### 1. 탭 네비게이션
- **추천 여행지** 탭: 관리자가 추천하는 여행지 목록 (type: PLACE)
- **여행기** 탭: 사용자들이 작성한 여행 후기/가이드 (type: POST)

### 2. 검색 기능
- 실시간 검색 (타이핑 시 즉시 필터링)
- 검색 대상: 제목, 부제목, 태그, 지역명
- 타입별 필터링 (PLACE/POST)
- 검색어 초기화 버튼

### 3. 새로고침 기능
- Pull-to-refresh 지원
- 여행지 탭: 랜덤 10개 재생성
- 여행기 탭: 최신 순으로 재정렬

### 4. 정렬 기능
- 최신순 (createdAt DESC)
- 인기순 (likesCount DESC) 
- 조회순 (viewsCount DESC)
- 리뷰순 (reviewsCount DESC)

## 🏗️ 데이터 구조 요구사항

### Recommendation Domain (통합)
```java
@Entity
public class Recommendation {
    private Long id;
    private String title;                    // 여행지명 또는 여행기 제목
    private String subTitle;                 // 부제목 (지역명 등)
    private Location location;               // 위치 정보 (address, latitude, longitude)
    private String price;                    // 가격 정보
    private RecommendationType type;         // PLACE 또는 POST 구분
    private PostMeta postMeta;               // 게시물 메타데이터 (author, source)
    private List<String> tags;               // 태그 목록
    private List<RecommendationBlock> blocks; // 블록 기반 콘텐츠 (순서 보장)
    private LocalDateTime createdAt;         // 생성일
    private LocalDateTime updatedAt;         // 수정일
    private int likesCount;                  // 좋아요 수
    private int reviewsCount;                // 리뷰 수
    private int viewsCount;                  // 조회수
}

@Entity
public class RecommendationBlock {
    private Long id;
    private BlockType type;                  // TEXT 또는 IMAGE
    private String text;                     // 텍스트 내용
    private Image image;                     // 이미지 (type이 IMAGE일 때)
    private String caption;                  // 이미지 캡션
    private int orderIndex;                  // 순서 인덱스 (정렬용)
    private Recommendation recommendation;   // 소속 추천 장소
}

public enum RecommendationType {
    PLACE,  // 추천 여행지
    POST    // 여행기
}

public enum BlockType {
    TEXT,   // 텍스트 블록
    IMAGE   // 이미지 블록
}
```

## 🎨 UI 컴포넌트 요구사항

### 1. 추천 여행지 카드 (type: PLACE)
- **제목**: 여행지명 (1줄 제한)
- **부제목**: 지역명 (1줄 제한)
- **위치**: 주소 정보
- **가격**: 우측 상단 표시
- **좋아요**: 하트 아이콘 + 좋아요 수
- **태그**: 하단에 태그 목록 (최대 3개)
- **생성일**: 상대 시간 표시 (예: "3일 전")

### 2. 여행기 카드 (type: POST)
- **제목**: 여행기 제목 (2줄 제한)
- **작성자**: postMeta.author 정보
- **지역**: location.address
- **생성일**: createdAt 상대 시간
- **통계**: 조회수, 좋아요 수, 리뷰 수
- **태그**: 하단에 태그 목록 (최대 3개)

### 3. 상세 페이지 (RecommendDetail)
- **헤더**: 제목, 부제목, 위치, 가격
- **블록 콘텐츠**: orderIndex 순서대로 렌더링
  - TEXT 블록: 텍스트 내용 표시
  - IMAGE 블록: 이미지 + 캡션 표시
- **좋아요 버튼**: 토글 기능 (즉시 반영, 5초 후 서버 동기화)
- **태그 목록**: 전체 태그 표시
- **메타 정보**: 작성자, 생성일, 조회수 등

## 🔄 비즈니스 로직

### 1. 데이터 표시 방식
- **랜덤 표시**: `GET /recommendations/random?type={PLACE|POST}&limit=10`
- **검색 결과**: `GET /recommendations/search?q={keyword}&type={PLACE|POST}`
- **TOP 10**: `GET /recommendations/top10` (홈페이지용)
- **정렬 조회**: `GET /recommendations?order={createdAt|likesCount|viewsCount|reviewsCount}&direction={ASC|DESC}`

### 2. 필터링 로직
- **검색 대상**: `title`, `subTitle`, `tags`에서 키워드 검색
- **타입 필터**: `type` 파라미터로 PLACE/POST 구분
- **정렬 옵션**: createdAt, likesCount, viewsCount, reviewsCount

### 3. 상태 관리
- **탭 상태**: 현재 활성 탭 (PLACE/POST)
- **검색 상태**: 검색어와 필터링된 결과
- **정렬 상태**: 현재 정렬 기준
- **로딩 상태**: 새로고침 중 표시
- **좋아요 상태**: 메모리 기반 즉시 반영, 5초 후 서버 동기화

### 4. 좋아요 처리 로직
- **즉시 반영**: UI에서 바로 좋아요 상태 변경
- **배치 처리**: 5초마다 서버에 일괄 동기화
- **중복 방지**: 동일 사용자의 중복 요청 무시
- **에러 처리**: 서버 동기화 실패 시 롤백

## 🚫 제거된 불필요한 필드들
- `isFeatured` - 랜덤/검색/TOP10으로 대체
- `isRecommended` - 랜덤/검색/TOP10으로 대체
- `notionUrl` - UI에서 사용하지 않음
- `content` - 블록 기반 구조로 대체
- `mainImage` - 블록 내 이미지로 대체

## 📱 네비게이션
- **추천 여행지 클릭**: `RecommendDetail` 화면으로 이동 (type: 'PLACE')
- **여행기 클릭**: `RecommendDetail` 화면으로 이동 (type: 'POST')
- **상세 페이지**: 블록 순서대로 콘텐츠 표시

## 🎯 API 엔드포인트 요구사항

### 사용자용 API
- `GET /recommendations` - 전체 조회 (정렬 옵션 포함)
- `GET /recommendation?id={id}` - 단일 조회 (조회수 증가)
- `GET /recommendations/top10` - 홈페이지용 TOP 10
- `GET /recommendations/search?q={keyword}&type={PLACE|POST}` - 검색
- `GET /recommendations/random?type={PLACE|POST}&limit={number}` - 랜덤 목록
- `GET /recommendation/{id}/blocks` - 블록 조회 (순서 보장)
- `PUT /recommendation/like` - 좋아요 토글

### 관리자용 API (Form Data 방식)
- `POST /admin/recommend` - 추천 장소 생성 (multipart/form-data)
- `PATCH /admin/recommend` - 추천 장소 수정 (application/json)
- `DELETE /admin/recommend?placeId={id}` - 추천 장소 삭제
- `POST /admin/recommend/{id}/blocks` - 블록 추가 (multipart/form-data)
- `PATCH /admin/recommend/blocks/{blockId}` - 블록 수정 (multipart/form-data)
- `DELETE /admin/recommend/blocks/{blockId}` - 블록 삭제

## 📝 참고사항
- **Form Data 방식**: 모든 이미지 업로드는 multipart/form-data 사용
- **블록 순서**: `orderIndex` 기준으로 정렬하여 표시
- **텍스트 제한**: 카드에서는 1-2줄 제한, 상세에서는 전체 표시
- **이미지 처리**: 로딩 실패 시 기본 이미지 표시
- **좋아요 UX**: 즉시 반영으로 빠른 응답성 제공
- **검색 결과**: 빈 결과 시 "검색 결과가 없습니다" 메시지
- **Pull-to-refresh**: 새로고침으로 최신 데이터 갱신
- **상대 시간**: "3일 전", "1주 전" 등으로 표시
- **태그 표시**: 카드에서는 최대 3개, 상세에서는 전체 표시
- **단계별 생성**: 추천 장소 생성 → 블록 추가 순서로 진행
- **JSON 필드**: location, postMeta, tags는 JSON 문자열로 전송

## 🛠️ 프론트엔드 구현 가이드

### 1. 상태 관리 구조
```typescript
interface RecommendationState {
  // 탭 상태
  activeTab: 'PLACE' | 'POST';
  
  // 데이터 상태
  recommendations: Recommendation[];
  currentRecommendation: Recommendation | null;
  blocks: RecommendationBlock[];
  
  // 검색/필터 상태
  searchKeyword: string;
  selectedType: RecommendationType | null;
  sortOrder: 'createdAt' | 'likesCount' | 'viewsCount' | 'reviewsCount';
  sortDirection: 'ASC' | 'DESC';
  
  // UI 상태
  isLoading: boolean;
  isRefreshing: boolean;
  
  // 좋아요 상태 (메모리 기반)
  likeStates: Map<number, boolean>; // recommendationId -> isLiked
}
```

### 2. API 호출 예시
```typescript
// 추천 장소 목록 조회
const fetchRecommendations = async (params: {
  order?: string;
  direction?: 'ASC' | 'DESC';
  type?: 'PLACE' | 'POST';
}) => {
  const response = await fetch(`/recommendations?${new URLSearchParams(params)}`);
  return response.json();
};

// 검색
const searchRecommendations = async (keyword: string, type?: 'PLACE' | 'POST') => {
  const response = await fetch(`/recommendations/search?q=${keyword}&type=${type}`);
  return response.json();
};

// 랜덤 조회
const getRandomRecommendations = async (type: 'PLACE' | 'POST', limit: number = 10) => {
  const response = await fetch(`/recommendations/random?type=${type}&limit=${limit}`);
  return response.json();
};

// 좋아요 토글
const toggleLike = async (recommendationId: number, userId: number) => {
  const response = await fetch('/recommendation/like', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ recommendationId, userId })
  });
  return response.json();
};

// 추천 장소 생성 (Form Data)
const createRecommendation = async (data: {
  title: string;
  subTitle?: string;
  location: any;
  price?: string;
  type: 'PLACE' | 'POST';
  postMeta?: any;
  tags?: string[];
  mainImage?: File;
}) => {
  const formData = new FormData();
  formData.append('title', data.title);
  if (data.subTitle) formData.append('subTitle', data.subTitle);
  formData.append('location', JSON.stringify(data.location));
  if (data.price) formData.append('price', data.price);
  formData.append('type', data.type);
  if (data.postMeta) formData.append('postMeta', JSON.stringify(data.postMeta));
  if (data.tags) formData.append('tags', data.tags.join(','));
  if (data.mainImage) formData.append('mainImage', data.mainImage);

  const response = await fetch('/admin/recommend', {
    method: 'POST',
    body: formData
  });
  return response.json();
};

// 블록 추가 (Form Data)
const addBlock = async (recommendationId: number, data: {
  type: 'TEXT' | 'IMAGE';
  text?: string;
  imageFile?: File;
  caption?: string;
  orderIndex: number;
}) => {
  const formData = new FormData();
  formData.append('type', data.type);
  if (data.text) formData.append('text', data.text);
  if (data.imageFile) formData.append('imageFile', data.imageFile);
  if (data.caption) formData.append('caption', data.caption);
  formData.append('orderIndex', data.orderIndex.toString()); // 문자열로 전송

  const response = await fetch(`/admin/recommend/${recommendationId}/blocks`, {
    method: 'POST',
    body: formData
  });
  return response.json();
};
```

### 3. 블록 렌더링 로직
```typescript
const renderBlocks = (blocks: RecommendationBlock[]) => {
  return blocks
    .sort((a, b) => a.orderIndex - b.orderIndex)
    .map((block, index) => {
      if (block.type === 'TEXT') {
        return (
          <TextBlock key={block.id} text={block.text} />
        );
      } else if (block.type === 'IMAGE') {
        return (
          <ImageBlock 
            key={block.id} 
            image={block.image} 
            caption={block.caption} 
          />
        );
      }
    });
};
```

### 4. 좋아요 처리 로직
```typescript
const handleLikeToggle = (recommendationId: number, userId: number) => {
  // 1. 즉시 UI 반영
  const currentState = likeStates.get(recommendationId) || false;
  likeStates.set(recommendationId, !currentState);
  
  // 2. 서버 요청 (배치 처리용)
  toggleLike(recommendationId, userId);
  
  // 3. 5초 후 실제 상태 확인 (선택사항)
  setTimeout(() => {
    // 서버 상태와 동기화 확인
  }, 5000);
};
```

### 5. 검색 디바운싱
```typescript
const useDebounce = (value: string, delay: number) => {
  const [debouncedValue, setDebouncedValue] = useState(value);
  
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);
    
    return () => clearTimeout(handler);
  }, [value, delay]);
  
  return debouncedValue;
};

// 사용 예시
const debouncedSearchKeyword = useDebounce(searchKeyword, 300);
```

### 6. 에러 처리
```typescript
const handleApiError = (error: Error) => {
  if (error.message.includes('404')) {
    showMessage('추천 장소를 찾을 수 없습니다.');
  } else if (error.message.includes('500')) {
    showMessage('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
  } else {
    showMessage('알 수 없는 오류가 발생했습니다.');
  }
};
```

### 7. 실제 사용 예시
```typescript
// 추천 장소 생성 및 블록 추가 예시
const createRecommendationWithBlocks = async () => {
  try {
    // 1단계: 추천 장소 생성
    const recommendation = await createRecommendation({
      title: '제주도 한라산',
      subTitle: '한국의 최고봉',
      location: {
        address: '제주특별자치도 제주시 해안동',
        latitude: 33.3617,
        longitude: 126.5292
      },
      price: '무료',
      type: 'PLACE',
      postMeta: {
        author: '관리자',
        source: '공식'
      },
      tags: ['산', '등산', '자연', '제주도'],
      mainImage: mainImageFile
    });

    // 2단계: 텍스트 블록 추가
    await addBlock(recommendation.id, {
      type: 'TEXT',
      text: '한라산은 제주도의 상징이자 한국의 최고봉입니다.',
      orderIndex: 0
    });

    // 3단계: 이미지 블록 추가
    await addBlock(recommendation.id, {
      type: 'IMAGE',
      text: '한라산 전경',
      imageFile: blockImageFile,
      caption: '아름다운 한라산 전경',
      orderIndex: 1
    });

    console.log('추천 장소 생성 완료:', recommendation);
  } catch (error) {
    handleApiError(error);
  }
};
```

### 8. Postman 테스트 가이드

#### 추천 장소 생성
```
Method: POST
URL: http://localhost:8080/admin/recommend
Body: form-data

필드:
- title: 제주도 한라산
- subTitle: 한국의 최고봉
- location: {"address":"제주특별자치도 제주시 해안동","latitude":33.3617,"longitude":126.5292}
- price: 무료
- type: PLACE
- postMeta: {"author":"관리자","source":"공식"}
- tags: 산,등산,자연,제주도
- mainImage: [파일 선택]
```

#### 블록 추가
```
Method: POST
URL: http://localhost:8080/admin/recommend/{recommendationId}/blocks
Body: form-data

텍스트 블록:
- type: TEXT
- text: 한라산은 제주도의 상징이자 한국의 최고봉입니다.
- orderIndex: 0

이미지 블록:
- type: IMAGE
- text: 한라산 전경
- imageFile: [파일 선택]
- caption: 아름다운 한라산 전경
- orderIndex: 1
```
