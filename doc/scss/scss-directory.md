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
2. overflow(넘침)
3. float(흐름)
4. position(위치)
5. width/height(크기)
6. padding/margin(간격)
7. border(테두리)
8. background(배경)
9. color/font(글꼴)
10. animation
11. etc(기타)
```

* 1~6 (레이아웃) / 7~8 (테두리, 배경) / 9 (글꼴) / 10 (동작) / 11 (기타)
* ※ 네이버 css 선언 순서를 참고하여 아래와 같이 정의합니다.

## 약속어 목록

---

| 약속어   |          명칭           | 
|-------|:---------------------:|
| `gnb` | global navigation bar |
| `btn` |        button         |

## SCSS Directory 구조

---

### Directory
<ul>
    <li style="list-style-type:disc">bz-colors</li>
    <li><details close=""><summary><strong>Utils</strong></summary>
        <ul>
            <li><details close=""><summary>mixins</summary>
                <ul>
                    <li style="list-style-type:disc">_animation</li>
                    <li style="list-style-type:disc">_border</li>
                    <li style="list-style-type:disc">_divider</li>
                    <li style="list-style-type:disc">_elements</li>
                    <li style="list-style-type:disc">_image</li>
                    <li style="list-style-type:disc">_layout</li>
                    <li style="list-style-type:disc">_position</li>
                    <li style="list-style-type:disc">_scroll</li>
                    <li style="list-style-type:disc">_text</li>
                    <li style="list-style-type:disc">_functions</li>
                </ul>
            </details></li>
        </ul>
        <ul>
            <li><details close=""><summary>variables / 변수만</summary>
                <ul>
                    <li style="list-style-type:disc">_alignments</li>
                    <li style="list-style-type:disc">_columns</li>
                    <li style="list-style-type:disc">_fonts</li>
                    <li style="list-style-type:disc">_spacing</li>
                    <li style="list-style-type:disc">_variables</li>
                </ul>
            </details></li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Base</strong></summary>
        <ul>
            <li style="list-style-type:disc">_reset</li>
            <li style="list-style-type:disc">_common</li>
            <li style="list-style-type:disc">_flex</li>
            <li style="list-style-type:disc">_font</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Layout</strong></summary>
        <ul>
            <li style="list-style-type:disc">_wrapper</li>
            <li style="list-style-type:disc">_header</li>
            <li style="list-style-type:disc">_navigation</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Components</strong></summary>
        <ul>
            <li><details close=""><summary>Button</summary>
                <ul>
                    <li style="list-style-type:disc">Single</li>
                    <li style="list-style-type:disc">Group</li>
                </ul>
            </details></li>
        </ul>
        <ul>
            <li><details close=""><summary>Form</summary>
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
            <li style="list-style-type:disc">_alerts</li>
            <li style="list-style-type:disc">_avatar</li>
            <li style="list-style-type:disc">_elements</li>
            <li style="list-style-type:disc">_icons</li>
            <li style="list-style-type:disc">_popup</li>
            <li style="list-style-type:disc">_modal</li>
            <li style="list-style-type:disc">_reply</li>
            <li style="list-style-type:disc">_modal</li>
            <li style="list-style-type:disc">_paging</li>
            <li style="list-style-type:disc">_scrollbar</li>
            <li style="list-style-type:disc">_tag</li>
            <li style="list-style-type:disc">_thumbnail</li>
            <li style="list-style-type:disc">_tooltip</li>
            <li style="list-style-type:disc">_tree</li>
            <li style="list-style-type:disc">_validation</li>
        </ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Page</strong></summary>
        <ul><li><details close=""><summary>document</summary></details></li></ul>
        <ul><li><details close=""><summary>formDesigner</summary></details></li></ul>
        <ul><li><details close=""><summary>portal</summary></details></li></ul>
        <ul><li><details close=""><summary>process</summary></details></li></ul>
        <ul><li><details close=""><summary>report</summary></details></li></ul>
    </details></li>
</ul>
<ul>
    <li><details close=""><summary><strong>Themes</strong></summary>
        <ul><li><details close=""><summary>Default</summary></details></li></ul>
</details></li>
</ul>

### ※ 참고 링크
<p>
<a href="https://nuli.navercorp.com/data/convention/NHN_Coding_Conventions_for_Markup_Languages.pdf" target="_blank">NHN 코드 작성 규칙</a><br>
<a href="https://imagineu.tistory.com/23">7-1 패턴</a>
</p>
