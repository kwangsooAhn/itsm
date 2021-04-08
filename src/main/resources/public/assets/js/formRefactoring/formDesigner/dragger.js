/**
 * drag & drop Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import { EventHandler } from '../lib/eventHandler.js';
import { UIDiv } from '../lib/ui.js';

export class Dragger {
    constructor(editor) {
        this.editor = editor;
        this.dragElements = [];
        // dropzone 생성
        const formElement = this.editor.form.UIElement.domElement;
        formElement.addEventListener('dragenter', this.dragEnter.bind(this), false);
        formElement.addEventListener('dragover', this.dragOver.bind(this), false);
        formElement.addEventListener('drop', this.drop.bind(this), false);
        formElement.addEventListener('dragleave', this.dragLeave.bind(this), false);


        //this.domElement = element; // drag 대상
        //this.container = this.editor.form.UIElement.domElement; // drop 영역
        //this.copy = options.copy || false; // 복사여부
        this.fakeElement = new UIDiv().setClass('fake-draggable')
            .setAttribute('draggable', true);

        // event init
        //this.event = new EventHandler();
        //this.event.on('start', this.dragStart, this);
        //this.event.on('over', this.dragOver, this);
        //this.event.on('drop', this.drop, this);
        //this.event.on('leave', this.dragLeave, this);
        //this.event.on('end', this.dragEnd, this);

        // event bind
        //this.domElement.addEventListener('dragstart', this.dragStart.bind(this), false);
        //this.domElement.addEventListener('dragend', this.dragEnd.bind(this), false);
        //this.container.addEventListener('dragenter', this.dragEnter.bind(this), false);
        //this.container.addEventListener('dragover', this.dragOver.bind(this), false);
        //this.container.addEventListener('drop', this.drop.bind(this), false);
        //this.container.addEventListener('dragleave', this.dragLeave.bind(this), false);
    }

    addDrag(element) {
        element.addEventListener('dragstart', this.dragStart.bind(this), false);
        element.addEventListener('dragend', this.dragEnd.bind(this), false);
        this.dragElements.push(element);
    }

    dragStart(e) {
        e.stopImmediatePropagation();
        e.target.classList.add('selected');

        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('text/plain', e.target.id);
        e.dataTransfer.setDragImage(e.target, 60, 50);
        setTimeout(() => (e.target.classList.remove('selected')), 0);

        if (e.target.getAttribute('data-draggable-role') === 'copy') {
            this.editor.form.UIElement.domElement.appendChild(this.fakeElement.domElement);
        }
    }
    // these functions prevents default behavior of browser
    dragEnter(e) {
        e.preventDefault();
        //console.log('enter');
        //e.target.classList.add('hovered');
    }

    dragOver(e) {
        // dragEnter 이벤트가 동작할 경우, drop 이벤트가 발생하지 않으므로 아래 코드 필수
        e.preventDefault();
        this.fakeElement.addClass('draggable-in');
        e.dataTransfer.dropEffect = 'move';
        // TODO: move fakeElement

        // TODO: sort component
        // group , row, component 추가
        //const rowElement = aliceJs.clickInsideElement(e, 'zenius-row');
        //if (rowElement) {
        // console.log('row');
        // component 만 추가
        //}
        //dragger.event.emit('over', e);
    }

    drop(e) {
        e.stopPropagation(); // Stops some browsers from redirecting.
        e.preventDefault();
        console.log('drop');
        const id = e.dataTransfer.getData('text/plain');
        //const dragElement = document.getElementById(id).cloneNode(true);
        //console.log(dragElement);
        // dropZone이 form 내부이면 group, row, component 추가
        const dropZone = e.target;
        // dropZone이 row 내부이면 component 추가
        if (aliceJs.clickInsideElement(e, 'zenius-row')) {
            console.log('컴포넌트');
            console.log(dropZone);
        }
        //dropZone.appendChild(dragElement);
        e.dataTransfer.clearData();
        //dragger.event.emit('drop');
        return false;
    }

    dragLeave(e) {
        //console.log('leave');
        this.fakeElement.removeClass('draggable-in');
        //dragger.event.emit('leave');
    }

    dragEnd(e) {
        console.log('end');
        if (e.target.getAttribute('data-draggable-role') === 'copy') {
            this.editor.form.UIElement.domElement.removeChild(this.fakeElement.domElement);
        }
        //dragger.event.emit('end');
    }

    makeDragItem () {

    }
}
