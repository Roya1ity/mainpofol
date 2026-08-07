# 파일 인덱스

## 애플리케이션

- `src/main/java/com/example/mainpofol/MainpofolApplication.java`

## 설정

- `src/main/resources/application.yaml`
- `src/main/java/com/example/mainpofol/config/AsyncConfig.java`
- `src/main/java/com/example/mainpofol/config/SecurityConfig.java`

## Admin

- `admin/controller/AdminAuthController.java`
- `admin/controller/AdminMyInfoAskHistoryController.java`
- `admin/controller/AdminMyInfoDocumentController.java`
- `admin/controller/AdminMyInfoRecommendedQuestionController.java`
- `admin/service/AdminAuthService.java`
- `admin/service/AdminMyInfoAskHistoryService.java`
- `admin/service/AdminMyInfoDocumentService.java`
- `admin/service/AdminMyInfoRecommendedQuestionService.java`
- `admin/repository/MyInfoRecommendedQuestionRepository.java`
- `admin/dto/*`

## MyInfo

- `myinfo/controller/MyInfoAiController.java`
- `myinfo/controller/MyInfoRecommendedQuestionController.java`
- `myinfo/service/MyInfoAiService.java`
- `myinfo/service/MyInfoAskPostProcessService.java`
- `myinfo/service/MyInfoDocumentService.java`
- `myinfo/service/MyInfoSelectionParser.java`
- `myinfo/service/MyInfoRecommendedQuestionService.java`
- `myinfo/repository/MyInfoAskHistoryRepository.java`
- `myinfo/persistence/StringListJsonConverter.java`
- `myinfo/dto/*`

## Global

- `global/entity/MyInfoAskHistory.java`
- `global/entity/MyInfoRecommendedQuestion.java`
- `global/error/*`
- `global/security/*`

## Integration

- `telegram/TelegramMessageService.java`

## Static Knowledge

- `src/main/resources/static/myinfo/checklist.md`
- `src/main/resources/static/myinfo/*.md`

## Frontend

- `src/main/resources/static/index.html`
- `src/main/resources/static/app.css`
- `src/main/resources/static/app.js`
- `src/main/resources/static/favicon.svg`
- `src/main/resources/static/favicon.ico`
- `src/main/resources/static/favicon.png`

프론트 기능:

- 공개 채팅 UI
- 추천 질문 카드 슬라이더
- 관리자 로그인 모달
- 관리자 대시보드
- 일자별/시간대별 방문자수 그래프
- 질문 히스토리 관리
- 추천 질문 DB 관리
- MyInfo Markdown 문서 관리
