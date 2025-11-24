## ✦  요구사항 분석 및 아키텍처 설계

### ✧ 자동 복구
- 장애 시나리오 표
    
    
    | 장애 종류 | 발생 원인 | 자동 복구 가능 여부 | 복구 방법 | 예상 복구 시간 | 활용 AWS 기능 |
    | --- | --- | --- | --- | --- | --- |
    | App 크래시 | OutOfMemoryError | 🟢 가능 | Task 자동 재시작 | 30초 | ECS Health Check |
    | 물리 서버 문제 | Fargate 물리 호스트에 하드웨어 문제 | 🟢 가능 | AWS의 비정상 호스트 감지 → 자동으로 새 호스트에서 Task 재배치, ECS의 DesiredCount 유지 | 60초 | CloudWatch Container Insights, ECS Health Check, ECS 자동 복구 기능 (desiredCount) |
    | AZ 장애 | 특정 AZ 다운 | 🟢 가능 | 다른 AZ로 Task 재배포 | 1-2분 | Multi-AZ 배포, ALB |
    |  | Primary DB 다운 | 🟢 가능 | Standby로 자동 페일오버 | 1-2분 | RDS Multi-AZ |
    | 네트워크 장애 | NAT Gateway 장애 | 🟢 가능 | 다른 AZ의 NAT Gateway 사용 | 1분 | Multi-AZ NAT Gateway |
    | DB 장애 | 디스크 풀 | ❌ 불가능 | 데이터 정리 또는 스토리지 확장 | 5-15분 | RDS Storage Autoscaling, CloudWatch Alarm |
    | 애플리케이션 버그 | 애플리케이션 버그 | ❌ 불가능 | 이전 버전 롤백 또는 핫픽스 배포 | 10-30분 | ECS Deployment Circuit Breaker |
    | 리전 장애 | AWS 리전 전체 다운 | ❌ 불가능 | DR 리전으로 수동 페일오버 | 30분-2시간 | Route53 Failover, Cross-Region Replica |    

---

### ✧  Auto Scaling
- 전략 제시

    - 현재 요구사항에 가장 적합한 전략은 Scheduled Scaling과 반응적 Auto Scaling을 조합하는 것이다.
    - 블랙프라이데이처럼 트래픽 급증 시점을 미리 알고 있는 경우, 당일 오전 8시에 Scheduled Scaling으로 Task를 최대 개수로 미리 증설해두고, 예상보다 트래픽이 더 많을 경우를 대비해 반응적 Auto Scaling을 보험으로 설정한다.
- 지금 당장의 오토 스케일링 적용이 힘든 이유

    - 데이터 기반으로 만들어야 하는데, 72시간 안에 성급하게 추측하고 설정할 경우 임계값이 틀려서 불필요한 증설로 비용이 낭비되거나, 임계값이 너무 높아서 대응이 늦어 SLA를 위반할 수 있다.
    - 실제 트래픽 패턴을 모르고 추측으로 설정할 경우 실패 확률이 높다.

---

### ✧  네트워크 격리와 심층 방어
    
- 네트워크 다이어그램
    <img width="7520" height="3780" alt="image" src="https://github.com/user-attachments/assets/bcd82315-1d12-4653-af46-d894f9efed8c" />
    
- Defense in Depth(심층 방어) 전략 설명
    
    한 게층의 방어가 뚫려도 다음 계층이 보호할 수 있도록 여러 보안 체계를 준비
    
    - Security Group 규칙 표
        
        
        | SG명 | 방향 | 프로토콜 | 포트 | Source / Destination | 용도 |
        | --- | --- | --- | --- | --- | --- |
        | ALB-SG | Inbound | TCP | 80, 443 | 0.0.0.0/0 | 인터넷에서 HTTP/HTTPS |
        | ALB-SG | Outbound | 전체 | 전체 | ECS-GW-SG |  |
        | ECS-GW-SG | Inbound | TCP | 8080 | ALB-SG | ALB에서만 접근 |
        | ECS-GW-SG | Outbound | TCP | 전체 | ECS-SG | NAT |
        | ECS-SG | Inbound | TCP | 전체 | ECS-GW-SG | Applicationgateway 에서만 접근 |
        | ECS-SG | Outbound | TCP | 8080 | ECS-GW-SG | Applicationgateway 를 통해서 통신 |
        | RDS-SG | Inbound | TCP | 5432 | ECS-SG, Bastion-SG | ECS와 Bastion 에서만 접근 |
        | RDS-SG | Outbound | - | - | - | - |
        | Bastion-SG | Inbound | TCP | 22 | 개발자 IP | 개발자 IP에서만 SSH 접속 |
        | Bastion-SG | Outbound | TCP | 5432 | RDS-SG | - |

---

