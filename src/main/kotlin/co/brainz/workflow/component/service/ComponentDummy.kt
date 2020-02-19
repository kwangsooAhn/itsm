package co.brainz.workflow.component.service

import co.brainz.workflow.component.dto.ComponentDto
import org.springframework.stereotype.Service

@Service
class ComponentDummy(): Component {

    override fun getComponents(search: String): String {
        return  "{\n" +
                "    'form': { 'id': 'c17eb3ad-dffb-4315-adc0-5a1b0a1bf642', 'name': '장애신고', 'description': '장애신고 신청서 문서양식입니다.'},\n" +
                "    'components': [\n" +
                "        { \n" +
                "            'id': '4a417b48-be2e-4ebe-82bf-8f80a63622a4',  \n" +
                "            'type': 'text',\n" +
                "            'label': {'position': 'left', 'column': 2, 'size': 12, 'color': '#ffffff', 'bold': 'Y', 'italic': 'N', 'underline': 'N', 'text': '제목', 'align': 'left'},\n" +
                "            'display': {'placeholder': '제목을 입력하세요.', 'column': 10, 'outline-width': 1, outline-color: '#000000', 'order': 1},\n" +
                "            'validate': {'type': 'validate', 'required': 'N', 'regexp': '', 'regexp-msg': '', 'length-min': 0, 'length-max': 30}\n" +
                "        },\n" +
                "        { \n" +
                "            'id': '00e3b10a-a9b1-4f68-9ae0-113f34532c27',  \n" +
                "            'type': 'textarea',\n" +
                "            'label': {'position': 'left', 'column': 2, 'size': 12, 'color': '#ffffff', 'bold': 'Y', 'italic': 'N', 'underline': 'N', 'text': '설명', 'align': 'left'},\n" +
                "            'display': {'rows': 3, 'placeholder': '', 'column': 10, 'outline-width': 1, outline-color: '#000000', 'order': 2},\n" +
                "            'validate': {'required': 'N', 'length-min': 0, 'length-max': 100}\n" +
                "        },\n" +
                "        { \n" +
                "            'id': '53f7b7f9-c7de-4ffc-a819-0be1d37032fe',  \n" +
                "            'type': 'select',\n" +
                "            'label': {'position': 'left', 'column': 2, 'size': 12, 'color': '#ffffff', 'bold': 'Y', 'italic': 'N', 'underline': 'N', 'text': '부서', 'align': 'left'},\n" +
                "            'display': {'column': 10, 'order': 3},\n" +
                "            'validate': {'required': 'N'},\n" +
                "            'option': [ {'seq': 1, 'name': 'ITSM팀', 'value': 'itsm'}, {'seq': 2, 'name': '인프라웹팀', 'value': 'infraweb'} ]\n" +
                "        },\n" +
                "        { \n" +
                "            'id': 'f8544e7e-6764-4d85-9745-ae63c0b07a7e',  \n" +
                "            'type': 'image',\n" +
                "            'display': {'path': '/noImage.png', 'width': 100, 'height': 100, 'align': 'center', 'order': 4}\n" +
                "        },\n" +
                "        { \n" +
                "            'id': 'e89aeb3b-04f2-4806-8e59-803c7a3e996c',  \n" +
                "            'type': 'datetime',\n" +
                "            'label': {'position': 'left', 'column': 2, 'size': 12, 'color': '#ffffff', 'bold': 'Y', 'italic': 'N', 'underline': 'N', 'text': '부서', 'align': 'left'},\n" +
                "            'display': {'column': 10, 'format': 'HH:mm:ss', 'default': 'now', 'order': 5},\n" +
                "            'validate': {'date-min': '1875-06-23', 'date-max': '2999-12-31', 'required': 'N'}\n" +
                "        }\n" +
                "    ]\n" +
                "}"
    }

    override fun getComponent(compId: String): ComponentDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
