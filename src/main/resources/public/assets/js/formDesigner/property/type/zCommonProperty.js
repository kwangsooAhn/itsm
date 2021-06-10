// TODO: #10641 폼 리팩토링 - 공통속성 정리 일감 처리 후 삭제 예정
import ZInputBoxProperty from './zInputBoxProperty.js';
import ZSwitchProperty from './zSwitchProperty.js';
import ZTagProperty from './zTagProperty.js';
import ZSwitchButtonProperty from './zSwitchButtonProperty.js';
import ZToggleButtonProperty from './zToggleButtonProperty.js';
import ZColorPickerProperty from './zColorPickerProperty.js';
import ZClipboardProperty from './zClipboardProperty.js';
import ZGroupProperty from './zGroupProperty.js';
import ZSliderProperty from './zSliderProperty.js';
import { UNIT } from '../../../lib/ZConstants.js';

export default class ZCommonProperty {
    constructor(target) {
        this.target = target;
    }

    getCommonProperty() {
        // mapId
        const mapIdProperty = new ZInputBoxProperty('mapId', this.target.mapId);
        mapIdProperty.help = 'form.help.mapping-id';

        // is topic
        const isTopicProperty = new ZSwitchProperty('isTopic', this.target.isTopic);
        isTopicProperty.help = 'form.help.is-topic';

        // tag
        const tagProperty = new ZTagProperty('tags', this.target.tags);

        // label - text
        const labelTextProperty = new ZInputBoxProperty('label.text', this.target.labelText);
        labelTextProperty.columnWidth = '8';

        // label - fontSize
        const labelFontSizeProperty = new ZInputBoxProperty('label.fontSize', this.target.labelFontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        labelFontSizeProperty.unit = UNIT.PX;
        labelFontSizeProperty.columnWidth = '3';

        // label - position
        const labelPositionProperty = new ZSwitchButtonProperty('label.position', this.target.labelPosition, [
            {'name': 'icon-position-left', 'value': 'left'},
            {'name': 'icon-position-top', 'value': 'top'},
            {'name': 'icon-position-hidden', 'value': 'hidden'}
        ]);

        // label - align
        const labelAlignProperty = new ZSwitchButtonProperty('label.align', this.target.labelAlign, [
            { 'name': 'icon-align-left', 'value': 'left' },
            { 'name': 'icon-align-center', 'value': 'center' },
            { 'name': 'icon-align-right', 'value': 'right' }
        ]);
        labelAlignProperty.columnWidth = '5';

        // label - fontOption
        const labelFontOption = [
            { 'name': 'icon-bold', 'value': 'bold'},
            { 'name': 'icon-italic', 'value': 'italic' },
            { 'name': 'icon-underline', 'value': 'underline' }
        ];
        const labelFontValue = labelFontOption.map((item) => {
            const method = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            return this.target['labelFontOption' + method] ? 'Y' : 'N';
        }).join('|');
        const labelFontOptionProperty = new ZToggleButtonProperty('label.fontOption', labelFontValue, labelFontOption);
        labelFontOptionProperty.columnWidth = '5';

        // label - fontColor
        const labelFontColorProperty = new ZColorPickerProperty('label.fontColor', this.target.labelFontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        labelFontColorProperty.columnWidth = '12';

        return [
            new ZClipboardProperty('id', this.target.id),
            mapIdProperty,
            isTopicProperty,
            tagProperty,
            new ZGroupProperty('group.display')
                .addProperty(new ZSliderProperty('display.columnWidth', this.target.displayColumnWidth)),
            new ZGroupProperty('group.label')
                .addProperty(labelPositionProperty)
                .addProperty(labelTextProperty)
                .addProperty(labelFontSizeProperty)
                .addProperty(labelAlignProperty)
                .addProperty(labelFontOptionProperty)
                .addProperty(labelFontColorProperty)
        ];
    }
}