# 놀러오세요 토이프로젝트 🎁 [backend]

## 배포 URL
- **https://nolto.kro.kr**

## 구현할 기능 목록
- Qs1. 마스터 권한이 생긴다면, 마스터가 사용 할 API를 따로 만들어 두는것이 좋은것인가?
- Qs2. 아니면 마스터라는 검증을 하고 모든 역할을 수행할 수 있도록 기존 API를 변경하는 것이 좋은 것인가?

- 나의 구상
    - 우선 yml에 Master의 id, password, jwt_master_payload를 분리하여 가지게 하자. 
    - argumentResolver에서 페이로드가 jwt_master_payload 맞는지를 검사하고, User를 상속한 Master를 만들어 이를 컨트롤러에 넘겨주자 
    - 마스터 유저라면 CRUD 모든 권한을 오픈해준다는 로직을 기존 API에 작성해주자. 

- [ ] login/admin 으로 마스터 jwt 토큰을 발급해주기. 
- [ ] 만약 해당 토큰이 masterPayload가 있다면 masterUser를 아규먼트 리졸버에서 반환해주기
    - 이거 패키지 어디로 두는게 좋을까?
- [ ] masterUser를 반환받았다면 이게 수정/삭제/조회 싹다 가능
	- `@MasterAuthenticationPrincipal` 만들어야할듯

- 전반적으로 기존 비즈니스 로직 코드에 영향을 안줬으면 하는 바람
    - admin 패키지를 삭제해도 굉장히 영향이 적게 갔으면 하는 바람
    - 로직을 짜긴해야하니 Service 단에서 몇가지 로직이 추가는 되겠지만,,, 쉽게 걷어낼수 있도록

## 필요한 API
- [x] 어드민 로그인 /admin/login

- [x] 피드 전체 조회 /admin/feeds
- [ ] 피드 단일 수정 /admin/feeds/:id
- [x] 피드 단일 삭제 /admin/feeds/:id

- [ ] 댓글 전체 조회 /admin/comments
- [ ] 댓글 단일 삭제 /admin/comments/:id

- [ ] 회원 전체 조회 /admin/users
- [ ] 회원 단일 삭제 /admin/users/:id

## 설계
- admin 이라는 패키지를 따로 빼자
- adminArgumentResolver 만들어서 AdminUser 권한 꽂아주기
- adminUser가 아니라면 뻑내는 로직 필요
