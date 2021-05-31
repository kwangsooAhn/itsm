// TODO: #10641 폼 리팩토링 - 공통속성 정리 일감 처리 후 삭제 예정
import InputBoxProperty from './inputBoxProperty.module.js';
import SwitchProperty from './switchProperty.module.js';
import TagProperty from './tagProperty.module.js';
import SwitchButtonProperty from './switchButtonProperty.module.js';
import ToggleButtonProperty from './toggleButtonProperty.module.js';
import ColorPickerProperty from './colorPickerProperty.module.js';
import ClipboardProperty from './clipboardProperty.module.js';
import GroupProperty from './groupProperty.module.js';
import SliderProperty from './sliderProperty.module.js';

export default class CommonProperty {
    constructor(target) {
        this.target = target;
    }

    getCommonProperty() {
        // mapId
        const mapIdProperty = new InputBoxProperty('mapId', this.target.mapId);
        mapIdProperty.help = 'form.help.mapping-id';

        // is topic
        const isTopicProperty = new SwitchProperty('isTopic', this.target.isTopic);
        isTopicProperty.help = 'form.help.is-topic';

        // tag
        const tagProperty = new TagProperty('tags', this.target.tags);

        // labe - text
        const labelTextProperty = new InputBoxProperty('label.text', this.target.labelText);
        labelTextProperty.columnWidth = '8';

        // label - fontSize
        const labelFontSizeProperty = new InputBoxProperty('label.fontSize', this.target.labelFontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        labelFontSizeProperty.unit = 'px';
        labelFontSizeProperty.columnWidth = '3';

        // label - position
        const labelPositionProperty = new SwitchButtonProperty('label.position', this.target.labelPosition, [
            {'name': 'icon-position-left', 'value': 'left'},
            {'name': 'icon-position-top', 'value': 'top'},
            {'name': 'icon-position-hidden', 'value': 'hidden'}
        ]);

        // label - align
        const labelAlignProperty = new SwitchButtonProperty('label.align', this.target.labelAlign, [
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
        const labelFontOptionProperty = new ToggleButtonProperty('label.fontOption', labelFontValue, labelFontOption);
        labelFontOptionProperty.columnWidth = '5';

        // label - fontColor
        const labelFontColorProperty = new ColorPickerProperty('label.fontColor', this.target.labelFontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        labelFontColorProperty.columnWidth = '12';

        return [
            new ClipboardProperty('id', this.target.id),
            mapIdProperty,
            isTopicProperty,
            tagProperty,
            new GroupProperty('group.display')
                .addProperty(new SliderProperty('display.columnWidth', this.target.displayColumnWidth)),
            new GroupProperty('group.label')
                .addProperty(labelPositionProperty)
                .addProperty(labelTextProperty)
                .addProperty(labelFontSizeProperty)
                .addProperty(labelAlignProperty)
                .addProperty(labelFontOptionProperty)
                .addProperty(labelFontColorProperty)
        ];
    }
}