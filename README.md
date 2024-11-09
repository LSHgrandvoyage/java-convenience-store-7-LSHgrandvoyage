# java-convenience-store-precourse

## Input 처리
&ensp; - 제품별로 comma로 구분되고 구분된 contents는 "[product-quantity]" 형식으로 표현되어야 함  
&ensp; - 제품명과 구매 수량 형식 오류 -> [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.  
&ensp; - 존재하지 않는 product 입력 -> [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.  
&ensp; - 구매 수량이 재고를 초과 -> [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.  
&ensp; - 기타 입력 오류(ex. Blank 포함) -> [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.  
&ensp; - Promotions.md의 Date와 현재 Date를 비교, 일치할 경우 Promotion 상품으로 처리

## 재고 관리
&ensp; -  
&ensp; -  
&ensp; -  

## 프로모션 할인
&ensp; -  
&ensp; -  
&ensp; -  

## 멤버십 할인
&ensp; -  
&ensp; -  
&ensp; -  

## 영수증 출력
&ensp; -  
&ensp; -  
&ensp; -  

## Exception
&ensp; - Focus on user input with validation  
&ensp; - Print [ERROR] with error messages  
&ensp; - In this case, 에러 발생 지점 이전부터 restart  

## 입출력 담당 클래스 별도 구현
&ensp; - InputView, OutputView 두 개의 class 구현  
&ensp; - Input 관련된 내용(ex. validate 등)은 InputView 클래스에  
&ensp; - 프로그램 실행 시 product.md 재고 내용 출력(comma로 구분, 재고 0일시 "재고 없음", promotion == null일 경우 출력 안함)  