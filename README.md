# CoffeeChat

Spring Boot 기반 커피챗 신청 예제입니다. 본 리포지터리의 실제 프로젝트는 `coffeeChat` 폴더 하위에 존재합니다.

## 빌드 및 실행 방법

Gradle Wrapper가 포함되어 있으므로 아래 명령만으로 빌드와 실행이 가능합니다.

```bash
cd coffeeChat
./gradlew build       # 빌드
./gradlew bootRun     # 애플리케이션 실행
```

빌드 후 JAR 파일을 직접 실행하고 싶다면 다음과 같이 할 수 있습니다.

```bash
./gradlew bootJar
java -jar build/libs/coffeeChat-0.0.1-SNAPSHOT.jar
```

## Solapi 설정 방법

`src/main/resources/application.properties` 파일에 Solapi API 키와 시크릿을 설정합니다.

```properties
solapi.api.key=YOUR_API_KEY
solapi.api.secret=YOUR_API_SECRET
```

환경 변수나 외부 설정 파일을 통해서도 같은 이름의 프로퍼티를 지정할 수 있습니다.

## 간단한 사용 예시

애플리케이션을 실행한 뒤 브라우저에서 [http://localhost:8080/apply](http://localhost:8080/apply) 에 접속하면 신청 폼을 확인할 수 있습니다. 필요한 정보를 입력해 신청하면 관리자가 확인 후 승인 링크를 통해 승인을 진행합니다. 승인되면 신청자에게 알림톡이 발송됩니다.

관리자 전화번호, 템플릿 코드 등은 코드에서 `ADMIN_PHONE`이나 `TEMPLATE_ADMIN` 등의 값으로 되어 있으므로 실제 환경에 맞게 수정해야 합니다.

## 테스트 실행 방법

단위 테스트를 실행하려면 다음 명령을 이용합니다.

```bash
cd coffeeChat
./gradlew test
```

