/**
 * @projectDescription 태깅 기능 공통 함수
 *
 * 원래 클래스로 만들려고 했으나 태그를 동적으로 생성하는 JS 파일들이 모듈로 모두 변경해야 하는 이슈가 발생하여
 * 함수로 작성하여 제공. 추후 언젠가 관련 JS 들이 ES6 Module 패턴을 지원하게 되면 같은 방식으로 수정 고려.
 *
 * Tagify example : https://yaireo.github.io/tagify/
 * Tagify source : https://github.com/yairEO/tagify
 *
 * 사용법
 *  - ZeniusTag 클래스 선언 시 관련 파라미터 3가지
 *   1) Tag 사용하는 input element
 *   2) tagify 설정용 json 객체 -> 위의 사이트에서 조사. 기본값 존재.
 *   3) zenius 설정용 json 객체
 *      - suggestion : boolean (false)
 *      - realtime : boolean (false)
 *      - tagType : String (required)
 *      - targetId : String (required)
 *
 *  NOTICE : zenius 설정은 tagify 설정보다 우선 적용된다.
 *
 * @author jung hee chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const TAG_URL = '/rest/tags';
const DEFAULT_TAGIFY_SETTING = {
    pattern: /^.{0,100}$/,
    whitelist: [],
    dropdown: {
        maxItems: 20,
        classname: "tags-look",
        enabled: 1,             // <- show suggestions on focus
        closeOnSelect: true     // <- do not hide the suggestions dropdown once an item has been selected
    },
    editTags: false,
    placeholder: i18n.msg('token.msg.tag')
};
const TAG_TYPE = ['ci', 'instance', 'component'];
const TAG_ERROR_MSG = {
    INVALID_TAG_TYPE: 'is invalid tag type. you can use tag type in [' + TAG_TYPE + ']'
};

function zTag(inputElement, usedSettings, tagifySettings) {
    // tag type validation check
    if (!TAG_TYPE.includes(usedSettings.tagType)) {
        console.error(usedSettings.tagType, TAG_ERROR_MSG.INVALID_TAG_TYPE);
        return false;
    }

    // tagify default setting
    this.tagifyOptions = tagifySettings || DEFAULT_TAGIFY_SETTING;

    this.tagifyOptions.targetId = usedSettings.targetId
    this.tagifyOptions.tagType = usedSettings.tagType

    let tagging = new Tagify(inputElement, this.tagifyOptions);

    // 추천목록 사용인 경우 이벤트 등록
    if (usedSettings.suggestion) {
        let controller
        tagging.on('input', (function (e) {
            let value = e.detail.value;
            let tag = e.detail.tagify;
            tag.settings.whitelist.length = 0; // reset the whitelist

            // https://developer.mozilla.org/en-US/docs/Web/API/AbortController/abort
            controller && controller.abort();
            controller = new AbortController();

            // show loading animation and hide the suggestions dropdown
            tag.loading(true).dropdown.hide.call(tag)

            fetch(TAG_URL + '/whitelist?tagValue=' + value + '&tagType=' + tag.settings.tagType,
                {signal: controller.signal}
            ).then(response => response.json())
                .then(whitelist => {
                    // update whitelist Array in-place
                    tag.settings.whitelist.splice(0, whitelist.length, ...whitelist)
                    tag.loading(false).dropdown.show.call(tag, value); // render the suggestions dropdown
                }).catch(() => {
                    console.log('whitelist canceled by user');
                });
        }));
    }

    // 화면 저장과 따로 실시간으로 저장/삭제하는 경우 이벤트 (ex: 처리할문서)
    if (usedSettings.realtime) {
        tagging.on('add', (function (tag) {
            const jsonData = {
                tagType: tag.detail.tagify.settings.tagType,
                tagValue: tag.detail.data.value,
                targetId: tag.detail.tagify.settings.targetId
            };

            fetch(TAG_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jsonData)
            }).then(response => response.text())
                .then(tagId => {
                // DOM 에 tag id  값 추가하기.
                document.querySelector('tag[value="' + tag.detail.data.value + '"]').setAttribute('id', tagId)
                // tagify 데이터에 tag id 값 추가하기.
                tag.detail.tagify.tagData(tag.detail.tagify.getTagElmByValue(tag.detail.data.value), {id: tagId});
            })
        }));

        tagging.on('remove', (function (tag) {
            fetch(TAG_URL + '/' + tag.detail.data.id, {
                method: 'DELETE'
            }).then(response => console.log('tag deleted :', tag.detail.data.value, response.statusText))
        }));
    }
}
