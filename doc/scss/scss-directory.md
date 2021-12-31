# SCSS 리팩토링


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
* ※ 네이버 css 선언 순서를 참고하여 아래와 같이 정의합니다.

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
    <li><details close=""><summary><strong>Utils</strong></summary>
        <ul>
            <li><details close=""><summary>mixins</summary>
                <ul>
                    <li style="list-style-type:disc">_animation.scss</li>
                    <li style="list-style-type:disc">_border.scss</li>
                    <li style="list-style-type:disc">_divider.scss</li>
                    <li style="list-style-type:disc">_component.scss</li>
                    <li style="list-style-type:disc">_image.scss</li>
                    <li style="list-style-type:disc">_layout.scss</li>
                    <li style="list-style-type:disc">_position.scss</li>
                    <li style="list-style-type:disc">_scroll.scss</li>
                    <li style="list-style-type:disc">_text.scss</li>
                    <li style="list-style-type:disc">_functions.scss</li>
                </ul>
            </details></li>
        </ul>
        <ul>
            <li><details close=""><summary>variables / 변수만</summary>
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
    <li><details close=""><summary><strong>Base</strong></summary>
        <ul>
            <li style="list-style-type:disc">_reset.scss</li>
            <li style="list-style-type:disc">_common.scss</li>
            <li style="list-style-type:disc">_font.scss</li>
            <li style="list-style-type:disc">_icons.scss</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Layout</strong></summary>
        <ul>
            <li style="list-style-type:disc">_wrapper.scss</li>
            <li style="list-style-type:disc">_header.scss</li>
            <li style="list-style-type:disc">_footer.scss</li>
            <li style="list-style-type:disc">_navigation.scss</li>
            <li><details close=""><summary>_content.scss</summary>
                <ul>
                    <li style="list-style-type:disc">search</li>
                    <li style="list-style-type:disc">edit / view</li>
                </ul>
            </details></li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Components</strong></summary>
        <ul>
            <li><details close=""><summary>Button.scss</summary>
                <ul>
                    <li style="list-style-type:disc">Icon</li>
                    <li style="list-style-type:disc">Single</li>
                    <li style="list-style-type:disc">Group</li>
                </ul>
            </details></li>
        </ul>
        <ul>
            <li><details close=""><summary>Form.scss</summary>
                <ul>
                    <li style="list-style-type:disc">Text Field</li>
                    <li style="list-style-type:disc">Dropdown / Select</li>
                    <li style="list-style-type:disc">Textarea</li>
                    <li style="list-style-type:disc">File uploader</li>
                    <li style="list-style-type:disc">Sliders</li>
                    <li style="list-style-type:disc">Switches</li>
                    <li style="list-style-type:disc">Checkbox</li>
                    <li style="list-style-type:disc">Radio</li>
                    <li style="list-style-type:disc">Color</li>
                    <li style="list-style-type:disc">Date / Time
                        <ul>
                            <li style="list-style-type:circle">Date</li>
                            <li style="list-style-type:circle">Time</li>
                            <li style="list-style-type:circle">Date Time</li>
                        </ul>
                    </li>
                </ul>
            </details></li>
        </ul>
        <ul>
            <li style="list-style-type:disc">_alerts.scss</li>
            <li style="list-style-type:disc">_avatar.scss</li>
            <li style="list-style-type:disc">_popup.scss</li>
            <li style="list-style-type:disc">_modal.scss</li>
            <li style="list-style-type:disc">_reply.scss</li>
            <li style="list-style-type:disc">_paging.scss</li>
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
    <li><details close=""><summary><strong>Page</strong></summary>
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
    <li><details close=""><summary><strong>Themes</strong></summary>
        <ul><li><details close=""><summary>Dark</summary></details></li></ul>
</details></li>
</ul>

### ※ 참고 링크
<p>
<a href="https://nuli.navercorp.com/data/convention/NHN_Coding_Conventions_for_Markup_Languages.pdf" target="_blank">NHN 코드 작성 규칙</a><br>
<a href="https://imagineu.tistory.com/23">7-1 패턴</a>
</p>
