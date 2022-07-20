/**
 * 공통 Alert
 *
 * @version 1.0
 */
const zAlert = {};

zAlert.makeAlertModal = function (alertType, message, callbackFunc) {
    return new modal({
        body: `<div class="alert-dialog"><div class="alert-body alert-icon-` + alertType + `">`
            + `<div class="alert-message">` + message + `</div></div></div>`,
        buttons: [{
            content: i18n.msg('common.btn.close'),
            classes: 'alert-button btn__text--box secondary',
            bindKey: 13, /* Enter */
            callback: function (modal) {
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
                modal.hide();
                modal.destroy();
            }
        }],
        close: {
            closable: false,
        }
    });
};

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
zAlert.info = function (message, callbackFunc) {
    const myModal = zAlert.makeAlertModal('info', message, callbackFunc);
    myModal.show();
};

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
zAlert.success = function (message, callbackFunc) {
    const myModal = zAlert.makeAlertModal('success', message, callbackFunc);
    myModal.show();
};

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
zAlert.warning = function (message, callbackFunc) {
    const myModal = zAlert.makeAlertModal('warning', message, callbackFunc);
    myModal.show();
};

/**
 * open error dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
zAlert.danger = function (message, callbackFunc) {
    const myModal = zAlert.makeAlertModal('danger', message, callbackFunc);
    myModal.show();
};

/**
 * open confirm with icon dialog.
 *
 * @param message message
 * @param okCallbackFunc ok 시 callback function
 * @param cancelCallbackFunc cancel 시 callback function
 */
zAlert.confirm = function (message, okCallbackFunc, cancelCallbackFunc) {
    const myModal = new modal({
        message: message,
        body: `<div class="alert-dialog"><div class="alert-body alert-icon-confirm">
            <div class="alert-message">` + message + `</div></div></div>`,
        buttons: [
            {
                content: i18n.msg('common.btn.check'),
                classes: 'alert-button btn__text--box secondary',
                bindKey: false, /* no key! */
                callback: function (modal) {
                    if (typeof okCallbackFunc === 'function') {
                        okCallbackFunc();
                    }
                    modal.hide();
                    return true;
                }
            },
            {
                content: i18n.msg('common.btn.cancel'),
                classes: 'alert-button btn__text--box secondary',
                bindKey: false, /* no key! */
                callback: function (modal) {
                    if (typeof cancelCallbackFunc === 'function') {
                        cancelCallbackFunc();
                    }
                    modal.hide();
                    return false;
                }
            }
        ],
        close: {
            closable: false,
        },
        onCreate: () => {
            setTimeout(function(){
                document.querySelector('.alert-button:last-child').focus();
            },100);
        }
    });
    myModal.show();
};
