/**
 * @projectDescription 태깅 기능 공통 Class
 *
 * 내부적으로 tagify 라이브러리를 사용하고 있으며 필요한 기능을 정리하고 공통화하기 위한 Wrapper Class.
 *
 * Tagify example : https://yaireo.github.io/tagify/
 * Tagify source : https://github.com/yairEO/tagify
 *
 * 사용법
 * 1. HTML input 태그에 data-tag-type 이 지정되어 있어야 한다. (ci, component, instance 등등)
 * 2.
 *
 * @author jung hee chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const TAG_URL = '/rest/tags/';

class ZeniusTag {
    constructor(inputElement, settings) {
        this.tagType = inputElement.dataset.tagType;
        this.tagging = new Tagify(inputElement, settings);
        this.controller = new AbortController();
        this.init();
    }

    init() {
        this.tagging.on('input', this.onInput);
        this.tagging.on('add', this.onAddTag);
        this.tagging.on('remove', this.onRemoveTag);
    }

    onInput(e){
        let value = e.detail.value;
        this.tagging.settings.whitelist.length = 0; // reset the whitelist

        // https://developer.mozilla.org/en-US/docs/Web/API/AbortController/abort
        this.controller && this.controller.abort();
        this.controller = new AbortController();

        // show loading animation and hide the suggestions dropdown
        this.tagging.loading(true).dropdown.hide.call(this.tagging)

        fetch('/rest/tags/whitelist?tagValue=' + value + '&tagType=' + 'instance', {signal:this.controller.signal})
            .then(RES => RES.json())
            .then(function(whitelist){
                // update whitelist Array in-place
                this.tagging.settings.whitelist.splice(0, whitelist.length, ...whitelist)
                this.tagging.loading(false).dropdown.show.call(this.tagging, value); // render the suggestions dropdown
            })
            .catch(() => {
                console.log('whitelist canceled by user');
            });
    }
    /**
     * 태그 추가.
     *
     * @param tag 태그 정보
     */
    onAddTag(tag) {
        const jsonData = {
            tagType: 'instance',
            value: tag.detail.data.value,
            targetId: document.getElementById('instanceId').getAttribute('data-id')
        };

        aliceJs.sendXhr({
            method: 'POST',
            url: TAG_URL,
            params: JSON.stringify(jsonData),
            contentType: 'application/json',
            showProgressbar: true,
            callbackFunc: function (response) {
                // DOM 에 tag id 값 추가하기.
                document.querySelector('tag[value="' + tag.detail.data.value + '"]').setAttribute('id', response.responseText)

                // tagify 데이터에 tag id 값 추가하기.
                let newId = { id: response.responseText };
                this.tagging.tagData(this.tagging.getTagElmByValue(tag.detail.data.value), newId);
            }
        });
    }

    /**
     * 태그 삭제.
     *
     * @param tag 태그 정보
     */
    onRemoveTag(tag) {
        aliceJs.sendXhr({
            method: 'DELETE',
            url: TAG_URL + tag.detail.data.id,
            showProgressbar: true
        });
    }
}
