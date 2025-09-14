# Nomadic

## 개요
이 프로젝트는 자주 사용하던 여러 여행 어플리케이션들을 보며 "이런 서비스들은 어떻게 구현되어 있을까?" 라는 궁금증에서 시작되었으며,
사용자의 여행을 도와주는 여러 기능을 구현했습니다.
\
\
경험을 통한 학습을 하는 것에 의의를 두었지만, 추가적인 기능과 여러 최적화를 통해 궁극적으로 나만의 여행 서비스를 제공하고자 합니다.

---

## 사용 기술 및 환경

### 1. Backend
  - Java : 21.0.2
  - Springboot : 3.1.5
  - MySQL : 8.0.33
  - Python : 3.10
  - FastAPI : 0.104.1

### 2. Frontend
  - TypeScript : 5.8.3
  - React Native : 0.81.4
  - React : 19.1.0
  - Expo : 54.0.2

---

## 주요 기능
- ✅ 숙소 조회
  - ES 기반 숙소 조회
  - 자동 완성, 오타 검증, 정렬 지원
- ✅ 추천 여행지
  - 관리자가 추천하는 여행지 제공
  - 사용자가 직접 작성한 여행기 제공
  - 여행지에 찜 및 댓글 작성 가능
- ✅ 계획 생성
  - 계획에 따른 지도 제공
  - 세부 계획들을 마커로 표시하여 동선 확인 가능
  - 계획을 공유하여 함께 조회하고 함께 수정 가능
  - 계획의 수정사항들을 기록하여, 누가 언제 어떻게 수정했는지 확인 가능

---

## 프로젝트 구조

<img width="1284" height="720" alt="image" src="https://github.com/user-attachments/assets/9ec2a4ca-c34d-4272-9393-886fd3387111" />  
<br/>
  

```
src/main/java/com/example/nomadic/
┣ batch/ # 배치 처리
┣ common/ # 공통 모듈 (auth, error, file, kafka, logging, template)
┣ configuration/ # 설정 클래스
┣ domain/ # 도메인 모듈 (accommodation, member, notification, plan, recommend, report, review)
┗ mysql/ # MySQL 관련
```
---

## 사용 예시
### 1. 초기 로드 및 홈 화면
<img width="486" height="600" alt="image" src="https://github.com/user-attachments/assets/237357d0-12b3-4672-ade1-c7770a106259" />

### 2. 숙소 검색 화면
<img width="486" height="600" alt="image" src="https://github.com/user-attachments/assets/c0928601-6dbf-4b72-888c-5bfe76fc1dd4" />

### 3. 추천 여행지 화면
<img width="486" height="600" alt="image" src="https://github.com/user-attachments/assets/0014e5ce-5b88-493e-8149-9c9806313424" />

### 4. 계획 화면
<img width="417" height="646" alt="image" src="https://github.com/user-attachments/assets/2d3d6f04-7689-416f-92fa-87751ac08b23" />



---
## 📖 기획서
https://trite-chips-629.notion.site/1d49bff393c780b69030eac14bfe2dad?pvs=74

