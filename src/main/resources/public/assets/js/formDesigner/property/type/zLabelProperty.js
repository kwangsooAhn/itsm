/**
 * label Property Class
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZInputBoxProperty from './zInputBoxProperty.js';
import ZSwitchButtonProperty from './zSwitchButtonProperty.js';
import ZToggleButtonProperty from './zToggleButtonProperty.js';
import ZColorPickerProperty from './zColorPickerProperty.js';
import ZGroupProperty from './zGroupProperty.js';
import { UNIT } from '../../../lib/ZConstants.js';

export default class ZLabelProperty {
    constructor(target) {
        this.target = target;
    }

    getLabelProperty() {
        // label - text
        const labelTextProperty = new ZInputBoxProperty('labelText', 'label.text', this.target.labelText);
        labelTextProperty.columnWidth = '9';

        // label - fontSize
        const labelFontSizeProperty = new ZInputBoxProperty('labelFontSize', 'label.fontSize', this.target.labelFontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        labelFontSizeProperty.unit = UNIT.PX;
        labelFontSizeProperty.columnWidth = '3';

        // label - position
        const labelPositionProperty = new ZSwitchButtonProperty('labelPosition', 'label.position', this.target.labelPosition, [
            { 'name': 'i-position-left', 'value': 'left' },
            { 'name': 'i-position-top', 'value': 'top' },
            { 'name': 'i-position-hidden', 'value': 'hidden' }
        ]);

        // label - align
        const labelAlignProperty = new ZSwitchButtonProperty('labelAlign', 'label.align', this.target.labelAlign, [
            { 'name': 'i-align-left', 'value': 'left' },
            { 'name': 'i-align-center', 'value': 'center' },
            { 'name': 'i-align-right', 'value': 'right' }
        ]);
        labelAlignProperty.columnWidth = '6';

        // label - fontOption
        const labelFontOption = [
            { 'name': 'i-bold', 'value': 'bold'},
            { 'name': 'i-italic', 'value': 'italic' },
            { 'name': 'i-underline', 'value': 'underline' }
        ];
        const labelFontValue = labelFontOption.map((item) => {
            const method = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            return this.target['labelFontOption' + method] ? 'Y' : 'N';
        }).join('|');
        const labelFontOptionProperty = new ZToggleButtonProperty('labelFontOption', 'label.fontOption', labelFontValue, labelFontOption);
        labelFontOptionProperty.columnWidth = '6';

        // label - fontColor
        const labelFontColorProperty = new ZColorPickerProperty('labelFontColor', 'label.fontColor', this.target.labelFontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        labelFontColorProperty.columnWidth = '12';

        return [
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