## 무신사 채용 과제 백엔드 - 카테고리 서비스 구현

### 🙋‍♂️ 소개
  - 안녕하세요. 이번 무신사 백엔드 채용 과제에 참여한 박우진입니다.
  - 카테고리 서비스를 구현하게 되었습니다. 아래 자세한 내용을 기록했습니다. 감사합니다.

### 💻 기술 스택
  - Spring boot 2.6.9
  - Java 11
  - H2 database
  - Spring Data JPA
  - JUnit5

### 🔧 환경 구성 및 빌드, 실행 방법(리눅스 기준)
  - Java11 설치
    - `sudo apt-get update && sudo apt-get upgrade` 
    - `sudo apt-get install openjdk-11-jdk`
  - Git 설치
    - `sudo apt install git`
  - Git Clone
    - `git clone https://github.com/ak2j38/musinsa-test.git`
  - Build
    - `cd '프로젝트 경로'`
    - `gradlew build`
    - `cd build/libs`
    - `java -jar musinsa-0.0.1-SNAPSHOT.jar`

### 📒 구현사항
  - 카테고리 설계
    - 카테고리 엔티티에서 부모카테고리, 자식카테고리 리스트를 갖고 있도록 구현했습니다.
    - 위의 방법은 depth가 깊어질수록 성능이 떨어진다는 단점이 있지만 실제 무신사의 카테고리 구현에서는 최대 depth가 2정도로 확인되어 구성했습니다.
    - `Path Enumeration` 방법과 `Closuer Table` 방법 또한 고민하였으나 각각의 단점들이 보여 최종적으로 위의 방법을 선택했습니다.
  ```java 
@Entity
@Getter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String code;
    private Integer depth;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.PERSIST)
    private List<Category> subCategories = new ArrayList<>();

    private Category(String name, String code, Integer depth, Category parent) {
        this.id = null;
        this.name = name;
        this.code = code;
        this.depth = depth;
        this.parentCategory = parent;
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
    }

    public static Category of(String name, String code, Integer depth, Category parent) {
        return new Category(name, code, depth, parent);
    }

    public void addSubCategory(Category child) {
        this.subCategories.add(child);
    }

    public void updateName(String name) {
        if (name != null) {
            this.name = name;
            this.setUpdatedAt(LocalDateTime.now());
        }
    }
}
  ``` 
- - -
👇 REST API 목록
- 전체 카테고리 조회
  - `GET /categories`
- 특정 카테고리 조회
  - `GET /categories/{id}`
  - id는 Long 타입
- 카테고리 저장
  - `POST /categories`
  - CategoryRequest 예시 
```json
{
"name":"상의",
"depth": 1,
"code": "001",
"parentId": 1
}
```
  - 카테고리 업데이트
    - `PUT /categories/{id}`
    - CategoryUpdate 예시
```json
{
"name":"하의"
}
```
  - 카테고리 삭제
    - `DELETE /categories/{id}`
    - 하위에 카테고리가 존재하는 경우에는 삭제할 수 없습니다. 이 경우 `ExistSubCategoryException`가 발생하며 `"하위에 카테고리가 존재합니다. 먼저 삭제해주세요."`라는 예외메시지를 리턴합니다.
- - -
  👨‍💻 테스트 결과
  > <img width="713" alt="스크린샷 2022-07-17 오후 10 18 33" src="https://user-images.githubusercontent.com/29879110/179400271-a0c584ef-9d76-4286-b896-fef8c489fed2.png">


  
