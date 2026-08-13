# 파일 인덱스

## 애플리케이션

- `src/main/java/com/example/myaicv/MyAiCvApplication.java`

## 설정

- `src/main/resources/application.yaml`
- `src/main/java/com/example/myaicv/config/AsyncConfig.java`
- `src/main/java/com/example/myaicv/config/SecurityConfig.java`

## Auth

- `auth/controller/AuthController.java`
- `auth/domain/AppUser.java`
- `auth/domain/OAuthProvider.java`
- `auth/domain/UserRole.java`
- `auth/dto/AuthUserResponse.java`
- `auth/repository/AppUserRepository.java`
- `auth/service/AuthCookieService.java`
- `auth/service/OAuthLoginSuccessHandler.java`
- `auth/service/OAuthUserProvisionService.java`

## MyInfo Public

- `myinfo/controller/MyInfoAiController.java`
- `myinfo/controller/MyInfoDocumentController.java`
- `myinfo/controller/MyInfoRecommendedQuestionController.java`
- `myinfo/controller/SiteVisitController.java`
- `myinfo/service/MyInfoAiService.java`
- `myinfo/service/MyInfoAskPostProcessService.java`
- `myinfo/service/MyInfoDocumentService.java`
- `myinfo/service/MyInfoSelectionParser.java`
- `myinfo/service/MyInfoRecommendedQuestionService.java`
- `myinfo/service/SiteVisitService.java`
- `myinfo/repository/MyInfoAskHistoryRepository.java`
- `myinfo/repository/SiteVisitRepository.java`
- `myinfo/persistence/StringListJsonConverter.java`
- `myinfo/dto/*`

## Seeker

- `seeker/controller/SeekerDashboardController.java`
- `seeker/controller/SeekerMyInfoAskHistoryController.java`
- `seeker/controller/SeekerMyInfoDocumentController.java`
- `seeker/controller/SeekerMyInfoRecommendedQuestionController.java`
- `seeker/service/SeekerMyInfoAskHistoryService.java`
- `seeker/service/SeekerMyInfoDocumentService.java`
- `seeker/service/SeekerMyInfoRecommendedQuestionService.java`
- `seeker/repository/MyInfoRecommendedQuestionRepository.java`
- `seeker/dto/*`

## Resume

- `resume/controller/ResumeApiController.java`
- `resume/controller/ResumePageController.java`

## Admin

- `admin/controller/AdminMenuController.java`

## Global

- `global/entity/MyInfoAskHistory.java`
- `global/entity/MyInfoRecommendedQuestion.java`
- `global/entity/SiteVisit.java`
- `global/error/*`
- `global/security/*`

## Integration

- `telegram/TelegramMessageService.java`

## Static Knowledge

- `src/main/resources/static/myinfo/checklist.md`
- `src/main/resources/static/myinfo/*.md`
- `src/main/resources/static/myinfo/subpofol/*`

## Frontend

- `src/main/resources/static/index.html`
- `src/main/resources/static/app.css`
- `src/main/resources/static/app.js`

프론트엔드 주요 기능:

- 공개 채팅 UI
- 진행 상태 표시
- 추천 질문 카드
- OAuth 로그인 진입
- 구직자 대시보드
- 질문 이력 관리
- 추천 질문 관리
- MyInfo Markdown 문서 관리
