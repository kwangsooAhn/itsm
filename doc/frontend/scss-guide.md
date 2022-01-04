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

📄 bz-colors.scss<sup>[1](#footnote_1)</sup>  
📁 <strong>utils</strong><sup>[2](#footnote_2)</sup>  
　　📁 mixins  
　　　　📄 _animation.scss  
　　　　📄 _border.scss  
　　　　📄 _component.scss  
　　　　📄 _divider.scss  
　　　　📄 _functions.scss  
　　　　📄 _image.scss  
　　　　📄 _layout.scss  
　　　　📄 _position.scss  
　　　　📄 _scroll.scss  
　　　　📄 _text.scss  
　　📁 variables  
　　　　📄 _alignments.scss  
　　　　📄 _columns.scss  
　　　　📄 _flex.scss  
　　　　📄 _spacing.scss  
　　　　📄 _variables.scss  
📁 <strong>base</strong><sup>[3](#footnote_3)</sup>  
　　📄 _common.scss  
　　📄 _font.scss  
　　📄 _icons.scss  
　　📄 _reset.scss  
📁 <strong>layout</strong><sup>[4](#footnote_4)</sup>  
　　📄 _content.scss  
　　📄 _footer.scss  
　　📄 _header.scss  
　　📄 _navigation.scss  
　　📄 _wrapper.scss  
📁 <strong>components</strong><sup>[5](#footnote_5)</sup>  
　　📄 _alerts.scss  
　　📄 _avatar.scss  
　　📄 _button.scss  
　　📄 _form.scss  
　　📄 _modal.scss  
　　📄 _paging.scss  
　　📄 _popup.scss  
　　📄 _reply.scss  
　　📄 _scrollbar.scss  
　　📄 _tag.scss  
　　📄 _thumbnail.scss  
　　📄 _tooltip.scss  
　　📄 _tree.scss  
　　📄 _validation.scss  
📁 <strong>page</strong><sup>[6](#footnote_6)</sup>  
　　📄 document.scss  
　　📄 formDesigner.scss  
　　📄 portal.scss  
　　📄 process.scss  
　　📄 report.scss  
📁 <strong>themes</strong><sup>[7](#footnote_7)</sup>  
　　📄 dark.scss

### ※ 참고 링크
<p>
<a href="https://nuli.navercorp.com/data/convention/NHN_Coding_Conventions_for_Markup_Languages.pdf" target="_blank">NHN 코드 작성 규칙</a><br>
<a href="https://imagineu.tistory.com/23">7-1 패턴</a>
</p>

---

#### 상세설명
<a id="footnote_1">1</a> : vender처럼 사용, 이 파일은 수정하지 않습니다.  
<a id="footnote_2">2</a> : 실제 스타일은 없고, 다른 폴더에 정의된 스타일을 도와주는 역할  
<a id="footnote_3">3</a> : 사이트 전반에 걸쳐서 재사용되는 스타일  
<a id="footnote_4">4</a> : 사이트 구조에 해당하는 레이아웃  
<a id="footnote_5">5</a> : 사이트 내에서 재사용가능한 작은 소형 레이아웃  
<a id="footnote_6">6</a> : 각 페이지에 사용되는 스타일  
<a id="footnote_7">7</a> : 테마에 따라 사용되는 스타일