### ✧  예산 분배
- AWS Pricing Calculator 계산 과정
    - 스크린샷 및 계산 근거
        - ECS Fargate 비용
      
            <img width="603" height="123" alt="image" src="https://github.com/user-attachments/assets/6f5441df-d796-4724-8481-47fb459fde8b" />
            
            - vCPU 비용 : 6 tasks * 0.50vCPU * 730 hours * 0.04656 USD per hour = $101.97
            - 메모리 비용 : 6 tasks * 1.00GB * 730 hours * 0.00511 USD per GB per hour = $22.38
            - 총 Fargate 비용 : $124.35 (환율 1,300원 기준 ₩161,655)
        - RDS MySQL 비용
            - Single-AZ
          
                <img width="513" height="56" alt="image" src="https://github.com/user-attachments/assets/16d6eee8-8bdb-4721-93c5-cfec2e96b9c2" />

                <img width="481" height="59" alt="image" src="https://github.com/user-attachments/assets/185c67f9-3d91-414b-b64c-1db4b4e6d761" />

                
                - RDS MySQL 비용 : 1 instance * 0.026 USD hourly * 730 hours = $18.98
                - 스토리지 비용 : 20GB * 0.131 USD * 1 instance = $2.62
                - 총 RDS 비용 : $21.60 (환율 1,300원 기준 ₩28,080)
            - Multi-AZ
                
                <img width="511" height="60" alt="image" src="https://github.com/user-attachments/assets/cc70e069-9a8e-47c4-bfbc-dac3f60f06f3" />

                <img width="430" height="61" alt="image" src="https://github.com/user-attachments/assets/4ba2d4af-4bba-4abc-96ca-3cd0e2027671" />

                - RDS MySQL 비용 : 1 instance * 0.052 USD hourly * 730 hours = $37.96
                - 스토리지 비용 : 20GB * 0.262 USD * 1 instance = $5.24
                - 총 RDS 비용 : $43.20 (환율 1,300원 기준 ₩56,160)
        - NAT Gateway 비용
            
            <img width="660" height="54" alt="image" src="https://github.com/user-attachments/assets/093a678d-3f01-4ab4-bd0c-45c63d4755f5" />

            - 고정 비용 : 2 NAT Gateway * 730 hours * 0.059 USD per hour = $86.14
            
            <img width="546" height="38" alt="image" src="https://github.com/user-attachments/assets/c94bc905-3d79-442e-ab99-e81e68996c60" />
            
            - 데이터 전송 비용: 2 NAT Gateway * 0.059 USD per GB × 30GB = $3.54

            <img width="684" height="131" alt="image" src="https://github.com/user-attachments/assets/3bb97582-b5b9-4b8d-b545-e62b7eb62e9a" />

            - 총 NAT Gateway 비용 : $89.68 (환율 1,300원 기준 ₩116,584)
        - CloudWatch 비용
            
            <img width="749" height="255" alt="image" src="https://github.com/user-attachments/assets/82c20bd9-cd8c-49c1-91c6-5da36fbca6fe" />

            - CloudWatch Logs 비용 : 10GB * 0.76 USD per GB = $7.60
            - 스토리지 비용 : 10GB * 0.15 Storage compression factor * 1 Logs retention factor * 0.0314 USD = $0.0471
            - 총 CloudWatch 비용 : $7.65 (환율 1,300원 기준 ₩9,945)
        - ECR 비용
            
            <img width="416" height="62" alt="image" src="https://github.com/user-attachments/assets/090eaab6-e85a-4024-ad3a-dd62bae0c73e" />

            - ECR 비용 : 2GB * 0.10 USD per GB = $0.20
            - 총 ECR 비용 : $0.20 (환율 1,300원 기준 ₩260)
        - EC2(Bastion) 비용
            
            <img width="610" height="69" alt="image" src="https://github.com/user-attachments/assets/e04eb87a-74f3-492e-a43b-0490392f44b3" />

            
            - EC2 비용 : 1 instance * 0.0144 USD hourly * 730 hours = $10.512
            - 총 EC2 비용 : $10.51 (환율 1,300원 기준 ₩13,663)
    
    <img width="8800" height="2260" alt="image" src="https://github.com/user-attachments/assets/2470ad60-70e3-4c42-878f-86d6fdc33e0a" />

