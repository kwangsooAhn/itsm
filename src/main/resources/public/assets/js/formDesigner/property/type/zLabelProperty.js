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
import { UNIT } from '../../../lib/zConstants.js';

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
            { 'name': 'ic-position-left', 'value': 'left' },
            { 'name': 'ic-position-top', 'value': 'top' },
            { 'name': 'ic-position-hidden', 'value': 'hidden' }
        ]);

        // label - align
        const labelAlignProperty = new ZSwitchButtonProperty('labelAlign', 'label.align', this.target.labelAlign, [
            { 'name': 'ic-align-left', 'value': 'left' },
            { 'name': 'ic-align-center', 'value': 'center' },
            { 'name': 'ic-align-right', 'value': 'right' }
        ]);
        labelAlignProperty.columnWidth = '6';

        // label - fontOption
        const labelFontOption = [
            { 'name': 'ic-bold', 'value': 'bold'},
            { 'name': 'ic-italic', 'value': 'italic' },
            { 'name': 'ic-underline', 'value': 'underline' }
        ];
        const labelFontValue = labelFontOption.map((item) => {
            const method = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            return this.target['labelFontOption' + method] ? 'Y' : 'N';
        }).join('|');
        const labelFontOptionProperty = new ZToggleButtonProperty('labelFontOption', 'label.fontOption', labelFontValue, labelFontOption);
        labelFontOptionProperty.columnWidth = '6';

        // label - fontColor
        const labelFontColorProperty = new ZColorPickerProperty('labelFontColor', 'label.fontColor', this.target.labelFontColor)
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