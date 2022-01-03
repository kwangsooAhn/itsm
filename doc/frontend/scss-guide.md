# SCSS 작성 가이드


## 목차

---

1. [css 선언 순서](#css-선언-순서)
2. [약속어 목록](#약속어-목록)
3. [SCSS Directory 구조](#scss-directory-구조)

## css 선언 순서

---

```
1. display(표시)
    - flex
    - align-item / align-content
    - justify-content
2. overflow(넘침)
3. float(흐름)
4. position(위치)
    - top
    - right
    - bottom
    - left
5. width/height(크기)
    - width
    - height
    - min-width
    - min-height
6. margin/padding(간격)
7. border(테두리)
8. background(배경)
9. color/font(글꼴)
    - color
    - font
    - text
10. animation
11. etc(기타)
    - line-height
    - letter-spacing
    - opacity
    - z-index
```

* 1~6 (레이아웃) / 7~8 (테두리, 배경) / 9 (글꼴) / 10 (동작) / 11 (기타)
* ※ 네이버 css 선언 순서를 참고하여 작성하였습니다.

## 약속어 목록

---

| 약속어    |          명칭           | 
|--------|:---------------------:|
| `bg`   |      background       |
| `btn`  |        button         |
| `gnb`  | global navigation bar |
| `img`  |         image         |
| `nav`  |      navigation       |
| `tbl`  |         table         |

## SCSS Directory 구조

---

### Directory
<ul>
    <li style="list-style-type:disc">bz-colors</li>
    <li><details open=""><summary><strong>utils</strong></summary>
        <ul>
            <li><details open=""><summary>mixins</summary>
                <ul>
                    <li style="list-style-type:disc">_animation.scss</li>
                    <li style="list-style-type:disc">_border.scss</li>
                    <li style="list-style-type:disc">_component.scss</li>
                    <li style="list-style-type:disc">_divider.scss</li>
                    <li style="list-style-type:disc">_functions.scss</li>
                    <li style="list-style-type:disc">_image.scss</li>
                    <li style="list-style-type:disc">_layout.scss</li>
                    <li style="list-style-type:disc">_position.scss</li>
                    <li style="list-style-type:disc">_scroll.scss</li>
                    <li style="list-style-type:disc">_text.scss</li>
                </ul>
            </details></li>
        </ul>
        <ul>
            <li><details open=""><summary>variables</summary>
                <ul>
                    <li style="list-style-type:disc">_alignments.scss</li>
                    <li style="list-style-type:disc">_columns.scss</li>
                    <li style="list-style-type:disc">_flex.scss</li>
                    <li style="list-style-type:disc">_spacing.scss</li>
                    <li style="list-style-type:disc">_variables.scss</li>
                </ul>
            </details></li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details open=""><summary><strong>base</strong></summary>
        <ul>
            <li style="list-style-type:disc">_common.scss</li>
            <li style="list-style-type:disc">_font.scss</li>
            <li style="list-style-type:disc">_icons.scss</li>
            <li style="list-style-type:disc">_reset.scss</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details open=""><summary><strong>Layout</strong></summary>
        <ul>
            <li style="list-style-type:disc">_content.scss</li>
            <li style="list-style-type:disc">_footer.scss</li>
            <li style="list-style-type:disc">_header.scss</li>
            <li style="list-style-type:disc">_navigation.scss</li>
            <li style="list-style-type:disc">_wrapper.scss</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details open=""><summary><strong>components</strong></summary>
        <ul>
            <li style="list-style-type:disc">_alerts.scss</li>
            <li style="list-style-type:disc">_avatar.scss</li>
            <li style="list-style-type:disc">_button.scss</li>
            <li style="list-style-type:disc">_form.scss</li>
            <li style="list-style-type:disc">_modal.scss</li>
            <li style="list-style-type:disc">_paging.scss</li>
            <li style="list-style-type:disc">_popup.scss</li>
            <li style="list-style-type:disc">_reply.scss</li>
            <li style="list-style-type:disc">_scrollbar.scss</li>
            <li style="list-style-type:disc">_tag.scss</li>
            <li style="list-style-type:disc">_thumbnail.scss</li>
            <li style="list-style-type:disc">_tooltip.scss</li>
            <li style="list-style-type:disc">_tree.scss</li>
            <li style="list-style-type:disc">_validation.scss</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details open=""><summary><strong>page</strong></summary>
        <ul>
            <li style="list-style-type:disc">document.scss</li>
            <li style="list-style-type:disc">formDesigner.scss</li>
            <li style="list-style-type:disc">portal.scss</li>
            <li style="list-style-type:disc">process.scss</li>
            <li style="list-style-type:disc">report.scss</li>        
        </ul>
    </details></li>
</ul>
<ul>
    <li><details open=""><summary><strong>themes</strong></summary>
        <ul>
            <li style="list-style-type:disc">dark.scss</li>      
        </ul>
</details></li>
</ul>

### ※ 참고 링크
<p>
<a href="https://nuli.navercorp.com/data/convention/NHN_Coding_Conventions_for_Markup_Languages.pdf" target="_blank">NHN 코드 작성 규칙</a><br>
<a href="https://imagineu.tistory.com/23">7-1 패턴</a>
</p>