- 최종 제안
    
    현재 책정된 총 금액은 **358,300원**이며, 실행 가능한 전략을 적용했을 때의 최소 비용은 **260,200원** (NAT Instance 전환, Bastion 스펙 축소)
    
    월 150,000원의 제한에 맞추기 위해서는 ECS Fargate의 각 스펙을 축소하고 NAT Gateway의 수를 하나로 줄여야 함
    
    Fargate의 스펙이 감소함에 따라, API 응답 지연이 발생할 수 있으며 이로 인해 응답시간 95 백분위수 1초 초과될 시 **매달 25만원의 위약금**이 발생
    
    11만원의 추가 투자로 25만원의 위약금에 대한 리스크를 제거할 수 있음
    
    초기에는 낮은 트래픽이 예상되므로, RDS의 Single-AZ 적용과 ECS 스펙 감소로 **초기 비용을 감축**할 수 있음 (165,800원)
    
    데이터베이스의 장애와 서비스 응답 지연 시간에 대한 모니터링을 강화함
    
    고객사가 늘어남에 따라 각 ECS에 대한 스펙을 증가시키고, 데이터베이스의 Multi-AZ를 적용하여 고가용성 보장 (260,200원)
    
    추후에는 Auto Scaling 적용을 통해 안전한 서비스가 가능하도록 개선

---

### ✧  SLA 준수

- 응답 시간 경로별 예상 지연 시간 표
    
    | 경로 단계 | 예상 지연 | 최적화 가능 여부 | 최적화 방법 |
    | --- | --- | --- | --- |
    | 사용자 → AWS | 10-50ms | ❌ 불가 | - |
    | ALB → ECS Task | 5-10ms | ❌ 불가 | - |
    | ECS Task(Spring Boot) | 10-100ms | 🟢 가능 | 불필요한 로직 제거 |
    | DB 쿼리 | 100-200ms | 🟢 가능 | 인덱스 적용, 쿼리 최적화 |
    | ECS Task 응답 생성 | 5-20ms | ⚠️ 제한적 | JSON 직렬화 최적화 |
    | AWS → 사용자 | 10-50ms | ❌ 불가 | - |
    | ⌛️ 총합 | **140-430ms** | - | - |
- 선택한 Task 스펙과 근거
    - 0.5vCPU + 1GB
        
        주어진 예산에서 가장 합당한 Task 스펙이라고 판단
        
        요청량이 증가할 경우, Task를 증가시키는 방향으로 선택
        

---

### ✧  기타 설계 설정

- 네트워크 아키텍처 설계
    - VPC CIDR 범위
        - 0.0.0.0/16을 사용하면 65,536개의 IP 주소 사용 가능
        - 고객사 10개까지 확장 고려 시 충분한가요?
          
            - 만약 각 고객사마다 완전히 격리된 VPC를 제공해야 한다면 10.0.0.0/16 하나로는 부족하고, 10.1.0.0/16, 10.2.0.0/16 같은 식으로 고객사별로 별도 VPC를 만들어야 한다.
            - 하지만 공통 인프라를 사용하고 애플리케이션 레벨에서 멀티테넌시를 구현한다면 하나의 VPC로도 충분할 수 있다.
            - 이 경우 ECS Task 수가 늘어나므로 Private 서브넷의 크기를 /22(1024개 IP)나 /21(2048개 IP)로 더 크게 설계하는 것이 안전하다.
        - Subnet당 몇 개의 IP가 필요한가요?
          
            - 배포할 리소스에 따라 결정되는데, ECS Fargate Task 하나당 하나의 Private IP가 필요하고, RDS 인스턴스, NAT Gateway, ALB 등도 각각 IP를 소비한다.
            - Public 서브넷에는 NAT Gateway, ALB가 들어가고, Private 서브넷에는 ECS Task와 RDS가 배치되는데,
            - Auto Scaling으로 Task가 최대 50개까지 늘어날 수 있다면 여유분을 고려해 256개 정도가 적당하다.
            - AZ 구성 시 AZ당 2개의 서브넷을 만들면, 3개 AZ 기준으로 최소 6개의 서브넷이 필요해 10.0.0.0/16을 적절히 나눠 사용해야 된다.
    - Subnet 구성
      
        - Public Subnet 2개 (AZ-a, AZ-c): ALB, NAT Gateway, Bastion 배치
            
            `10.0.0.0/24`, `10.0.1.0/24`
            
        - Private Subnet 2개 (AZ-a, AZ-c): ECS Tasks 배치
            
            `10.0.2.0/24`, `10.0.3.0/24`
            
        - DB Subnet 2개 (AZ-a, AZ-c): RDS 배치
            
            `10.0.4.0/24`, `10.0.5.0/24`
            
    - Route Table 설정
        - Public Subnet Route Table: 0.0.0.0/0 → Internet Gateway
        - Private Subnet Route Table: 0.0.0.0/0 → NAT Gateway (해당 AZ)
        - DB Subnet Route Table: 인터넷 접근 불필요 (RDS는 ECS에서만 접근)
            
            
            | Subnet 이름 | AZ | CIDR | 가용 IP | 용도 |
            | --- | --- | --- | --- | --- |
            | public-subnet-a | 2a | 10.0.0.0/24 | 251 | ALB, NAT-A, Bastion |
            | public-subnet-c | 2c | 10.0.1.0/24 | 251 | NAT-C |
            | private-subnet-a | 2a | 10.0.2.0/24 | 251 | ECS Tasks |
            | private-subnet-c | 2c | 10.0.3.0/24 | 251 | ECS Tasks |
            | db-subnet-a | 2a | 10.0.4.0/24 | 251 | RDS Primary |
            | db-subnet-c | 2c | 10.0.5.0/24 | 251 | RDS Standby |
