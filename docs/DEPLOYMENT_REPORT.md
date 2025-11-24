## ✦  실제 인프라 구축

### 핵심 스크린샷
- 네트워크 구성 확인
    - [x]  VPC Resource Map (전체 구조가 한눈에 보이게끔)
        - VPC Recource Map
            
            <img width="6676" height="2768" alt="image" src="https://github.com/user-attachments/assets/b58f4ac9-f679-4cc1-90cd-dda6e9344aa4" />

    - [x]  Security Groups 설정 (3개의 SG의 Inbound rules)
        - Security Groups
            - Application Load Balancer SG
                
                <img width="6692" height="1928" alt="image" src="https://github.com/user-attachments/assets/e60b8445-f2e3-48ef-904b-06ae6fc8a1ca" />

            - ECS Gateway SG
                
                <img width="6680" height="1900" alt="image" src="https://github.com/user-attachments/assets/96b2f7e4-b19c-4eb9-b675-572c34114ac7" />

            - ECS Application SG
                
                <img width="6732" height="2020" alt="image" src="https://github.com/user-attachments/assets/6369ab4a-5ec7-43f4-a0ce-4b79c85ebe62" />

            - RDS SG
                
                <img width="6716" height="2148" alt="image" src="https://github.com/user-attachments/assets/156b8353-7327-4d88-adb8-4afcd861e9f3" />

            - Bastion SG
                
                <img width="6752" height="1840" alt="image" src="https://github.com/user-attachments/assets/ebedafa8-f44f-4dce-9be2-16e7b8230340" />

    - [x]  NAT Gateway Status “**Available**”
        - NAT Gateway
            
            <img width="4528" height="836" alt="image" src="https://github.com/user-attachments/assets/c45454d3-3474-46b2-86a4-43b7fdefd2bb" />

- 데이터베이스 구성 확인
    - [x]  RDS 인스턴스 상세 (Multi-AZ 확인)
        - RDS 인스턴스
            
            <img width="5920" height="3268" alt="image" src="https://github.com/user-attachments/assets/ffeeb67b-5aa1-4617-9d14-6d120efc9079" />

    - [x]  Bastion에서 MySQL 접속 성공 (쿼리 결과)
        - 쿼리 결과
          
            <img width="2048" height="804" alt="image" src="https://github.com/user-attachments/assets/f8b370a2-50a0-437a-8611-c1624eb303e1" />

            <img width="1840" height="456" alt="image" src="https://github.com/user-attachments/assets/c6d707dd-e2ff-4726-8f26-666685ad91a4" />

            
- 컨테이너 배포 확인
    - [x]  ECR 이미지 목록 (3개 서비스 latest)
        - 이미지 목록
            
            <img width="5680" height="4772" alt="image" src="https://github.com/user-attachments/assets/68dc43c8-13a0-4207-b086-e82cd5a15712" />

    - [x]  ECS Services 목록 (3개 모두 Active)
        - ECS 서비스 목록
            
            <img width="8296" height="1612" alt="image" src="https://github.com/user-attachments/assets/441e215e-d052-47d6-8ee7-2844a70aee9b" />

    - [x]  CloudWatch Logs (한 서비스 시작 로그)
        - Gateway 애플리케이션 실행 로그
            
            <img width="6204" height="2960" alt="image" src="https://github.com/user-attachments/assets/67080253-c93e-45ad-934a-c2148ca48e42" />

- 동작 검증
    - [x]  API 호출 결과 (주문 생성 성공)
        - 호출 결과
            
            <img width="4316" height="2320" alt="image" src="https://github.com/user-attachments/assets/1bef03f7-dce7-46fe-9fc3-240049039538" />

    - [ ]  이벤트 전파 확인 (3개의 서비스 로그)
    - [ ]  MySQL 데이터 확인 (3개 테이블 조회)
    - [ ]  전체 아키텍처 체크리스트 완성본

---
### 구축 가이드라인

**Phase 1: 네트워크 구성 (2-3시간)**
    
- [x]  VPC 생성 (CIDR, DNS 설정)
        
  10.0.0.0/16으로 생성
        
- [x]  Internet Gateway 생성 및 연결
- [x]  Subnet 6개 생성 (Public 2, Private 2, DB 2)
        
  public subnet, private subnet, db subnet 각각 2개씩 생성
        
  10.0.0.[0-5]/24 로 생성
        
- [x]  NAT Gateway 2개 생성 (각 AZ)
        
  A region, C region에 대한 각각의 NAT Gateway 생성
        
