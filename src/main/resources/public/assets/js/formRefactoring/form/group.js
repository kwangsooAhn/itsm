/**
 * 컴포넌트 내부를 이루는 구성 Class.
 *
 *  Group은 1개 이상의 Row로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { CLASS_PREFIX, FORM } from '../lib/constants.js';
import { UICheckbox, UIDiv, UILabel, UISpan } from '../lib/ui.js';

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.margin = data.margin || '10px 0px 10px 0px'; // 그룹 간 간격(위 오른쪽 아래 왼쪽)
        this.label = util.mergeObject(data.label || FORM.DEFAULT.GROUP_LABEL);
        this.isAccordionUsed = data.isAccordionUsed;

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        // 그룹용 툴팁
        const groupTooltip = new UIGroupTooltip().setMargin(this.margin);
        // 그룹
        groupTooltip.UIGroup = new UIGroup(this.isAccordionUsed).setId(this.id);
        // 아코디언용 체크박스
        groupTooltip.UIGroup.UICheckbox.setId('chk-' + this.id)
            .setClass(CLASS_PREFIX + 'group-accordion-checkBox');
        // 라벨
        groupTooltip.UIGroup.UILabel.setFor('chk-' + this.id)
            .addClass((this.label.isLabelUsed ? 'on' : 'off'))
            .setTextAlign(this.label.align); // 라벨 사용여부: 라벨 숨김 또는 보임
        // 라벨 텍스트
        groupTooltip.UIGroup.UILabel.UILabelText.setFontSize(this.label.fontSize)
            .setFontWeight((this.label.bold ? 'bold' : ''))
            .setFontStyle((this.label.italic ? 'italic' : ''))
            .setTextDecoration((this.label.underline ? 'underline' : ''))
            .setColor(this.label.fontColor)
            .setTextContent(this.label.text);

        groupTooltip.add(groupTooltip.UIGroup);
        this.UIElement = groupTooltip;
    }
}

export class UIGroupTooltip extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + 'group-tooltip';
    }
}

export class UIGroup extends UIDiv {
    constructor(boolean) {
        super();
        this.domElement.className = CLASS_PREFIX + 'group';

        if (boolean) {
            this.addClass('accordion');
        }

        this.UICheckbox = new UICheckbox(true);
        this.add(this.UICheckbox);

        this.UILabel = new UILabel().setClass(CLASS_PREFIX + 'group-label');
        this.UILabel.UILabelText = new UISpan().setClass(CLASS_PREFIX + 'group-label-text');
        this.UILabel.UIIcon = new UISpan().setClass(CLASS_PREFIX + 'group-label-icon arrow-left');
        this.UILabel.add(this.UILabel.UILabelText).add(this.UILabel.UIIcon);
        this.add(this.UILabel);
    }
}