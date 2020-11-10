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
    
    const iconPath = '/assets/media/icons/tree';

    let options = {};

    // 기본값 셋팅.
    let defaults = {
        view: '',                               // '': 전체, modal: 모달
        title: '',                              // 제목 (option)
        root: '',                               // 트리 최상위 부모
        rootLevel: 0,                           // 트리 취상위 레벨
        search: '',                             // 검색어
        text: 'code',                           // 노드 텍스트 값 (code, codeName, codeValue, ...)
        data: null,                             // 코드 데이터
        target: 'treeList',                     // 트리를 붙일 object id
        sessionKey: null,                       // 펼쳐진 트리 정보를 저장하는 세션 키
        backColor: '',                          // 배경색
        icons: [],                              // depth 별 아이콘 (순차적으로 해당 아이콘 적용)
        defaultIcon: iconPath + '/icon_folder_open.svg',  // 기본 아이콘
        leafIcon: '',                           // 마지막 node 의 아이콘
        selectedValue: '',                      // 선택된 값
        totalCount: false,                      // 전체 개수 표시여부
        expandTree: true,                       // 전체 펼치기
        classes: 'tree',
        buttons: [{
            content: 'Confirm',
            classes: 'default-line',
            bindKey: false,
            callback: function(modal) {
               if (saveSelectedNode()) {
                   modal.hide();
               }
            }
        }, {
            content: 'Cancel',
            classes: 'default-line',
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
     * 모달에서 선택한 노드 저장
     */
    function saveSelectedNode() {
        const selectedNode = document.querySelector('.node_selected');
        if(!selectedNode) {
            aliceJs.alert(i18n.msg('common.msg.dataSelect'));
            return false;
        }
        let callbackFunc = options.callbackFunc;
        if (typeof callbackFunc === 'function') {
            callbackFunc(selectedNode);
        }
        return true;
    }

    /**
     * Tree Object.
     *
     * @return {tree}
     */
    function createTree() {
        let tree = {
            name: 'tree',
            div: options.target,
            ulElement: null,
            childNodes: [],
            backcolor: options.backColor,
            selectedNode: null,
            rendered: false,

            /**
             * Node 생성.
             *
             * @param item 코드 데이터
             * @param p_expanded 펼침 on/off (boolean)
             * @param p_depth node 단계
             * @param p_parentNode 부모 node
             * @return {node}
             */
            createNode: function(item, p_expanded, p_depth, p_parentNode) {
                let v_tree = this;
                let node = {
                    id: item.code,
                    text: item[options.text],
                    parent: p_parentNode,
                    expanded : p_expanded,
                    childNodes : [],
                    elementLi: null,
                    depth: p_depth,
                    data: { // node 에 원하는 데이터를 추가한다.
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
                    createChildNode: function(item, p_expanded, p_depth) { return v_tree.createNode(item, p_expanded, p_depth, this); },
                    expandTree: function() { v_tree.expandTree(); }
                }

                if (this.rendered) {
                    if (p_parentNode === undefined) {
                        this.drawNode(this.ulElement,node);
                        this.adjustLines(this.ulElement,false);
                    }
                    else {
                        let v_ul = p_parentNode.elementLi.getElementsByTagName('ul')[0];
                        if (p_parentNode.childNodes.length === 0) {
                            if (p_parentNode.expanded) {
                                p_parentNode.elementLi.getElementsByTagName('ul')[0].style.display = 'block';
                                let v_img = p_parentNode.elementLi.getElementsByTagName('img')[0];
                                v_img.style.visibility = 'visible';
                                v_img.src = iconPath + '/icon_tree_collapse.svg';
                                v_img.id = 'toggle_off';
                            } else {
                                p_parentNode.elementLi.getElementsByTagName('ul')[0].style.display = 'none';
                                let v_img = p_parentNode.elementLi.getElementsByTagName('img')[0];
                                v_img.style.visibility = 'visible';
                                v_img.src = iconPath + '/icon_tree_expand.svg';
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

                // node 에 dataset 추가
                v_span.id = p_node.id;
                v_span.dataset['name'] = p_node.data.name;
                v_span.dataset['value'] = p_node.data.value;
                v_span.dataset['editable'] = p_node.data.editable;
                v_span.dataset['depth'] = p_node.depth;

                let v_exp_col = null;
                if (p_node.childNodes.length === 0) {
                    v_exp_col = createImgElement('toggle_off', 'exp_col', iconPath + '/icon_tree_collapse.svg');
                    v_exp_col.style.visibility = 'hidden';
                } else {
                    if (p_node.expanded) {
                        v_exp_col = createImgElement('toggle_off', 'exp_col', iconPath + '/icon_tree_collapse.svg');
                    } else {
                        v_exp_col = createImgElement('toggle_on', 'exp_col', iconPath + '/icon_tree_expand.svg');
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
                let v_a = createSimpleElement('label', null, null);
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
                    let img = p_node.elementLi.getElementsByTagName('img')[0];
                    p_node.expanded = true;

                    img.id = 'toggle_off';
                    img.src = iconPath + '/icon_tree_collapse.svg';
                    let elem_ul = img.parentElement.getElementsByTagName('ul')[0];
                    elem_ul.style.display = 'block';

                    if (this.nodeAfterOpenEvent !== undefined) {
                        this.nodeAfterOpenEvent(p_node);
                    }
                }
            },
            collapseNode: function(p_node) {
                if (p_node.childNodes.length > 0 && p_node.expanded === true) {
                    let img=p_node.elementLi.getElementsByTagName('img')[0];
                    p_node.expanded = false;
                    if (this.nodeBeforeCloseEvent !== undefined) {
                        this.nodeBeforeCloseEvent(p_node);
                    }
                    img.id = 'toggle_on';
                    img.src = iconPath + '/icon_tree_expand.svg';
                    let elem_ul = img.parentElement.getElementsByTagName('ul')[0];
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
                if (p_node !== null) {
                    let span = p_node.elementLi.getElementsByTagName('span')[0];
                    span.className = 'node_selected';
                    if (this.selectedNode !== null && this.selectedNode !== p_node)
                        this.selectedNode.elementLi.getElementsByTagName('span')[0].className = 'node';
                    this.selectedNode = p_node;
                }
            },
            removeNode: function(p_node) {
                let index = p_node.parent.childNodes.indexOf(p_node);
                if (p_node.elementLi.className === 'last' && index !== 0) {
                    p_node.parent.childNodes[index-1].elementLi.className += 'last';
                    p_node.parent.childNodes[index-1].elementLi.style.backgroundColor = this.backcolor;
                }
                p_node.elementLi.parentNode.removeChild(p_node.elementLi);
                p_node.parent.childNodes.splice(index, 1);

                if (p_node.parent.childNodes.length === 0) {
                    let v_img = p_node.parent.elementLi.getElementsByTagName('img')[0];
                    v_img.style.visibility = 'hidden';
                }
            },
            removeChildNodes: function(p_node) {
                if (p_node.childNodes.length>0) {
                    let v_ul = p_node.elementLi.getElementsByTagName('ul')[0];
                    let v_img = p_node.elementLi.getElementsByTagName('img')[0];
                    v_img.style.visibility = 'hidden';

                    p_node.childNodes = [];
                    v_ul.innerHTML = '';
                }
            },
            adjustLines: function(p_ul,p_recursive) {
                let tree = p_ul;
                let lists = [];
                if (tree.childNodes.length>0) {
                    lists = [ tree ];
                    if (p_recursive) {
                        for (var i = 0; i < tree.getElementsByTagName('ul').length; i++) {
                            var check_ul = tree.getElementsByTagName('ul')[i];
                            if ( check_ul.childNodes.length !== 0) {
                                lists[lists.length] = check_ul;
                            }
                        }
                    }
                }

                for (let i = 0; i < lists.length; i++) {
                    let item = lists[i].lastChild;
                    while (!item.tagName || item.tagName.toLowerCase() !== 'li') {
                        item = item.previousSibling;
                    }
                    item.className += 'last';
                    item.style.backgroundColor = this.backcolor;
                    item = item.previousSibling;
                    if (item !== null) {
                        if (item.tagName.toLowerCase() === 'li') {
                            item.className = '';
                            item.style.backgroundColor = 'transparent';
                        }
                    }
                }
            }
        }
        return tree;
    }

    //Create a HTML element specified by parameter 'p_type'
    function createSimpleElement(p_type, p_id, p_class) {
        let element = document.createElement(p_type);
        if (p_id !== undefined) {
            element.id = p_id;
        }
        if (p_class !== undefined) {
            element.className = p_class;
        }
        return element;
    }

    //Create img element
    function createImgElement(p_id, p_class, p_src) {
        let element = document.createElement('img');
        if (p_id !== undefined) {
            element.id = p_id;
        }
        if (p_class !== undefined) {
            element.className = p_class;
        }
        if (p_src !== undefined) {
            element.src = p_src;
        }
        return element;
    }

    /**
     * 코드 값으로 해당 node 자동 선택.
     *
     * @param node node
     * @param selectedNode 선택된 node
     * @return {*}
     */
    function getSelectNode(node, selectedNode) {
        if (selectedNode === null) {
            if (node.childNodes.length === 0) {
                if (options.selectedValue === node.id) {
                    selectedNode = node;
                }
            } else {
                for (let i = 0; i < node.childNodes.length; i++) {
                    selectedNode = getSelectNode(node.childNodes[i], selectedNode);
                }
            }
        }
        return selectedNode;
    }

    /**
     * Tree 생성.
     *
     * @return {tree}
     */
    function makeTree() {
        const tree = createTree();
        makeNode(tree);
        tree.drawTree();

        // 기존 값이 존재할 경우 자동 선택.
        if (options.selectedValue !== null && options.selectedValue !== '') {
            tree.selectNode(getSelectNode(tree, null));
        }
        return tree;
    }

    /**
     * 코드의 최상위 부모까지 부모 정보를 저장.
     *
     * @param code 코드
     * @param pArray 코드 배열
     * @return {*}
     */
    function getRecursiveParentCode(code, pArray) {
        options.data.forEach(function (item) {
           if (item.code === code) {
               if (item.pcode !== null) {
                   pArray.push(item.pcode);
                   getRecursiveParentCode(item.pcode, pArray);
               }
           }
        });
        return pArray;
    }

    /**
     * Node 생성.
     *
     * @param tree Tree
     */
    function makeNode(tree) {
        // sessionKey에 저장된 펼치진 노드 정보를 추출하여 node 생성시 펼침 여부 판단
        let expandObject = [];
        if (options.selectedValue !== null && options.selectedValue !== '') {
            expandObject = getRecursiveParentCode(options.selectedValue, expandObject);
            expandObject.reverse();
        } else {
            if (options.sessionKey !== null && sessionStorage.getItem(options.sessionKey) !== null) {
                expandObject = JSON.parse(sessionStorage.getItem(options.sessionKey));
            }
        }

        // Node 생성
        options.data.forEach(function (item) {
            if (item.level === options.rootLevel) {
                let expand = false;
                if (expandObject.length > 0 && expandObject.indexOf(item.code) > -1) {
                    expand = true;
                }
                let firstNode = tree.createNode(item, expand, 1, null);
                createChildNode(firstNode, item.level, expandObject, 2);
            }
        });
    }

    /**
     * 자식 Node 생성.
     *
     * @param node Node
     * @param level 데이터 레벨
     * @param expandObject 펼침 여부 (booelan)
     * @param depth Tree 단계
     */
    function createChildNode(node, level, expandObject, depth) {
        options.data.forEach(function (item) {
            if (node.id === item.pcode) {
                let expand = false;
                if (expandObject !== null && expandObject.indexOf(item.code) > -1) {
                    expand = true;
                }
                let newNode = node.createChildNode(item, expand, depth);
                createChildNode(newNode, level, expandObject, depth + 1);
            }
        });
    }

    /**
     * 펼치진 node의 key를 찾아 session 저장.
     *
     * @param sessionKey 저장할 세션 키
     */
    function setTreeExpandNode(sessionKey) {
        let expandCodes = [];
        document.querySelectorAll('#toggle_off').forEach(function(object){
            if (object.style.visibility !== 'hidden') {
                expandCodes.push(object.nextElementSibling.querySelector('label').textContent);
            }
        });
        sessionStorage.setItem(sessionKey, JSON.stringify(expandCodes));
    }

    /**
     * 선택된 node.
     *
     * @return {Element}
     */
    function getTreeSelectNode() {
        return document.querySelector('.node_selected');
    }

    /**
     * Load.
     *
     * @param userOptions 옵션
     */
    function load(userOptions) {

        // 버튼 다국어 처리
        defaults.buttons[0].content = i18n.msg('common.btn.check');
        defaults.buttons[1].content = i18n.msg('common.btn.close');

        // 옵션 셋팅
        options = Object.assign({}, defaults, userOptions);
        let selectedNode = options.selectedNode;

        // 모달일 경우 선택 모달 생성.
        let treeModal;
        if (options.view === 'modal') {
            treeModal = new Modal();
        }
        aliceJs.sendXhr({
            method: 'get',
            url: '/rest/codes?pCode=' + options.root + '&search=' + options.search,
            async: false,
            contentType: 'application/json; charset=utf-8',
            callbackFunc: function(xhr) {
                let responseJson = JSON.parse(xhr.responseText);
                let totalCount = 0;
                if (responseJson.length > 0) {
                    options.data = responseJson;
                    if (options.totalCount) {
                        totalCount = responseJson[0].totalCount;
                        document.querySelector('#totalCount').innerHTML = i18n.msg('common.label.count', totalCount);
                    }
                    let tree = makeTree();
                    // 트리 Node 클릭시 이벤트 처리
                    if (typeof selectedNode === 'function') {
                        let nodes = document.querySelector('#' + options.target).querySelectorAll('span');
                        nodes.forEach.call(nodes, function(node) {
                            node.addEventListener('click', function(e) {
                                selectedNode(this);
                            });
                        });
                    }
                    if (options.expandTree) {
                        tree.expandTree();
                    }
                } else {
                    document.querySelector('#' + options.target).innerHTML = '';
                    if (options.totalCount) {
                        document.querySelector('#totalCount').innerHTML = i18n.msg('common.label.count', 0);
                    }
                }
                if (options.sessionKey !== null && sessionStorage.getItem(options.sessionKey) !== null) {
                    sessionStorage.removeItem(options.sessionKey);
                }
            }
        }, true);

        if (options.view === 'modal') {
            treeModal.show();
            OverlayScrollbars(document.querySelector('#' + options.target), { className: 'scrollbar' });
        }
    }
    exports.load = load;
    exports.setTreeExpandNode = setTreeExpandNode;
    exports.getTreeSelectNode = getTreeSelectNode;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
