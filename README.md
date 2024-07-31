# 선착순 구매 MSA 기반 프로젝트
이 프로젝트는 마이크로서비스 아키텍처(MSA)를 기반으로 하여 선착순 옷 구매를 구현하였습니다. 
사용자들이 특정 상품을 선착순으로 구매할 수 있는 기능을 제공하며, 
높은 트래픽을 효과적으로 처리하기 위해 다양한 기술 스택과 아키텍처 패턴을 적용하였습니다.

### 기술 스택
---
- **프로그래밍 언어**: Java
- **프레임워크**: Spring Boot
- **데이터베이스**: Postgresql, Redis
- **메시지 브로커**: Kafka
- **API 관리**: Spring Cloud Netflix Eureka
- **인증 및 인가**: JWT, Spring Security
- **부하 테스트**: JMeter

### 아키텍처
---
- **API Gateway**: 모든 클라이언트 요청을 수신하고 각 마이크로서비스로 라우팅하는 역할 + 사용자 인증 및 권한 부여
- **멤버 서비스**: 가입, 로그인, 리프레시, 로그아웃 등 사용자 관리
- **상품 서비스**: 상품 정보 관리 및 제공
- **주문 서비스**: 주문 생성 및 관리
- **재고 서비스**: 실시간 재고 관리 및 업데이트
- **결제 서비스**: 결제 처리 및 검증
- **이벤트 스트리밍**: Kafka를 사용한 비동기 이벤트 처리 및 통합
- **데이터베이스**: MySQL을 주요 데이터 저장소로 사용하며, Redis를 캐싱 및 세션 관리를 위해 사용

### API 명세서
---
#### 사용자 관리
- **POST /member/login**
  - 사용자 로그인
- **POST /member/join**
  - 회원가입
- **POST /member/email/send**
  - 인증번호 메일 보내기
- **POST /member/email/verify**
  - 인증번호 확인하기
- **GET /member/refresh**
  - 토큰 리프레시
- **GET /member/mypage**
  - 내 정보 확인
- **PUT /member/mypage**
  - 내 정보 수정
- **PATCH /member/password**
  - 비밀번호 수정
- **DELETE /member/logout**
  - 해당 기기에서 로그아웃
- **DELETE /member/logout/all**
  - 전체 기기에서 로그아웃

#### 상품 관리
- **GET /products**
    - 설명: 모든 상품 조회
- **GET /products/{id}**
    - 설명: 특정 상품 상세 조회

#### 주문 관리
- **POST /orders**
    - 설명: 새 주문 생성
    - 요청 본문: `{ "productId": 1, "quantity": 2, "paymentMethod": "credit_card" }`
    - 응답: `{ "orderId": 12345, "status": "confirmed" }`

#### 결제 관리

### 주요 기능
---
- **JWT 인증**: JWT를 사용하여 사용자 인증 및 권한 부여
- **실시간 재고 관리**: Redis를 사용하여 실시간으로 재고 상태를 관리하고 업데이트
- **비동기 이벤트 처리**: Kafka를 사용하여 주문, 결제 등 이벤트를 비동기적으로 처리, 보상 트랜잭션 적용


### 트러블 슈팅
---
- **문제**: Kafka 서버 장애 시 데이터베이스의 원자성 보장 문제
    - **해결 방안**: Outbox 패턴을 적용하여 Kafka와 데이터베이스 간의 트랜잭션 보장
- **문제**: 주문 생성 시 재고 부족 문제 발생
    - **해결 방안**: 재고 서비스에서 분산 락을 사용하여 동시성 이슈 해결