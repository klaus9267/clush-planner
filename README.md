# clush-planner

안녕하십니까 백엔드 지원자 김민호입니다.

# 설명

일정, 할 일(Todo)를 관리, 공유할 수 있는 어플리케이션입니다.

사용자 구분을 통한 일정, 할 일의 공유를 위해 User Module를 추가했으며 업무에 사용되는 상황을 가정하여 Team Module을 추가했습니다.

api의 안정성을 확보하기 위해 테스트코드를 작성했습니다

### Module

1. User
2. Team
3. Todo
4. Schedule

# 소스 빌드 및 실행 방법 메뉴얼

### 필요 조건

- Java 17 이상
- Gradle 7.x 이상

### 소드 코드 다운로드

- `git clone https://github.com/klaus9267/clush-planner.git`

### 빌드 방법

프로젝트 루트 디렉토리에서 다음 명령어를 실행하여 프로젝트를 빌드합니다.

- `./gradlew clean build`

### 실행 방법

 빌드된 JAR 파일을 실행합니다

- `java -jar build/libs/planner-0.0.1-SNAPSHOT.jar`

애플리케이션이 시작되면 기본적으로 [http://localhost:8080](http://localhost:8080/) 에서 접속할 수 있습니다.

### 데이터베이스 설정

이 프로젝트는 MySQL과 H2 데이터베이스를 모두 지원합니다.

- H2 MODE=MySQL 사용

H2 콘솔 접속 방법 

- 브라우저에서 http://localhost:8080/h2-console 접속
- url : `jdbc:h2:mem:local;MODE=MySQL;DATABASE_TO_LOWER=TRUE`
- 사용자명 : sa
- 비밀번호 : 없음

### API 문서

Swagger UI를 통해 API 문서를 확인할 수 있습니다:
http://localhost:8080/swagger-ui.html

### 테스트 실행

다음 명령어로 테스트를 실행할 수 있습니다:

`./gradlew test`

# 주력으로 사용한 컴포넌트

### Facade

파사드 패턴을 적용하기 위한 컴포넌트입니다. 사용자 정보를 가져오기 위해 UserService를 특정 Service에서 의존해야하는 경우가 생기는데 예상치 못한 순환 참조를 해결하기 위해 Controller, Service 사이에 위치해 있으며 각 Service들을 참조해 데이터를 조립하고 이후 Service로 넘어가 추가적인 로직을 전개합니다.

### Controller, Service, Repository

MVC패턴을 사용해 각 관심사 분리를 위해 각 클래스를 구분하여 구현했습니다.

Controller는 View와 Service 중간에 위치하여 요청을 받아들이고 이어주는 중간 다리 역할을 합니다.

Service는 실질적인 비즈니스 로직을 위한 컴포넌트입니다. 

Repository는 DB에 접근해 데이터를 입, 출력하기 위한 컴포넌트입니다.

### GlobalExceptionHandler

전역 예외처리를 위한 컴포넌트입니다. CustomException, ErrorCode를 구현하여 예외반환을 위한 추가적인 정보를 담았습니다.

### ErrorCode

예외처리에 사용되는 메세지들을 모아두는 Enum class입니다. Supplier를 implement 하여 throw 시 불필요한 람다의 사용을 줄였습니다.

### SecurityUtil

로그인 이후 API를 호출했을 때 현재 사용자를 특정 후 쉽게 정보를 조회하기 위한 컴포넌트입니다. JPA 영속성은 잃어버려 사용자의 정보를 수정하기 위해선 다시 DB에서 조회 후 수정해야합니다.

# API 명세서

Swagger를 사용하여 API 명세를 자동화했습니다.

http://localhost:8080/swagger-ui/index.html

# 추가적입 업무 API

1. **User Module**
    - 로그인 (Post /api/users/login)
    - 회원가입 (Post /api/users)
    - 내 정보 조회(Get /api/users)
    - 사용자 검색 (Get /api/search?userIds={id1,id2})
    - 내 정보 수정 (Patch /api/users/{name})
    - 회원탈퇴 (Delete /api/users)
2. **Team Module**
    - 팀 생성 (Post /api/teams/{name})
    - 팀 참가 (Post /api/teams/join/{teamId)
    - 팀 정보 조회 (Get /api/teams/{teamId})
    - 팀 정보 수정 (Patch /api/teams)
    - 팀 삭제 (Delete /api/teams/{id})
    - 팀 탈퇴 (Delete /api/teams/leave)
3. **Todo Module**
    - 팀 Todo 생성 (Post /api/todos/{teamId})
    - Todo 공유(사용자) (Post /api/todos/share)
    - Todo 공유(팀) (Post /api/todos/share/team)
4. **Schedule Module**
    - 팀 일정 생성 (Post /api/schedules/{teamId})
    - 일정 공유(사용자) (Post /api/schedules/share)
    - 일정 공유(팀) (Post /api/schedules/share/team)
  
# ERD
![planner](https://github.com/user-attachments/assets/efb0dd86-254d-4901-801e-39d1f47e4b96)


# 주요 사용 기술

- Spring Boot 3.3.3
- Spring Data JPA
- Spring Security
- MySQL / H2 Database
- Lombok
- JWT (JSON Web Token)
- QueryDSL
- Springdoc OpenAPI (Swagger)
