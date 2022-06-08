## 포인트 관리 배치 API

---
### 1. 개발 목적
#### 스프링 배치를 조금 더 유연하고 능숙하게 다룰 수 있도록 하는 프로젝트이며, QueryDSL과 JPA에 대한 이해도를 높이기 위해서 만들었습니다.

### 2. 개발 내용
* #### 유저와 포인트 지갑
  * #### 1명의 유저는 1개의 포인트 지갑을 가진다.
  * #### 1명의 유저는 N개의 포인트를 적립한 내역을 가지고 있다.
  * #### 적립된 포인트들은 유효기간이 존재한다.
* #### 포인트 만료
  * #### 유효기간이 만료되면 해당 포인트는 사용불가한다.
  * #### 유효기간은 일단위로 기준하고 시간단위는 무시한다.
  * #### 포인트가 만료된다면 포인트지갑의 잔액도 차감한다.
  * #### 이미 사용한 포인트는 만료하지 않는다.
  * #### 하루에 한 번 알림을 위해 유효기간이 만료된 총 금액을 구한다.
  * #### 하루에 한 번 알림을 위해 유효기간이 1주일 이내로 임박한 포인트 금액을 구한다.
* #### 포인트 예약 적립
  * #### 예약된 포인트는 정해진 일시와 현재 날짜가 같다면 해당 유저에게 적립한다.
  * #### 적립한 포인트만큼 포인트 지갑의 잔액을 증가시킨다.
  * #### 적립 예약이 완료된 포인트는 상태를 완료료 변경시킨다.

### 3. 개발 결과물
* #### 실행 전 준비 단계
![실행 전 프로시저](https://user-images.githubusercontent.com/64354998/172563105-59b176dd-5dd5-44ea-b198-7503fbb19e43.PNG)
#### <1번 포인트 지갑과 2번 포인트 지갑을 생성하고 1번 포인트 지갑에 3천 건의 예약 포인트를 삽입한다.>
![point_wallet](https://user-images.githubusercontent.com/64354998/172563470-ce960673-f441-4b63-9cec-ce3639bf6240.PNG)
#### <1,2번 포인트 지갑 생성>
![프로시저 실행 후 point_reservation](https://user-images.githubusercontent.com/64354998/172563539-9c6de5cc-f5af-40ac-af83-5e7cc9d609c7.PNG)
![프로시저 실행 후 point_reservation2](https://user-images.githubusercontent.com/64354998/172563588-abd0a8d4-c799-466c-a2fd-9dcf9f31dee7.PNG)
#### <1번 포인트 지갑에 3천건 포인트 적립>
* #### 예약 포인트 배치
![예약 포인트 배치](https://user-images.githubusercontent.com/64354998/172563880-b42b55d0-e1de-4d96-9116-34c5689f2ae5.PNG)
#### <JobParameter로 today를 설정하고 잡(executePointReservationJob)을 실행시킨다.>
![예약 포인트 배치 wallet](https://user-images.githubusercontent.com/64354998/172564092-f69c0d83-3b39-4337-844c-c8a4c1ed124c.PNG)
![예약포인트 배치 point](https://user-images.githubusercontent.com/64354998/172564368-09712399-2021-4d5d-87b7-addc90621cd7.PNG)
![예약 포인트 배치 reservation](https://user-images.githubusercontent.com/64354998/172564375-75bb6b5e-00ba-4020-b5a8-e9973c7d60b3.PNG)
#### <reservation 테이블의 is_executed가 1로 설정되고 1번 포인트 지갑 엔티티는 예약된 포인트들을 얻는다.>
* #### 예약 만료된 포인트 배치
![예약 만료된 포인트 배치1](https://user-images.githubusercontent.com/64354998/172564855-54dd6743-8f5f-4ed9-9340-df2ffeb75631.PNG)
#### <2번 포인트 지갑에 발행일이 2022년 5월 20일로 설정된 포인트들을 삽입한다.>
![예약 만료된 포인트 배치2](https://user-images.githubusercontent.com/64354998/172564996-338f4849-1b41-4422-a737-77c03f728808.PNG)
#### <JobParameter인 today를 2022년 5월 20일로 설정하고 잡(executePointReservationJob)을 실행한다.>
![예약 만료된 포인트 배치3](https://user-images.githubusercontent.com/64354998/172565283-bd6ae40c-4b2c-47b0-8fb3-b181a1c36cd1.PNG)
#### <2번 포인트 지갑에 150000원이 쌓이게 된다.>
![예약 만료된 포인트 배치4](https://user-images.githubusercontent.com/64354998/172565365-19a73164-7936-4ae9-97b9-96fef029a122.PNG)
#### <JobParameter인 today를 2022년 6월 8일로 설정하고 잡(expirePointJob)을 실행한다.>
![예약 만료된 포인트 배치5](https://user-images.githubusercontent.com/64354998/172565633-d48dcfe8-11b5-489a-85b8-9841cd6847d7.PNG)
#### <2번 포인트 지갑이 0원이 되었다.>
* #### 만료된 포인트 메시지 배치와 곧 만료될 포인트 메시지 배치
![포인트 만료 메시지2](https://user-images.githubusercontent.com/64354998/172565900-0684dedd-5377-497d-a605-dc66ad52335f.PNG)
#### <현재 만료일이 2022년 5월 30일인 포인트들이 존재한다.>
![포인트 만료 메시지1](https://user-images.githubusercontent.com/64354998/172566059-c6c601ee-fff3-428f-97bd-b848e8ade44a.PNG)
#### <JobParameter인 today를 2022년 5월 31일 (만료일 다음 날)로 설정하고 잡(messageExpiredPointJob)을 실행한다.>
![포인트 만료 메시지3](https://user-images.githubusercontent.com/64354998/172566226-0b34cd28-0e74-4776-b379-adcbb71d36e8.PNG)
#### <message 테이블에 만료된 포인트들을 확인시켜주는 데이터들이 쌓였다.>
![곧 만료될 포인트 배치1](https://user-images.githubusercontent.com/64354998/172566390-86f49197-2012-46c2-8e72-0582e4f5eac2.PNG)
#### <현재 만료일이 2022년 6월 18일인 포인트들이 존재한다.>
![곧 만료될 포인트 배치2](https://user-images.githubusercontent.com/64354998/172566545-8e706aff-5d80-4573-b5c1-f644fa9f350f.PNG)
#### <JobParameter인 today를 2022년 6월 12일(만료일 7일 전)로 설정하고 잡(MessageExpireSoonPointJob)을 실행한다.>
![곧 만료될 포인트 배치3](https://user-images.githubusercontent.com/64354998/172566732-3b2198d7-b0f8-4d55-a8f6-bdef99437ec4.PNG)
#### <message 테이블에 곧 만료될 포인트들을 확인시켜주는 데이터들이 쌓였다.>