- 데이터베이스 설계
    - **Single-AZ vs Multi-AZ**
        - Single-AZ: 월 약 $12 (₩15,600)
        - Multi-AZ: 월 약 $24 (₩31,200), 2배 비용
        - 가용성 차이: Single-AZ는 장애 시 수동 복구 (30분 이상), Multi-AZ는 자동 Failover (1-2분)
        - 운영팀장의 "자동 복구" 요구사항과 직결됨
        - 여러분의 선택은? 근거는?
            
            **→ Single-AZ 를 선택**
            
            이유:
            
            **→** 비용 효율을 최우선으로 고려해 초기에는 Single-AZ 선택
            
            **→** 모니터링 강화 + 30분 내 수동 복구 프로세스로 운영 리스크 최소화
            
            **→** 3개월 후 매출 발생 시 Multi-AZ로 자연스러운 확장 가능
            
    - **인스턴스 타입**
        - db.t3.micro: 2 vCPU, 1GB RAM, 평소 트래픽 처리 가능
        - db.t3.small: 2 vCPU, 2GB RAM, 블랙프라이데이 대응 가능, 비용 2배
        - 현재 선택과 향후 업그레이드 계획은?
            
            → 현재는 db.t3.micro로 시작하고, 성능 지표(Freeable Memory, CPU, Latency)를 모니터링해 db.t3.small로 업그레이드 계획을 수립한다.
            
            → 이유 : Amazon RDS는 무중단으로 인스턴스 스케일 업 가능하기 때문에 점진적 확장이 합리적이라고 판단
            
    - **백업 전략**
        - 자동 백업 보관 기간: 7일? 30일? (비용 차이 미미)
        - Point-in-Time Recovery 활성화? (특정 시점으로 복구 가능)
        - 백업은 DB 크기만큼 무료, 초과분만 과금
        
        →  **백업 보관 기간을 30일로 설정하고 Point-in-Time Recovery를 활성화**
        
        운영 안정성과 사용자 데이터 복구 측면에서 가장 안전한 선택이며 비용 영향도 거의 없을 것으로 판단.
        
        - Point-in-Time Recovery
            
            → “10월 3일 13:57 시점으로 복구” 같은 시나리오 대응 가능.
            
            → 운영 사고 대응
            
- 컨테이너 및 배포 전략
    - **Docker 이미지 설계**
        
        서비스별 Dockerfile 작성
        
        - Base Image 선택: `openjdk:17-slim` (작고 가벼움) vs `amazoncorretto:17` (AWS 최적화)
        - Multi-stage build 사용: 빌드 환경과 런타임 환경 분리로 이미지 크기 감소
        - 이미지 크기 최적화: 불필요한 파일 제거, .dockerignore 사용
    
      ```dockerfile
      # Build stage
      FROM amazoncorretto:21-alpine AS build
    
      WORKDIR /app
    
      COPY gradlew .
      COPY gradle gradle
      COPY build.gradle .
      COPY settings.gradle .
    
      COPY user/ user/
    
      RUN ./gradlew :user:clean :user:bootJar -x test --no-daemon
    
      # Runtime stage
      FROM amazoncorretto:21-alpine AS runtime
    
      WORKDIR /app
    
      COPY --from=build /app/user/build/libs/*.jar app.jar
    
      ENTRYPOINT ["java", "-jar", "app.jar"]
    
      EXPOSE 18081
      ```
    
    - **ECS 배포 전략**
        
        두 가지 배포 전략이 있습니다. 각각의 장단점을 이해하고, SwiftLogix 상황에 맞는 것을 선택하세요.
        
        **Rolling Update:**
        
        - 기존 Task를 하나씩 새 버전으로 교체
        - 장점: 간단, 추가 리소스 불필요, 비용 효율적
        - 단점: 배포 중 두 버전 공존 (하위 호환성 필요), 롤백 느림 (6분)
        - 적합한 경우: 빈번한 배포, 하위 호환성이 보장된 변경
        
        **★ Blue-Green Deployment:**
        
        - 새 버전을 완전히 별도로 배포 → 트래픽 한 번에 전환
        - 장점: 즉시 롤백 가능 (10초), 새 버전 철저히 테스트 가능
        - 단점: 배포 중 2배 리소스 필요 (비용 증가), 설정 복잡
        - 적합한 경우: 중요한 릴리스, 충분한 예산, 롤백 빈번한 경우