- [x]  Route Table 설정 (Public, Private, DB 각각)
  - public subnets(A, C) → internet gateway
  - private subnet(A) → public subnet NAT Gateway (A)
  - private subnet(C) → public subnet NAT Gateway (C)

- [x]  Security Group 4개 생성 (ALB, ECS, RDS, Bastion)
        
        
  | 종류 | 인바운드 | 아웃바운드 |
  | --- | --- | --- |
  | ALB(Application Load Balancer) | HTTP(80), HTTP(443) | 모든 트래픽 |
  | EC2(Elastic Compute Cloud) (Spring Cloud Gateway) | ALB SG | 모든 트래픽 |
  | ECS(Elastic Container Service) | EC2 SG | 모든 트래픽 |
  | RDS | ECS SG, Bastion SG | X |
  | Bastion | SSH(22) (내 아이피) | 모든 트래픽 |
    
**Phase 2: 데이터베이스 (1-2시간)**
    
- [x]  RDS Subnet Group 생성 (DB Subnet 2개 포함)
        
  db-subnet-a, db-subnet-c를 포함
        
- [x]  RDS MySQL 인스턴스 생성 (Multi-AZ 여부는 Mission 1 결정에 따라)
        
  PostgreSQL 사용, 다중 AZ 인스턴스 적용
        
  자격 증명 관리 : AWS Secrets Manager
        
  db.t3.small, 20GiB
        
- [x]  Bastion Host 생성 (Public Subnet에 배치)
        
  EC2 생성
        
  탄력적 IP 주소 할당
        
- [x]  Bastion에서 RDS 접속 테스트
        
  `ssh -i <key_file> <bastion_host_user>@<bastion_host_ip>`
        
  postgresql 설치 후 RDS 접속
        
- [x]  데이터베이스 스키마 생성 (3주차 과제 SQL 스크립트 활용)
        
  `user_service`, `project_service`, `payment_service` 스키마 생성
        
    
**Phase 3: 컨테이너 준비 (2-3시간)**
    
- [x]  Dockerfile 3개 작성 (각 서비스별)
- [x]  ECR 리포지토리 3개 생성
  - nowayback/encore/user
  - nowayback/encore/project
  - nowayback/encore/payment
- [x]  Docker 이미지 빌드 및 ECR
- [x]  푸시
- [x]  이미지 태그 전략 (latest, v1.0.0 등)
        
  latest
        
    
**Phase 4: ECS 배포 (3-4시간)**
    
- [x]  ECS Cluster 생성 (Fargate 타입)
  - nowayback-encore-cluster (Fargate 전용)
  
- [x]  Task Definition 3개 작성 (환경변수로 RDS 엔드포인트 설정)
  - user-task : 2vCPU + 4GB
            
    user-service 컨테이너 : user ECR 이미지 URI를 선택
            
    18081 TCP user-service-port HTTP
            
    로그 수집 (Amazon CloudWatch)
            
  - project-task : 2vCPU + 4GB
            
    project-service 컨테이너 : project ECR 이미지 URI를 선택
            
    18085 TCP project-service-port HTTP
 
    로그 수집 (Amazon CloudWatch)
            
  - payment-task : 2vCPU + 4GB
            
    payment-service 컨테이너 : payment ECR 이미지 URI를 선택
            
    18084 TCP payment-service-port HTTP
          
    로그 수집 (Amazon CloudWatch)
            
- [x]  ALB 생성 및 Target Group 3개 설정
        
  로드밸런서 이름 : nowayback-encore-alb
        
  nowayback - 8080 HTTP
        
  - 역할 생성
            
    로드밸런싱에 ecs 역할 추가
            
- [x]  Listener Rule 설정 (경로 기반 라우팅)
        
  우선순위 : 1, 80 HTTP
        
- [x]  ECS Service 3개 생성 (각 서비스당 Task 개수는 Mission 1 결정)
  - User ECS : user-service
      
    user-task 연결
            
    배포 옵션 : 블루/그린
            
    네트워킹 - VPC 연결
            
    서브넷 - private-subnet-a, private-subnet-c
            
    SG : ECS-Group-01
  - Project ECS
         
    위와 동일
  - Payment ECS
      
    위와 동일
- [x]  Health Check 검증
        
  상태확인 프로토콜 `/actuator/health`
        
    
**Phase 5: 통합 테스트 (1-2시간)**
    
- [x]  API 엔드포인트 테스트 (Postman 또는 curl)
- [ ]  이벤트 전파 확인 (CloudWatch Logs에서 확인)
- [x]  데이터베이스 데이터 확인 (Bastion 통해 접속)
- [x]  CloudWatch Logs 확인 (에러 없는지)
