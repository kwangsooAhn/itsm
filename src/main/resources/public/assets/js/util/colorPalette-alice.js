const colorPalette = (function() {
    'use strict';

    const paletteColors = [
        "#1abc9c"
        , "#2ecc71"
        , "#3498db"
        , "#9b59b6"
        , "#34495e"
        , "#16a085"
        , "#27ae60"
        , "#2980b9"
        , "#8e44ad"
        , "#2c3e50"
        , "#f1c40f"
        , "#e67e22"
        , "#e74c3c"
        , "#ecf0f1"
        , "#95a5a6"
        , "#f39c12"
        , "#d35400"
        , "#c0392b"
        , "#bdc3c7"
        , "#7f8c8d"
    ];

    /**
     * Color Palette 사용을 위한 컨테이너 생성 및 초기화 처리.
     *
     * @param targetId Target element id
     * @param options color palette options
     */
    function initPalette(targetId) {
        let targetElement = document.getElementById(targetId);

        let pk = new Piklor("#label-color-colorPalette", paletteColors, {
                    open: "#label-color-value"
                });

        let wrapperEl = pk.getElm("#label-color-value");

            pk.colorChosen(function (col) {
                wrapperEl.style.backgroundColor = col;
            });
        };


        // // set options
        // const colorPaletteId = 'picker-' + targetId;
        // options.value = targetElement.value;
        // options.el = '#' + pickerId;
        // options.inputEl = targetElement;
        //
        // // create target element container
        // let targetContainer = document.createElement('div');
        // targetContainer.style.display = 'inline-block';
        // targetElement.parentElement.insertBefore(targetContainer, targetElement.nextSibling);
        // targetElement.parentElement.removeChild(targetElement);
        // targetContainer.appendChild(targetElement);
        //
        // // create picker container
        // let pickerContainer = document.createElement('div');
        // pickerContainer.id = pickerId;
        // pickerContainer.className = 'picker';
        // targetContainer.appendChild(pickerContainer);
        //
        // // color palette initiation
        // let colorPalette = new Piklor(".color-picker", paletteColors,
        //     {open: ".picker-wrapper .btn"})
        // // let picker = new WindowDatePicker(options);
        // return colorPalette;


    return {
        initPalette: initPalette
    }
})();