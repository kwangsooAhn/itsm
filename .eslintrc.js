module.exports = {
    'env': {
        'browser': true,
        'es2021': true,
        'node': true,
    },
    'extends': 'eslint:recommended',
    'parserOptions': {
        'ecmaVersion': 2018,
        'sourceType': 'module',
        'ecmaFeatures': {
            'jsx': true
        }
    },
    'plugins': [
        'html'
    ],
    // 외부 라이브러리, 노드 모듈 제외
    'ignorePatterns': ['node_modules/', 'src/main/resources/public/assets/vendors/'],
    'rules': {
        'indent': ['error', 4, { 'SwitchCase': 1 }],
        'linebreak-style': 0,
        // 문자열은 홀따옴표(') / 백틱 허용
        'quotes': ['error', 'single', { 'allowTemplateLiterals': true }],
        // 코드 마지막에 세미콜른이 있게 formatting
        'semi': ['error', 'always'],
        // var 사용 안함
        'no-var': 'error',
        // 변수 선언, 배열 리터럴, 객체 리터럴, 함수 매개 변수 및 시퀀스에서 쉼표 앞뒤에 일관된 간격 적용
        'comma-spacing': ['error', { 'before': false, 'after': true }],
        // 글자 제한 
        'max-len': ['error', { 'code': 120 }],
        // 전역 객체 사용시 에러 표시 안함
        'no-undef': 'off',
        // 클래스는 대문자 시작
        'new-cap': ['error', { 'newIsCap': false }],
        // array = [] 로 사용
        'no-array-constructor': 'error',
        // new Object() 사용 안함
        'no-new-object': 'error',
        // new Function() 사용 안함
        'no-new-func': 'error',
        // arrow function
        'arrow-body-style': ['error', 'as-needed', { 'requireReturnForObjectLiteral': true }],
        // Primitive Wrapper 사용 안함
        'no-new-wrappers': 'error',
        // return false 허용
        'no-setter-return': 'off',
        // 콘솔 제거
        'no-console': ['error', { allow: ['warn', 'error'] }],
        // 캬멜 표기법 미사용시 에러
        'camelcase': 'error',
        // else 구문과 if 구문에 동일한 검증 하지 않음
        'no-dupe-else-if': 'error',
        // 키워드 전 후 공백
        'keyword-spacing': ['error', { 'before': true, 'after': true }],
        // 공백 뒤에 () 오지 않음
        'space-before-function-paren': ['error', {'anonymous': 'always', 'named': 'never', 'asyncArrow': 'always'}],
        // () 뒤에 공백
        'space-before-blocks': 'error',
        'no-unused-vars': ['error',
            { 'vars': 'all', 'args': 'after-used', 'ignoreRestSiblings': false, 'caughtErrorsIgnorePattern': '^err'}
        ]
    }
};
