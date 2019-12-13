# YOBO-BACKEND
## 개요
### YOBO 어플리케이션에서 사용하는 API들을 제공하는 백엔드 서버. SPRING BOOT 기반으로 작동한다.
## 1. basketController
장바구니를 API를 제공하는 컨트롤러.  
1.1 CreateBasket  
제공받은 USER의 도큐먼트 아이디로 새로운 장바구니 생성.

1.2 insertBasket 
USER DID, PRODUCT DID, Qty를 기반으로 장바구니에 물건을 추가한다.  
기존 동일한 품목이 있는 경우에는 수량을 더해준다.

1.3 updateBasket  
USER DID, PRODUCT DID, Qty를 기반으로 수량을 수정한다.  

1.4 DeleteBasket  
USER DID, PRODUCT DID를 기반으로 장바구니의 물건을 삭제한다. 

1.5 getBasket  
USER DID를 기반으로 장바구니를 불러온다.

## 2. commentsController  
코멘트 API를 제공하는 컨트롤러.  
2.1 createcomments  
domain에 존재하는 YOBOComment형태의 오브젝트를 전달받아 코멘트를 저장한다.  
  
2.2 getCommentsbyRId  
RECIPE DID를 기반으로 코멘트를 돌려준다.

2.3 updateComments 
domain에 존재하는 YOBOComment형태의 오브젝트를 전달받아 코멘트를 수정한다.

2.4 getCommentsbyUId  
USER DID를 기반으로 유저가 작성한 코멘트를 돌려준다.

## 3. companyController  
컴퍼니 API를 제공하는 컨트롤러. 

3.1 createcompany  
domain에 존재하는 YoboCompany형태의 오브젝트를 전달받아 코멘트를 저장한다.  

3.2 updateCompany  
domain에 존재하는 YoboCompany형태의 오브젝트를 전달받아 코멘트를 수정한다.  

3.3 getCompanyByName
회사명을 기반으로 회사정보를 돌려준다.

3.4 getCompanyByEmail
회사 EMAIL(ID와 동일)을 기반으로 회사정보를 돌려준다.

## 4. LoginController  
로그인 API를 제공하는 컨트롤러. 

4.1 users/naverlog
USER의 로그인을 처리하는 API, USER가 YOBO 어플리케이션에서 받은 AT를 기반으로 네이버 로그인 서버에서 USER ID를 가져온다.  
USER ID를 이용하여 저장된 유저 정보를 돌려준다.


4.2 company/naverlog
company 로그인을 처리하는 API, company YOBO-WEB에서 받은 EMAIL을 기반으로 저장된 company 정보를 돌려준다. 

## 5. productController
물품 API를 제공하는 컨트롤러. 

5.1 createProduct  
물품을 생성하는 API, YOBO-WEB에서 사용되며 물품에 필요한 필드들을 RequestParam형태로 받아 저장한다.

5.2 getImage  
파일 경로를 기반으로 물품의 이미지를 준다.

5.3 search  
물품명을 기반으로 물품 목록을 돌려준다.  

5.4 delete  
물품의 DID를 기반으로 물품을 삭제한다. 

5.5 searchbyPid  
판매 회사의 DID 기반으로 물품 목록을 돌려준다.

5.5 searchbyDid  
물품의 DID를 기반으로 물품 정보를 돌려준다.

5.6 getProducteList
존재하는 물품의 목록을 돌려준다.

5.7 searchbycate  
카테고리를 기반으로 물품 목록을 돌려준다.  

## 6. recipeController
레시피 API를 제공하는 컨트롤러

6.1  search  
레시피 명을 기반으로 레시피 목록을 돌려준다.

6.2 getRecipeList  
존재하는 레시피의 목록을 돌려준다. 

6.3 getListbyCate  
카테고리를 기반으로 레시피 목록을 돌려준다.  

6.4 getListbyUid  
유저의 ID를 기반으로 레시피 목록을 돌려준다.

6.5 getRecipebyDid  
레세피의 DID를 기반으로 레시피 정보를 돌려준다.

6.6 DeleteDid  
레세피의 DID를 기반으로 레시피를 삭제한다.

6.7 getImage
경로를 기반으로 레시피 이미지를 돌려준다.

6.8 rate
레시피의 DID, 유저의 DID, 평점을 기반으로, 레시피 정보에 평점을 추가한다.

6.9 checkrate  
레시피의 DID, 유저의 DID를 기반으로 평점을 매긴 레시피일 경우 -1, 아닌경우 1을 돌려준다.

6.10 createRecipe  
레시피 이미지, Domain의 YoboRecipe 형태의 오브젝트를 기반으로 레시피를 저장한다.

6.11 updateRecipe  
레시피 이미지, Domain의 YoboRecipe 형태의 오브젝트를 기반으로 레시피를 수정한다.

6.12 getByshortcut  
유저 DID를 기반으로 즐겨찾기에 저장된 레시피 목록을 돌려준다.

6.13 getByingredients  
재료 목록을 기반으로 하여 재료가 포함된 레시피를 많이 포함된 순으로 돌려준다.

6.14 getByrecommend  
유저의 선호 레피시목록을 기반으로 추천하는 레시피 목록을 돌려준다.

## 7 reportController  
신고 API를 제공하는 컨트롤러.

7.1 createReport  
레시피의 DID,  코멘트의 DID, 신고의 종류, 유저의 DID를 기반으로 신고를 저장한다.

7.2 getByRid  
레시피의 DID를 기반으로 신고 목록을 돌려준다.

7.3 getByUid  
유저의 DID를 기반으로 신고 목록을 돌려준다. 

7.4 getByDid  
코멘트의 DID를 기반으로 신고 목록을 돌려준다.

7.5 getReport  
모든 신고 목록을 돌려준다.

## 8. transactionController  
주문내역 API를 제공하는 컨트롤러.  

8.1 modifystatus  
주문내역의 DID, invoice_company, invoice_number, transaction_status를 기반으로 배송 상태를 수정하는 API

8.2 createtransaction  
DOAMIN의 Yobotransaction_log를 기반으로 주문 내역을 저장한다.

8.3  getByUid
유저의 DID를 기반으로 주문 목록을 돌려준다. 

8.4 getByCid  
회사의 DID를 기반으로 주문 목록을 돌려준다.

8.5 getByDid
주문내역의 DID를 기반으로 주문 정보를 돌려준다. 

## 9. userController  
유저 API를 제공하는 컨트롤러

9.1 getbyEmail  
Email(ID)를 기반으로 유저 정보를 돌려주는 API, 미가입된 USER일 경우 새로 USER정보를 생성한다.

9.2 getbyDid  
유저의 DID를 기반으로 유저 정보를 돌려준다.

9.3 createUser  
DOMAIN의 YoboUser 오브젝트를 기반으로 유저를 저장한다.

9.4 updateUser  
이미지, DOMAIN의 YoboUser 오브젝트를 기반으로 유저를 수정한다.

9.5 addShortCut  
유저 DID, 레시피 DID를 기반으로 즐겨찾기를 추가한다.  
기존에 존재하는 즐겨찾기일 경우 DeleteShortCut을 이용하여 삭제한다.

9.6 DeleteShortCut  
유저 DID, 레시피 DID를 기반으로 즐겨찾기를 삭제한다.  

9.7 getImage  
경로를 기반으로하여 이미지를 돌려준다. 
