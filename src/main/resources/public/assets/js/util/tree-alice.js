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

    const imagePath = '/assets/media/icons/tree';

    let options = {};

    let defaults = {
        view: '',
        title: '',
        root: '',
        rootLevel: 1,
        data: null,
        target: 'treeList',                     // 트리를 붙일 object id
        sessionKey: null,
        backColor: '',
        icons: [],                              // depth별로 순차적으로 해당 아이콘 적용
        defaultIcon: imagePath + '/parent.png', // 기본 아이콘
        leafIcon: '',                           // 마지막 node의 아이콘
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
    function Modal() {
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
            const selectedNode = document.querySelector('.node_selected');
            if(!selectedNode) {
                aliceJs.alert(i18n.msg('common.msg.dataSelect'));
                return false;
            }
            let callbackFunc = options.callbackFunc;
            if (typeof callbackFunc === 'function') {
                callbackFunc(selectedNode);
            }
            this.hide();
        };

        this.select = function(e) { };

        this.create = function() {
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

            if (this.options.title instanceof Element || (typeof this.options.title === "string" && this.options.title !== '')) {
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
            list.id = options.target;
            container.appendChild(list);

            body.appendChild(container);
            dialog.appendChild(body);

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

    function createTree() {
        let tree = {
            name: 'tree',
            div: options.target,
            ulElement: null,
            childNodes: [],
            backcolor: options.backColor,
            selectedNode: null,
            rendered: false,
            createNode: function(item, p_expanded, p_depth, p_parentNode) {
                let v_tree = this;
                let node = {
                    id: item.code,                    text: item[options.text],
                    parent: p_parentNode,
                    expanded : p_expanded,
                    childNodes : [],
                    elementLi: null,
                    depth: p_depth,
                    data: {
                        name: item.codeName || '',
                        value: item.codeValue || '',
                        editable: item.editable || false
                    },
                    removeNode: function() { v_tree.removeNode(this); },
                    toggleNode: function(p_event) { v_tree.toggleNode(this); },
                    expandNode: function(p_event) { v_tree.expandNode(this); },
                    expandSubtree: function() { v_tree.expandSubtree(this); },
                    setText: function(p_text) { v_tree.setText(this, p_text); },
                    collapseNode: function() { v_tree.collapseNode(this); },
                    collapseSubtree: function() { v_tree.collapseSubtree(this); },
                    removeChildNodes: function() { v_tree.removeChildNodes(this); },
                    createChildNode: function(item, p_expanded, p_depth) { return v_tree.createNode(item, p_expanded, p_depth, this); }
                }

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
                            } else {
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

                if (p_parentNode === null) {
                    this.childNodes.push(node);
                    node.parent = this;
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
            drawNode: function(p_ulElement, p_node) {
                let v_tree = this;
                let v_icon = null;
                let v_icon_image = options.defaultIcon;
                if (options.icons[p_node.depth - 1] !== undefined) {
                    v_icon_image = options.icons[p_node.depth - 1];
                }
                if (p_node.childNodes.length === 0 && options.leafIcon !== '') {
                    v_icon_image = options.leafIcon;
                }
                v_icon = createImgElement(null, 'icon_tree', v_icon_image);

                let v_li = document.createElement('li');
                p_node.elementLi = v_li;

                let v_span = createSimpleElement('span',null,'node');

                v_span.id = p_node.id;
                v_span.dataset['name'] = p_node.data.name;
                v_span.dataset['value'] = p_node.data.value;
                v_span.dataset['editable'] = p_node.data.editable;
                v_span.dataset['depth'] = p_node.depth;

                let v_exp_col = null;
                if (p_node.childNodes.length === 0) {
                    v_exp_col = createImgElement('toggle_off', 'exp_col', imagePath + '/collapse.png');
                    v_exp_col.style.visibility = "hidden";
                } else {
                    if (p_node.expanded) {
                        v_exp_col = createImgElement('toggle_off', 'exp_col', imagePath + '/collapse.png');
                    } else {
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
            setText: function(p_node, p_text) {
                p_node.elementLi.getElementsByTagName('span')[0].lastChild.innerHTML = p_text;
                p_node.text = p_text;
            },
            /*getNode: function(id) {
                for (let i = 0; i < this.childNodes.length; i++) {
                    console.log(this.childNodes[i]);
                    if (this.childNodes[i].id === id) {
                        return this.childNodes[i];
                    }
                    if (this.childNodes[i].childNodes.length > 0) {

                    }
                }
                //return node;
            },*/
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
                let span = p_node.elementLi.getElementsByTagName("span")[0];
                span.className = 'node_selected';
                if (this.selectedNode!=null && this.selectedNode !== p_node)
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
        return tree;
    }

    function makeTree() {
        const tree = createTree();
        makeNode(tree);
        tree.drawTree();


        /*let node = tree.getNode('numbering.pattern.format');
        console.log(node);
        tree.expandSubtree(node);*/
        /*let node = document.querySelector('#download').parentNode;
        console.log(node);
        tree.expandSubtree(node);*/

        return tree;
    }

    function loopNode(code, pArray) {
        //해당 값의 부모 값을 모두 찾는다.
        // 선택된 값으로 부모의 키를 모두 찾아서 역방향으로 임시 sessionkey 에 담아서 만들때 오픈한다.
        options.data.forEach(function (item) {
            if (code === item.code) {
                pArray.push(item.pcode);
            }
            console.log(item.pcode);
            //if (item.pcode != null) {
                //loopNode(item.pcode, pArray);
            //}
        });
        return pArray;
    }

    function makeNode(tree) {
        let expandObject = null;
        if (options.sessionKey != null && sessionStorage.getItem(options.sessionKey) != null) {
            expandObject = JSON.parse(sessionStorage.getItem(options.sessionKey));
        }

        /*let pArray = [];
        let a = loopNode('numbering.pattern.format', pArray);
        console.log(a);
        console.log(pArray);*/


        options.data.forEach(function (item) {
            if (item.level === options.rootLevel) {
                let expand = false;
                if (expandObject != null && expandObject.indexOf(item.code) > -1) {
                    expand = true;
                }
                let firstNode = tree.createNode(item, expand, 1, null);
                createChildNode(firstNode, item.level, expandObject, 2);
            }
        });
    }

    function createChildNode(node, level, expandObject, depth) {
        options.data.forEach(function (item) {
            if (node.id === item.pcode) {
                let expand = false;
                if (expandObject != null && expandObject.indexOf(item.code) > -1) {
                    expand = true;
                }
                let newNode = node.createChildNode(item, expand, depth);
                createChildNode(newNode, level, expandObject, depth + 1);
            }
        });
    }

    ////////////////////////////////////////////////

    function selectNode() {
        if (options.selectedValue !== null && options.selectedValue !== '') {

        }
    }

    /**
     * init.
     *
     * @param userOptions 옵션
     */
    function init(userOptions) {

        // 버튼 다국어 처리
        defaults.buttons[0].content = i18n.msg('common.btn.check');
        defaults.buttons[1].content = i18n.msg('common.btn.close');

        // 옵션 셋팅
        options = Object.assign({}, defaults, userOptions);
        let selectedNode = options.selectedNode;

        let treeModal;
        if (options.view === 'modal') {
            treeModal = new Modal();
        }
        aliceJs.sendXhr({
            method: 'get',
            url: '/rest/codes?pCode=' + options.root,
            async: false,
            callbackFunc: function(xhr) {
                let responseJson = JSON.parse(xhr.responseText);
                if (responseJson.length > 0) {
                    options.data = responseJson;
                    let tree = makeTree();
                    if (typeof selectedNode === 'function') {
                        let nodes = document.querySelector('#' + options.target).querySelectorAll('span');
                        nodes.forEach.call(nodes, function(node) {
                            node.addEventListener('click', function(e) {
                                selectedNode(this);
                            });
                        });
                    }
                } else {
                    document.querySelector('#' + options.target).innerHTML = '';
                }
            },
            contentType: 'application/json; charset=utf-8'
        }, true);

        if (options.view === 'modal') {
            treeModal.show();
            OverlayScrollbars(document.querySelector('#' + options.target), { className: 'scrollbar' });
        }
    }
    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
