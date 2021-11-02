/**
 * Select tag 디자인 적용을 위한 함수
 *
 * 기본 동작
 * 1) html 에서 구현된 select tag 는 숨기고
 * 2) 그 위에 z-select-box 클래스를 가지는 div 를 구성
 * 3) select 가 가지고 있던 옵션 목록을 복사해서 z-select-options 클래스를 가지는 ul 리스트 구성
 *
 * 사용법
 * 1) 대부분의 경우 aliceJs.initDesignedSelectTag()를 호출해서 사용.
 *   - document 전체에 대해서 select 태그를 검색해서 디자인 일괄 적용
 * 2) 폼 디자이너와 같이 특별한 경우 파라미터로 DOM 을 지정해서 호출하면 해당 영역의 select 를 대상으로 처리
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

aliceJs.initDesignedSelectTag = function (targetDOM) {
    if (!targetDOM) targetDOM = document;
    targetDOM.querySelectorAll('select').forEach(function (originSelectTag) {
        if (originSelectTag.style.display !== 'none') {
            // 이미 그려진 경우 초기화.
            if (originSelectTag.parentElement.classList.contains('z-select')) {
                let removeTarget = originSelectTag.parentElement;
                removeTarget.parentElement.insertBefore(originSelectTag, originSelectTag.parentElement);
                removeTarget.remove();
            }

            // originSelectTag option 중, style의 display가 'none'인 옵션이 존재한다면 해당 옵션을 제외한다
            for (let i = 0; i < originSelectTag.options.length; i++) {
                if (originSelectTag.options[i].style.display === 'none') {
                    originSelectTag.options[i].remove();
                }
            }

            // z-select : select tag 와 추가되는 div, ul 을 감싸는 wrapper 생성.
            originSelectTag.classList.remove('select-hidden');
            let selectWrapper = document.createElement('div');
            selectWrapper.classList = originSelectTag.classList;
            selectWrapper.classList.add('z-select');
            originSelectTag.parentElement.insertBefore(selectWrapper, originSelectTag);
            selectWrapper.append(originSelectTag);

            // z-select-box : 디자인된 SELECT 박스 창 만들기
            let designedSelectBox = document.createElement('div');
            designedSelectBox.classList.add('z-select-box');
            selectWrapper.insertBefore(designedSelectBox, originSelectTag.nextSibling);

            // z-select-box - 라벨
            let designedSelectBoxText = document.createElement('span');
            designedSelectBoxText.className = 'z-select-box-label';
            designedSelectBox.appendChild(designedSelectBoxText);
            // z-select-box - 아이콘
            let designedSelectBoxIcon = document.createElement('span');
            designedSelectBoxIcon.className = 'z-icon i-arrow-right';
            designedSelectBox.appendChild(designedSelectBoxIcon);

            // 인위적으로 추가되는 z-select-box 는 div 라서 focus 효과가 없다.
            // 원본 select tag 의 포커스를 active 클래스를 이용해서 전파.
            originSelectTag.addEventListener('focus', (function (e) {
                e.stopPropagation();
                e.target.nextSibling.classList.add('active');
            }));
            originSelectTag.addEventListener('focusout', (function (e) {
                e.stopPropagation();
                e.target.nextSibling.classList.remove('active');
            }));

            if (originSelectTag.disabled) designedSelectBox.classList.add('disabled-select');

            if (originSelectTag.classList.contains('readonly')) {
                designedSelectBox.classList.add('readonly');
                if (originSelectTag.options[originSelectTag.selectedIndex]) {
                    designedSelectBoxText.innerText = originSelectTag.options[originSelectTag.selectedIndex].text;
                }
            } else {
                // z-select-option : 옵션 리스트용 박스 만들기
                let ulElement = document.createElement('ul');
                ulElement.classList.add('z-select-options');
                selectWrapper.insertBefore(ulElement, designedSelectBox.nextSibling);

                // option 복사
                let options = document.createDocumentFragment();
                for (let i = 0; i < originSelectTag.childElementCount; i++) {
                    let liElement = document.createElement('li');
                    liElement.innerText = originSelectTag.options[i].text;
                    liElement.setAttribute('rel', originSelectTag.options[i].value);
                    if (originSelectTag.options[i].selected) {
                        designedSelectBoxText.innerText = originSelectTag.options[i].text;
                        liElement.classList.add('selected');
                    }
                    options.appendChild(liElement.cloneNode(true));
                }
                ulElement.appendChild(options);
                OverlayScrollbars(ulElement, {className: 'inner-scrollbar'});

                // z-select-box 클릭 이벤트
                if (!originSelectTag.disabled && !originSelectTag.classList.contains('disabled') &&
                    !originSelectTag.classList.contains('readonly') && originSelectTag.options.length > 0) {
                    designedSelectBox.addEventListener('click', (function (e) {
                        e.stopPropagation();
                        let clickedSelect = aliceJs.clickInsideElement(e, 'z-select-box');

                        document.querySelectorAll('div.z-select-box.active').forEach(function (selectTag) {
                            if (selectTag !== clickedSelect) {
                                selectTag.classList.remove('active');
                            }
                        });

                        // toggle
                        if (clickedSelect.classList.contains('active')) {
                            clickedSelect.classList.remove('active');
                        } else {
                            clickedSelect.classList.add('active');
                        }
                    }));

                    // option 을 선택하는 경우 이벤트
                    ulElement.querySelectorAll('li').forEach(function (liOption) {
                        liOption.addEventListener('click', function (clickedOption) {
                            clickedOption.stopPropagation();
                            clickedOption.target.parentElement.querySelectorAll('li').forEach(function (li) {
                                li.classList.remove('selected');
                            });
                            clickedOption.target.classList.add('selected');
                            designedSelectBoxText.innerText = liOption.innerText;
                            // 선택된 값을 원본 select 에 적용.
                            originSelectTag.value = liOption.getAttribute('rel');
                            originSelectTag.querySelector('option[value=\'' + originSelectTag.value + '\']').selected = true;
                            // 숨기기
                            designedSelectBox.classList.remove('active');
                            // 종종 select 선택이 변경되면 다른 화면의 변경을 위해서 이벤트가 있는 경우에 이벤트 발생.
                            let changeEvent = new Event('change');
                            originSelectTag.dispatchEvent(changeEvent);

                            // 폼 디자이너에서 dropdown 컴포넌트를 동작하면서 옵션을 선택 시
                            // 속성창 출력을 위해서 상위 DOM 객체에 이벤트 전달
                            liOption.parentElement.parentElement.click();
                        });
                    });

                    document.addEventListener('click', function () {
                        designedSelectBox.classList.remove('active');
                    });
                }
            }
        }
    });
};
