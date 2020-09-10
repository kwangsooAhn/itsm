/**
 * tree modal library
 * 
 * @author phc
 * @version 1.0
 */

(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.tree = global.tree || {})));
}(this, (function (exports) {
    'use strict';

    let defaults = {
        title: '',
        rootLevel: 1,
        images: [],
        data: null,
        target: null,
        sessionKey: null,
        backColor: '',
        buttons: [{
            content: 'Confirm',
            classes: 'tree-modal-button-default',
            bindKey: false,
            callback: function(modal) {
                modal.save();
            }
        }, {
            content: 'Cancel',
            classes: 'tree-modal-button-default',
            bindKey: false,
            callback: function(modal) {
                modal.hide();
            }
        }],
        close: {
            location: 'in',
            bindKey: 27,
            callback: function(modal) {
                modal.hide();
            }
        },
        onShow: function(modal) {},
        onHide: function(modal) {
            modal.destroy();
        },
        onCreate: function(modal) {},
        onDestroy: function(modal) {}
    };

    /**
     * 특정 클래스 목록 조회
     * @param el 대상객체
     */
    function getClasses(el) {
        return el.className.split(' ').filter(function(c) {
            return c.length > 0;
        });
    }

    /**
     * 특정 클래스 존재 여부 체크
     * @param el 대상객체
     * @param className 클래스명
     */
    function hasClass(el, className) {
        return getClasses(el).indexOf(className) >= 0;
    }

    /**
     * 특정 클래스 추가
     * @param el 대상객체
     * @param className 클래스명
     */
    function addClass(el, className) {
        if (!hasClass(el, className)) {
            el.className += ' ' + className;
        }
    }

    /**
     * 특정 클래스 삭제
     * @param el 대상객체
     * @param className 클래스명
     */
    function removeClass(el, className) {
        if (hasClass(el, className)) {
            let classes = getClasses(el);
            classes.splice(classes.indexOf(className), 1);
            el.className = classes.join(' ');
        }
    }

    /**
     * 모달 생성 (gModal.js 참고)
     * https://github.com/jsanahuja/thumbnail-modal
     * 
     * @param options 옵션
     */
    function Modal(options) {
        this.id = Math.random().toString(36).substr(2);
        this.options = options;
        this.display = false;
        this.bindings = {};

        this.bind = function(key, callback) {
            if (typeof this.bindings[key] !== 'undefined') {
                console.warn('tree-modal: Tried to bind the key ' + key + ' twice. Overriding...');
            }
            this.bindings[key] = callback;
        };

        this.addKeyListener = function() {
            window.currentModal = this;
            window.addEventListener('keydown', this.onKeyPress, false);
        };

        this.removeKeyListener = function() {
            window.currentModal = undefined;
            window.removeEventListener('keydown', this.onKeyPress, false);
        };

        this.onKeyPress = function(e) {
            /*if ( typeof window.currentModal !== 'undefined' &&  window.currentModal instanceof Modal) {
                let _that = window.currentModal;
                if (!_that.display) return;
                let keyCode = e.keyCode || e.which;
                let keys = Object.keys(_that.bindings);
                for (let i = 0; i < keys.length; i++) {
                    if (parseInt(keys[i]) === keyCode) {
                        e.preventDefault();
                        e.stopPropagation();
                        _that.bindings[keys[i]](_that);
                        return false;
                    }
                }
            }*/
        };

        this.show = function() {
            if (typeof this.wrapper !== 'undefined') {
                addClass(this.wrapper, 'tree-modal-active');
                addClass(document.body, 'tree-modal-active');
                this.display = true;
                this.options.onShow(this);
            }
        };

        this.hide = function() {
            if (typeof this.wrapper !== 'undefined') {
                removeClass(this.wrapper, 'tree-modal-active');
                removeClass(document.body, 'tree-modal-active');
                this.display = false;
                this.options.onHide(this);
            }
        };

        this.save = function() {

        };

        this.select = function(e) {
        };

        this.create = function() { // 모달 draw
            if (typeof this.wrapper !== 'undefined') { return; }
            let backdrop, dialog;

            this.wrapper = document.createElement('div');
            this.wrapper.className = 'tree-modal-wrapper';
            this.wrapper.id = 'tree-modal-wrapper-' + this.id;

            backdrop = document.createElement('div');
            backdrop.className = 'tree-modal-backdrop';

            dialog = document.createElement('div');
            dialog.className = 'tree-modal-dialog';

            const body = document.createElement('div');
            body.className = 'tree-modal-body';

            const container = document.createElement('div');
            container.className = 'tree-container';

            if (this.options.title instanceof Element || (typeof this.options.title === "string" && this.options.title != '')) {
                const title = document.createElement('div');
                title.className = 'tree-title';
                if (this.options.title instanceof Element) {
                    title.appendChild(this.options.title);
                } else {
                    title.innerHTML = this.options.title;
                }
                container.appendChild(title);
            }

            const list = document.createElement('div');
            list.className = 'tree-list';
            list.id = 'treeList';
            container.appendChild(list);

            body.appendChild(container);
            dialog.appendChild(body);
            
            // 하단 버튼
            if (this.options.buttons.length > 0) {
                const buttons = document.createElement('div');
                buttons.className = 'tree-modal-buttons';

                for (let i = 0; i < this.options.buttons.length; i++) {
                    const button = document.createElement('a');
                    button.setAttribute('href', 'javascript:void(0);');

                    button.className = 'tree-modal-button';
                    if (typeof this.options.buttons[i].classes !== 'undefined') {
                        button.className += ' ' + this.options.buttons[i].classes;
                    }

                    if (typeof this.options.buttons[i].content !== 'undefined') {
                        button.innerHTML = this.options.buttons[i].content;
                    }

                    if (typeof this.options.buttons[i].callback === 'undefined') {
                        this.options.buttons[i].callback = function(modal) {
                            modal.hide();
                        };
                    }

                    // button click
                    button.modal = this;
                    button.callback = this.options.buttons[i].callback;
                    button.onclick = function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        this.callback(this.modal);
                        return false;
                    };

                    // button key binding
                    if (typeof this.options.buttons[i].bindKey === 'number') {
                        this.bind(
                            this.options.buttons[i].bindKey,
                            this.options.buttons[i].callback
                        );
                    }

                    buttons.appendChild(button);
                }
                dialog.appendChild(buttons);
            }
            this.wrapper.appendChild(backdrop);
            this.wrapper.appendChild(dialog);
            document.body.appendChild(this.wrapper);
            this.addKeyListener();
            this.options.onCreate(this);
        };

        this.destroy = function() { // 모달 제거
            if (typeof this.wrapper !== 'undefined') {
                document.body.removeChild(this.wrapper);
                this.wrapper = undefined;
                this.removeKeyListener();
                this.options.onDestroy(this);
            }
        };

        this.create();
    }

    //////////////////////////////////////////////

    function createTree(p_div, p_backColor) {
        const imagePath = '/assets/media/icons/tree';
        let tree = {
            name: 'tree',
            div: p_div,
            ulElement: null,
            childNodes: [],
            backcolor: p_backColor,
            selectedNode: null,
            nodeCounter: 0,
            contextMenuDiv: null,
            rendered: false,
            createNode: function(p_text,p_expanded, p_icon, p_parentNode,p_tag,p_contextmenu) {
                let v_tree = this;
                let node = {
                    id: 'node_' + this.nodeCounter,
                    text: p_text,
                    icon: p_icon,
                    parent: p_parentNode,
                    expanded : p_expanded,
                    childNodes : [],
                    tag : p_tag,
                    contextMenu: p_contextmenu,
                    elementLi: null,
                    removeNode: function() { v_tree.removeNode(this); },
                    toggleNode: function(p_event) { v_tree.toggleNode(this); },
                    expandNode: function(p_event) { v_tree.expandNode(this); },
                    expandSubtree: function() { v_tree.expandSubtree(this); },
                    setText: function(p_text) { v_tree.setText(this,p_text); },
                    collapseNode: function() { v_tree.collapseNode(this); },
                    collapseSubtree: function() { v_tree.collapseSubtree(this); },
                    removeChildNodes: function() { v_tree.removeChildNodes(this); },
                    createChildNode: function(p_text,p_expanded,p_icon,p_tag,p_contextmenu) { return v_tree.createNode(p_text,p_expanded,p_icon,this,p_tag,p_contextmenu); }
                }
                this.nodeCounter++;

                if (this.rendered) {
                    if (p_parentNode === undefined) {
                        this.drawNode(this.ulElement,node);
                        this.adjustLines(this.ulElement,false);
                    }
                    else {
                        let v_ul = p_parentNode.elementLi.getElementsByTagName("ul")[0];
                        if (p_parentNode.childNodes.length === 0) {
                            if (p_parentNode.expanded) {
                                p_parentNode.elementLi.getElementsByTagName("ul")[0].style.display = 'block';
                                let v_img = p_parentNode.elementLi.getElementsByTagName("img")[0];
                                v_img.style.visibility = "visible";
                                v_img.src = imagePath + '/collapse.png';
                                v_img.id = 'toggle_off';
                            }
                            else {
                                p_parentNode.elementLi.getElementsByTagName("ul")[0].style.display = 'none';
                                let v_img = p_parentNode.elementLi.getElementsByTagName("img")[0];
                                v_img.style.visibility = "visible";
                                v_img.src = imagePath + '/expand.png';
                                v_img.id = 'toggle_on';
                            }
                        }
                        this.drawNode(v_ul,node);
                        this.adjustLines(v_ul,false);
                    }
                }

                if (p_parentNode === undefined) {
                    this.childNodes.push(node);
                    node.parent=this;
                } else {
                    p_parentNode.childNodes.push(node);
                }
                return node;
            },
            drawTree: function() {
                this.rendered = true;
                let div_tree = document.getElementById(this.div);
                div_tree.innerHTML = '';

                let ulElement = createSimpleElement('ul',this.name,'tree');
                this.ulElement = ulElement;

                for (let i = 0; i < this.childNodes.length; i++) {
                    this.drawNode(ulElement,this.childNodes[i]);
                }
                div_tree.appendChild(ulElement);
                this.adjustLines(document.getElementById(this.name),true);

            },
            drawNode: function(p_ulElement,p_node) {
                let v_tree = this;
                let v_icon = null;
                if (p_node.icon !== null) {
                    v_icon = createImgElement(null,'icon_tree',p_node.icon);
                }
                let v_li = document.createElement('li');
                p_node.elementLi = v_li;

                let v_span = createSimpleElement('span',null,'node');
                let v_exp_col = null;
                if (p_node.childNodes.length === 0) {
                    v_exp_col = createImgElement('toggle_off', 'exp_col', imagePath + '/collapse.png');
                    v_exp_col.style.visibility = "hidden";
                }
                else {
                    if (p_node.expanded) {
                        v_exp_col = createImgElement('toggle_off', 'exp_col', imagePath + '/collapse.png');
                    }
                    else {
                        v_exp_col = createImgElement('toggle_on', 'exp_col', imagePath + '/expand.png');
                    }
                }

                v_span.ondblclick = function() {
                    v_tree.doubleClickNode(p_node);
                };

                v_exp_col.onclick = function() {
                    v_tree.toggleNode(p_node);
                };

                v_span.onclick = function() {
                    v_tree.selectNode(p_node);
                };

                v_span.oncontextmenu = function(e) {
                    v_tree.selectNode(p_node);
                    v_tree.nodeContextMenu(e, p_node);
                };

                if (v_icon !== undefined) {
                    v_span.appendChild(v_icon);
                }
                let v_a = createSimpleElement('a', null, null);
                v_a.innerHTML = p_node.text;
                v_span.appendChild(v_a);
                v_li.appendChild(v_exp_col);
                v_li.appendChild(v_span);
                p_ulElement.appendChild(v_li);

                let v_ul = createSimpleElement('ul', 'ul_' + p_node.id, null);
                v_li.appendChild(v_ul);

                if (p_node.childNodes.length > 0) {
                    if (!p_node.expanded) {
                        v_ul.style.display = 'none';
                    }
                    for (let i = 0; i < p_node.childNodes.length; i++) {
                        this.drawNode(v_ul,p_node.childNodes[i]);
                    }
                }
            },
            setText: function(p_node,p_text) {
                p_node.elementLi.getElementsByTagName('span')[0].lastChild.innerHTML = p_text;
                p_node.text = p_text;
            },
            expandTree: function() {
                for (let i = 0; i < this.childNodes.length; i++) {
                    if (this.childNodes[i].childNodes.length > 0) {
                        this.expandSubtree(this.childNodes[i]);
                    }
                }
            },
            expandSubtree: function(p_node) {
                this.expandNode(p_node);
                for (let i = 0; i < p_node.childNodes.length; i++) {
                    if (p_node.childNodes[i].childNodes.length > 0) {
                        this.expandSubtree(p_node.childNodes[i]);
                    }
                }
            },
            collapseTree: function() {
                for (let i=0; i<this.childNodes.length; i++) {
                    if (this.childNodes[i].childNodes.length>0) {
                        this.collapseSubtree(this.childNodes[i]);
                    }
                }
            },
            collapseSubtree: function(p_node) {
                this.collapseNode(p_node);
                for (let i=0; i<p_node.childNodes.length; i++) {
                    if (p_node.childNodes[i].childNodes.length>0) {
                        this.collapseSubtree(p_node.childNodes[i]);
                    }
                }
            },
            expandNode: function(p_node) {
                if (p_node.childNodes.length > 0 && p_node.expanded === false) {
                    if (this.nodeBeforeOpenEvent !== undefined) {
                        this.nodeBeforeOpenEvent(p_node);
                    }
                    let img = p_node.elementLi.getElementsByTagName("img")[0];
                    p_node.expanded = true;

                    img.id="toggle_off";
                    img.src = imagePath + '/collapse.png';
                    let elem_ul = img.parentElement.getElementsByTagName("ul")[0];
                    elem_ul.style.display = 'block';

                    if (this.nodeAfterOpenEvent !== undefined) {
                        this.nodeAfterOpenEvent(p_node);
                    }
                }
            },
            collapseNode: function(p_node) {
                if (p_node.childNodes.length > 0 && p_node.expanded === true) {
                    let img=p_node.elementLi.getElementsByTagName("img")[0];
                    p_node.expanded = false;
                    if (this.nodeBeforeCloseEvent !== undefined) {
                        this.nodeBeforeCloseEvent(p_node);
                    }
                    img.id="toggle_on";
                    img.src = imagePath + '/expand.png';
                    let elem_ul = img.parentElement.getElementsByTagName("ul")[0];
                    elem_ul.style.display = 'none';

                }
            },
            toggleNode: function(p_node) {
                if (p_node.childNodes.length>0) {
                    if (p_node.expanded) {
                        p_node.collapseNode();
                    } else {
                        p_node.expandNode();
                    }
                }
            },
            doubleClickNode: function(p_node) {
                this.toggleNode(p_node);
            },
            selectNode: function(p_node) {
                var span = p_node.elementLi.getElementsByTagName("span")[0];
                span.className = 'node_selected';
                if (this.selectedNode!=null && this.selectedNode!=p_node)
                    this.selectedNode.elementLi.getElementsByTagName("span")[0].className = 'node';
                this.selectedNode = p_node;
            },
            removeNode: function(p_node) {
                let index = p_node.parent.childNodes.indexOf(p_node);
                if (p_node.elementLi.className === "last" && index !== 0) {
                    p_node.parent.childNodes[index-1].elementLi.className += "last";
                    p_node.parent.childNodes[index-1].elementLi.style.backgroundColor = this.backcolor;
                }
                p_node.elementLi.parentNode.removeChild(p_node.elementLi);
                p_node.parent.childNodes.splice(index, 1);

                if (p_node.parent.childNodes.length === 0) {
                    let v_img = p_node.parent.elementLi.getElementsByTagName("img")[0];
                    v_img.style.visibility = "hidden";
                }
            },
            removeChildNodes: function(p_node) {
                if (p_node.childNodes.length>0) {
                    let v_ul = p_node.elementLi.getElementsByTagName("ul")[0];
                    let v_img = p_node.elementLi.getElementsByTagName("img")[0];
                    v_img.style.visibility = "hidden";

                    p_node.childNodes = [];
                    v_ul.innerHTML = "";
                }
            },
            adjustLines: function(p_ul,p_recursive) {
                let tree = p_ul;
                let lists = [];
                if (tree.childNodes.length>0) {
                    lists = [ tree ];
                    if (p_recursive) {
                        for (var i = 0; i < tree.getElementsByTagName("ul").length; i++) {
                            var check_ul = tree.getElementsByTagName("ul")[i];
                            if ( check_ul.childNodes.length !== 0) {
                                lists[lists.length] = check_ul;
                            }
                        }
                    }
                }

                for (let i = 0; i < lists.length; i++) {
                    let item = lists[i].lastChild;
                    while (!item.tagName || item.tagName.toLowerCase() !== "li") {
                        item = item.previousSibling;
                    }
                    item.className += "last";
                    item.style.backgroundColor = this.backcolor;
                    item = item.previousSibling;
                    if (item !== null) {
                        if (item.tagName.toLowerCase() === "li") {
                            item.className = "";
                            item.style.backgroundColor = 'transparent';
                        }
                    }
                }
            }
        }
    }

    function makeTree(options) {
    //function makeTree(object, element, sessionKey, level, backColor) {
        const tree = createTree(options.target.id, options.backColor);
        makeNode(tree, options);
        //createNode(tree, object, sessionKey, level);
        tree.drawTree();
        return tree;
    }

    function makeNode(tree, options) {
    //function createNode(tree, object, sessionKey, level) {
        let expandObject = null;
        if (options.sessionKey != null && sessionStorage.getItem(options.sessionKey) != null) {
            expandObject = JSON.parse(sessionStorage.getItem(options.sessionKey));
        }
        options.object.forEach(function (item) {
            if (item.level === options.rootLevel) {
                let expand = false;
                if (expandObject != null && expandObject.indexOf(item.code) > -1) {
                    expand = true;
                }
                let firstNode = tree.createNode(item.code, expand, aliceJs.treeIconPath + '/parent.png', null, null, null);
                createChildNode(firstNode, options.object, item.level, expandObject);
            }
        });

        //aliceJs.leafChildNodeConfig(tree.childNodes);
    }

    function createChildNode(node, object, level, expandObject) {
        object.forEach(function (item) {
            if (node.text === item.pcode) {
                let expand = false;
                if (expandObject != null && expandObject.indexOf(item.code) > -1) {
                    expand = true;
                }
                let newNode = node.createChildNode(item.code, expand, aliceJs.treeDefaultIcon, null, null);
                createChildNode(newNode, object, level, expandObject);
            }
        });
    }

    ////////////////////////////////////////////////


    /**
     * init.
     *
     * @param userOptions 옵션
     */
    function init(userOptions) {

        // 버튼 다국어 처리
        defaults.buttons[0].content = i18n.msg('common.btn.check');
        defaults.buttons[1].content = i18n.msg('common.btn.close');
        let options = Object.assign({}, defaults, userOptions);

        let treeModal = new Modal(options);
        const pCode = 'department.group';
        aliceJs.sendXhr({
            method: 'get',
            url: '/rest/codes?pCode=' + pCode,
            callbackFunc: function(xhr) {
                let responseJson = JSON.parse(xhr.responseText);
                console.log(responseJson)
                if (responseJson.length > 0) {
                    options.data = responseJson;
                    //let tree = makeTree(options);
                    let tree = aliceJs.createTree(responseJson, document.querySelector('#treeList'), null, options.rootLevel);
                    let nodes = document.querySelector('#treeList').querySelectorAll('span');
                    nodes.forEach.call(nodes, function(node) {
                        node.addEventListener('click', function(e) {
                            console.log('click!!!');
                        });
                    });
                } else {
                    document.querySelector('#treeList').innerHTML = '';
                }
            },
            contentType: 'application/json; charset=utf-8'
        });
        treeModal.show();
        OverlayScrollbars(document.querySelector('.tree-list'), { className: 'scrollbar' });
    }
    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
