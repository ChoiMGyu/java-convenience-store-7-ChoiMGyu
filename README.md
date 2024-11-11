# 편의점

## 개요 및 목표
- 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템이다.
---
<br>

## Commit Type
- feat (feature) : 새로운 기능 추가
- fix (bug fix) : 에러 수정
- docs (documentation) : 문서, 요구사항 수정
- style (formatting, missing semi colons, …) : 코드 포맷팅, 로직은 변경되지 않았지만 코드 띄어쓰기, 세미콜론 누락과 같은 변경점
- refactor : 코드 리팩토링
- test (when adding missing tests) : 테스트 추가
- chore (maintain)
---
<br>

### 기능 요구 사항
- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산 [X]
    - 총 구매액은 상품별 가격 * 수량으로 계산, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액 산출
    - 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분
- 구매 내역과 산출한 금액 정보를 영수증으로 출력 [X]
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택 [X]
- 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException을 발생, "[ERROR]"로 시작하는 에러 메시지를 출력하고 그 부분부터 다시 입력 [X]

#### 재고 관리
- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인 [X]
    - [예외] 사용자가 입력한 상품의 수량이 해당 상품의 재고 수량보다 많을 때
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감 [X]
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지, 다음 고객이 구매할 때 정확한 재고 정보를 제공 [X]

#### 프로모션 할인
- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인 적용 [X]
- 프로모션은 N개 구매 시 1개 무료 증정의 형태로 진행 [X]
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용, 동일 상품에는 한 가지의 프로모션만 적용 [X]
    - [예외] 상품에 두 개 이상의 프로모션이 적용되었을 때
    - [예외] 1+1 또는 2+1 이 아닌 다른 프로모션이 적용되었을 때
- 프로모션 혜택은 프로모션 재고 내에서만 적용 [X]
- 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감, 프로모션 재고가 부족할 경우에는 일반 재고 사용 [X]
    - 같은 이름을 가진 상품에 프로모션 값이 존재하는 재고부터 우선적으로 차감
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내 [X]
    - Y : 증정 받을 수 있는 상품을 추가
    - N : 증정 받을 수 있는 상품을 추가하지 않는다
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내 [X]
    - Y : 일부 수량에 대해 정가로 결제
    - N : 정가로 결제해야하는 수량만큼 제외한 후 결제

#### 멤버십 할인
- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인 [X]
- 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용 [X]
- 멤버십 할인의 최대 한도는 8,000원 [X]
    - Y : 멤버십 할인을 적용
    - N : 멤버십 할인을 적용하지 않음

#### 영수증 출력
- 영수증은 고객의 구매 내역과 할인을 요약하여 출력 [X]
- 영수증 항목
    - 구매 상품 내역 : 구매한 상품명, 수량, 가격
    - 증정 상품 내역 : 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - 금액 정보
        - 총구매액 : 구매한 상품의 총 수량과 총 금액
        - 행사할인 : 프로모션에 의해 할인된 금액
        - 멤버십할인 : 멤버십에 의해 추가로 할인된 금액
        - 내실돈 : 최종 결제 금액
- 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 함

#### 추가 구매 여부 [X]
- Y : 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행
- N : 구매를 종료
---
<br>

### 출력
- 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내. 만약 재고가 0개라면 재고 없음을 출력 [X]
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지 출력 [X]
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지 출력 [X]
- 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력 [X]
- 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력 [X]
- 추가 구매 여부를 확인하기 위해 안내 문구를 출력 [X]
    - 사용자가 잘못된 값을 입력했을 때, "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력 [X]
        - 구매할 상품과 수량 형식이 올바르지 않은 경우: [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
        - 존재하지 않는 상품을 입력한 경우: [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
        - 구매 수량이 재고 수량을 초과한 경우: [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
        - 기타 잘못된 입력의 경우: [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.


