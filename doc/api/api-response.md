# Response 규격

백엔드와 프론트엔드 간에 REST API 통신시 Response 규격을 정의한다.

## 목차

---

1. [백엔드](#백엔드)
2. [프론트엔드](#프론트엔드)

---

서버와 통신 실패, 권한 에러 등 HTTP 에러 발생시에는 error.html 페이지로 리다이렉트 된다.
그 외에 데이터를 CRUD 하는 과정에서 발생하는 유효성 검증 에러나 실패는 규격화된 에러 코드를 사용하기로 한다.

## 백엔드

백엔드에서 데이터를 조회, 추가, 수정, 삭제 등의 처리를 진행할 때 아래와 같인 구조로 처리된다.

```
Controller -> Service -> Repository -> RepositoryImpl
```

Service에서 값을 리턴할 때 아래와 같이 ZResponse 에 결과를 담도록 한다.

```kotlin
data class ZResponse(
    val status: String = ZResponseConstants.STATUS.SUCCESS.code,
    var message: String? = null,
    val data: Any? = null
) : Serializable
```

Controller에서 화면으로 전송하는 모든 데이터는 아래의 구조를 갖는다.

```kotlin
fun response(response: ZResponse): ResponseEntity<ZResponse> {
    return ResponseEntity(response, this.setHeader(), HttpStatus.OK)
}
```

백엔드에서 사용되는 에러코드는 ZResponseConstants.kt 파일에 공통으로 정의하여 사용한다.

```kotlin
/**
 * 상태 코드
 */
enum class STATUS(val code: String) {
    SUCCESS("Z-0000"),
    SUCCESS_EDIT("Z-0001"),
    SUCCESS_EDIT_EMAIL("Z-0002"),
    SUCCESS_EDIT_PASSWORD("Z-0003"),
    ERROR_FAIL("E-0000"),
    ERROR_DUPLICATE("E-0001"),
    ERROR_EXPIRED("E-0002"),
    ERROR_NOT_EXIST("E-0003"),
    ERROR_EXIST("E-0004"),
    ERROR_NOT_FOUND("E-0005"),
    ERROR_ACCESS_DENY("E-0006"),
    ERROR_DUPLICATE_EMAIL("E-0007"),
    ERROR_DUPLICATE_ORGANIZATION("E-0008"),
    ERROR_DUPLICATE_WORKFLOW("E-0009"),
    ERROR_NOT_EXIST_CLASS("E-0010")
}
```

성공은 'Z-' prefix를 사용하며 실패는 'E-' prefix를 사용한다.

## 프론트엔드

프론트엔드에서 REST API 통신시 아래와 같이 유틸에 정의된 aliceJs.fetchJson 을 사용한다.

```javascript
/**
 * 비동기 통신 후 Promise 형태로 Json 데이터를 반환하는 함수
 * @param url url
 * @param option 옵션
 * @returns Promise 객체 반환값
 */
aliceJs.fetchJson = function(url, option) {
    return aliceJs.doFetch(url, option)
        .then(response => response.text())
        .then((data) => {
            // 공통 알림창은 없으며 각자 페이지에서 처리할 예정
            return data ? JSON.parse(data) : {};
        });
};
```

프론트 엔드에서 사용되는 에러코드는 zUtil.js 파일에 공통으로 정의하여 사용한다.

```javascript
// 응답 코드 - 서버에서 전달되는 코드값과 항상 일치하도록 관리한다.
aliceJs.response = {
    success: 'Z-0000',
    successEdit: 'Z-0001',
    successEditEmail: 'Z-0002',
    successEditPassword: 'Z-0003',
    error: 'E-0000',
    duplicate: 'E-0001',
    expired: 'E-0002',
    notExist: 'E-0003',
    exist: 'E-0004',
    notFound: 'E-0005',
    accessDeny: 'E-0006',
    duplicateEmail: 'E-0007',
    duplicateOrganization: 'E-0008',
    duplicateWorkflow: 'E-0009',
    notExistClass: 'E-0010'
};
```

아래와 같이 전달받은 상태 코드에 따라서 각 페이지에서 알림창을 표시하거나 성공시 작업을 처리한다.

```javascript
aliceJs.fetchJson('/rest/custom-codes', {
    method: method,
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
}).then((response) => {
    switch (response.status) {
        case aliceJs.response.success:
            zAlert.success(i18n.msg('common.msg.save'), function () {
                location.reload();
            });
            break;
        case aliceJs.response.duplicate:
            zAlert.warning(i18n.msg('customCode.msg.duplicateCustomCodeName'));
            break;
        case aliceJs.response.notExist:
            zAlert.warning(i18n.msg('customCode.msg.pCodeNotExist'));
            break;
        case aliceJs.response.exist:
            zAlert.warning(i18n.msg('customCode.msg.customCodeUsed'));
            break;
        case aliceJs.response.error:
            zAlert.danger(i18n.msg('common.msg.fail'));
            break;
        default :
            break;
    }
});
```