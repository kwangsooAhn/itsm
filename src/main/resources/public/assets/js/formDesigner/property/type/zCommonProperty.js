/**
 * mapId, is topic, tag 등 공통 Property Class
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZInputBoxProperty from './zInputBoxProperty.js';
import ZSwitchProperty from './zSwitchProperty.js';
import ZTagProperty from './zTagProperty.js';
import ZClipboardProperty from './zClipboardProperty.js';
import ZGroupProperty from './zGroupProperty.js';
import ZSliderProperty from './zSliderProperty.js';

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

        return [
            new ZClipboardProperty('id', this.target.id),
            mapIdProperty,
            isTopicProperty,
            tagProperty,
            new ZGroupProperty('group.display')
                .addProperty(new ZSliderProperty('display.columnWidth', this.target.displayColumnWidth))
        ];
    }
}