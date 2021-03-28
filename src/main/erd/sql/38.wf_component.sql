/**
 * 컴포넌트정보
 */
DROP TABLE IF EXISTS wf_component cascade;

CREATE TABLE wf_component
(
	component_id varchar(128) NOT NULL,
	form_id varchar(128) NOT NULL,
	component_type varchar(100) NOT NULL,
	mapping_id varchar(128),
	is_topic boolean DEFAULT 'false',
	CONSTRAINT wf_component_pk PRIMARY KEY (component_id),
	CONSTRAINT wf_component_fk FOREIGN KEY (form_id) REFERENCES wf_form (form_id)
);

COMMENT ON TABLE wf_component IS '컴포넌트정보';
COMMENT ON COLUMN wf_component.component_id IS '컴포넌트아이디';
COMMENT ON COLUMN wf_component.form_id IS '문서양식아이디';
COMMENT ON COLUMN wf_component.component_type IS '컴포넌트종류';
COMMENT ON COLUMN wf_component.mapping_id IS '매핑아이디';
COMMENT ON COLUMN wf_component.is_topic IS '토픽여부';

insert into wf_component values ('53dc0f34890f4c48a7ebd410169623b6','4028b25d787736640178773f645b0003','label','', FALSE);
insert into wf_component values ('caaf208e3d6c4ea0a4ec4d1fa63fc81c','4028b25d787736640178773f645b0003','divider','', FALSE);
insert into wf_component values ('f79d816a6ae748aabc1c58748b65c119','4028b25d787736640178773f645b0003','accordion-start','', FALSE);
insert into wf_component values ('cf96b8a2488c4b84a90002b01f80aeba','4028b25d787736640178773f645b0003','datetime','id1', FALSE);
insert into wf_component values ('aca126bd4f6958bce2cb5e6e607251d0','4028b25d787736640178773f645b0003','custom-code','user', FALSE);
insert into wf_component values ('c088820086ef49488a3033eb84566fa5','4028b25d787736640178773f645b0003','inputbox','id3', FALSE);
insert into wf_component values ('5e6444cb4fad489fa76cacd2b8a26108','4028b25d787736640178773f645b0003','dropdown','id4', FALSE);
insert into wf_component values ('3c592c92c4bc4868a4b585f8c9d9c360','4028b25d787736640178773f645b0003','dropdown','id5', FALSE);
insert into wf_component values ('a6ba20dc101143c1b6bcc21c159cc1dd','4028b25d787736640178773f645b0003','datetime','id6', FALSE);
insert into wf_component values ('ad4c94b7f64a4d01bafef76fbbb444bc','4028b25d787736640178773f645b0003','custom-code','assignee', FALSE);
insert into wf_component values ('ee6e34704b094873926c3918ae00bfbd','4028b25d787736640178773f645b0003','inputbox','id7', TRUE);
insert into wf_component values ('5b8529ddbd40462e824439a753b2c153','4028b25d787736640178773f645b0003','textbox','id8', FALSE);
insert into wf_component values ('74b8c62595f544eaa47d7c8b6ea8560e','4028b25d787736640178773f645b0003','fileupload','id9', FALSE);
insert into wf_component values ('4e1c394c09c84cb2a20b20492f2a3cb0','4028b25d787736640178773f645b0003','accordion-end','', FALSE);
insert into wf_component values ('022540d758ec405ba45d70f8df0c03fc','4028b25d787736640178773f645b0003','accordion-start','', FALSE);
insert into wf_component values ('36b04439545649a79824d742d8d48a61','4028b25d787736640178773f645b0003','datetime','id12', FALSE);
insert into wf_component values ('489d27f3f1eb454780256befc0167cf6','4028b25d787736640178773f645b0003','textbox','id13', FALSE);
insert into wf_component values ('e5f66323f50b498189d7ebccfaf3c4a9','4028b25d787736640178773f645b0003','fileupload','id14', FALSE);
insert into wf_component values ('27e376940afc4568aa228f8663318ea0','4028b25d787736640178773f645b0003','accordion-end','', FALSE);
insert into wf_component values ('357f6d029a3d420d9fefba4e5a6ca9df','4028b25d787736640178773f645b0003','accordion-start','', FALSE);
insert into wf_component values ('b28cb8265d134217b7336704c3554314','4028b25d787736640178773f645b0003','radio','id15', FALSE);
insert into wf_component values ('4ba00478892f4195a10597b3090d887c','4028b25d787736640178773f645b0003','textbox','', FALSE);
insert into wf_component values ('974aa952322d4800ab68d7efaa5c921f','4028b25d787736640178773f645b0003','fileupload','id17', FALSE);
insert into wf_component values ('130111f086244dc393963a75a480631e','4028b25d787736640178773f645b0003','accordion-end','', FALSE);
insert into wf_component values ('e4a75d472ad94be0abc41b7be5c60a06','4028b25d787736640178773857920000','label','',FALSE);
insert into wf_component values ('29fc1c91ed9145c2ab4569db715ea461','4028b25d787736640178773857920000','divider','',FALSE);
insert into wf_component values ('6b20477610204c32ac7460143405299f','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('763a02453da34238a611908d427b03a4','4028b25d787736640178773857920000','datetime','id1',FALSE);
insert into wf_component values ('a64a8b2b177f51b09134faf7d148a4bb','4028b25d787736640178773857920000','custom-code','user',FALSE);
insert into wf_component values ('189aafb3a9404ec489607fef8b9f8560','4028b25d787736640178773857920000','inputbox','id3',FALSE);
insert into wf_component values ('80060ae8be4b42a9883bc7b7dd96632b','4028b25d787736640178773857920000','dropdown','id4',FALSE);
insert into wf_component values ('a02d6fbdc20e46b08c95453f6d25c311','4028b25d787736640178773857920000','dropdown','id5',FALSE);
insert into wf_component values ('0177a25c92ad43aebd50bfc45d32fc02','4028b25d787736640178773857920000','datetime','id6',FALSE);
insert into wf_component values ('303f8e1f7e814953a759ff93ee336f65','4028b25d787736640178773857920000','custom-code','assignee',FALSE);
insert into wf_component values ('8fe7d5f6c16a49788420fe648a9a4cb2','4028b25d787736640178773857920000','inputbox','id7',true);
insert into wf_component values ('63c2bbf75cb9456a9de83c2f668ad181','4028b25d787736640178773857920000','textbox','id8',FALSE);
insert into wf_component values ('cec8377af21641e1a2a274db6654aa88','4028b25d787736640178773857920000','fileupload','id9',FALSE);
insert into wf_component values ('de47121e6034445598ff4c216168781e','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('7b53c1640c1640ca889f6e44ac92a256','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('2b2f880adf2f434c8b71d57f3439129e','4028b25d787736640178773857920000','datetime','id10',FALSE);
insert into wf_component values ('568e2d7388614f349756a9d55f9fbf82','4028b25d787736640178773857920000','textbox','id11',FALSE);
insert into wf_component values ('fc2d992990704ad7be4eb8fb52839a0b','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('cf757877eb04473492014366b49d7aef','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('4601ce00de0a463098f4d4c1a020028f','4028b25d787736640178773857920000','datetime','id12',FALSE);
insert into wf_component values ('c2a95ab676214ffa82571c8d24047f1a','4028b25d787736640178773857920000','textbox','id13',FALSE);
insert into wf_component values ('c57c3c17b8b34783ba385fa33d96570c','4028b25d787736640178773857920000','fileupload','id14',FALSE);
insert into wf_component values ('f2bf25aa9b87468ba02398838d110d9d','4028b25d787736640178773857920000','custom-code','assignee2',FALSE);
insert into wf_component values ('aec9c3f0a2cc456c9870e7cda1b6980a','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('f4639d8fee6a4796baf464b8f55105e5','4028b25d787736640178773857920000','accordion-start','',FALSE);
insert into wf_component values ('9cfaa92789b64c7da46c205995416821','4028b25d787736640178773857920000','datetime','id15',FALSE);
insert into wf_component values ('05df4c3c4c2641f082b8cb181327511d','4028b25d787736640178773857920000','textbox','id16',FALSE);
insert into wf_component values ('50871faf2bf74d2b8198b0049c9eab72','4028b25d787736640178773857920000','accordion-end','',FALSE);
insert into wf_component values ('a0438d6ebeb3e76391de4ca2591b891d','4028b25d77ed7e6f0177ed8daba10001','dropdown','',FALSE);
insert into wf_component values ('a083bcc9a0679f6bc9cde83eb4205161','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('a15a240317ee972e96532bf01836fd5c','4028b25d77ed7e6f0177ed8daba10001','custom-code','assignee0',FALSE);
insert into wf_component values ('a1709377086e44e4e319ba3c2aec93c4','4028b25d77ed7e6f0177ed8daba10001','ci','cmdb',FALSE);
insert into wf_component values ('a1bbab51ba6b903eea7834b64ff2018e','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('a20306ee9477e23c3b460bdb459f5f89','4028b25d77ed7e6f0177ed8daba10001','dropdown','',FALSE);
insert into wf_component values ('a293e254d00208aeb1afb8d78c0d0737','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a2ef0bbeda3884c8111f82abeb242d6f','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('a40b7f5aaa39c16cf44d8db83f3f2a5b','4028b25d77ed7e6f0177ed8daba10001','fileupload','',FALSE);
insert into wf_component values ('a4c6a3dd66d0cc36d108ff8cc6904fd7','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('a563dbfc8286f3ed75f47b8e778390fb','4028b25d77ed7e6f0177ed8daba10001','fileupload','',FALSE);
insert into wf_component values ('a640301d7e67dc92c279f83bec7358be','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a661cc2ef029e637a72124a4b7c5f32f','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a7bc03e8abd70100a86e43338e308054','4028b25d77ed7e6f0177ed8daba10001','custom-code','assignee1',FALSE);
insert into wf_component values ('a7fb73ce3c61d3d600ab135d6a6a8684','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('a82c5acd1b0ec0bba083d76135d39e5a','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('a87535522638ab7240e972d44d66ef49','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('a90620f766dedb2beebbe776eff556ec','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);
insert into wf_component values ('a9611c745a6716665c5e7690b872310b','4028b25d77ed7e6f0177ed8daba10001','custom-code','assignee2',FALSE);
insert into wf_component values ('a9b580dd25da55ce3ce347bcef0169d5','4028b25d77ed7e6f0177ed8daba10001','divider','',FALSE);
insert into wf_component values ('a9ef096c157fab2ec319efcf467b568a','4028b25d77ed7e6f0177ed8daba10001','inputbox','',TRUE);
insert into wf_component values ('aa54cd88560b84b902653fd90336ee1e','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('aa5a75f1766bc691fa0c066202c776ae','4028b25d77ed7e6f0177ed8daba10001','textbox','',FALSE);
insert into wf_component values ('abc34c5409954d3c792f2209f3347cdf','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('ad5e4623a663879158834cbdddd032e5','4028b25d77ed7e6f0177ed8daba10001','accordion-end','',FALSE);
insert into wf_component values ('ad6afd860d2cda7a78bf93f4e23c8efd','4028b25d77ed7e6f0177ed8daba10001','label','',FALSE);
insert into wf_component values ('ae615f2ac0d145ef25d5e7253c12940d','4028b25d77ed7e6f0177ed8daba10001','accordion-start','',FALSE);
insert into wf_component values ('aff3e77073f75d2385e2347ed996f412','4028b25d77ed7e6f0177ed8daba10001','datetime','',FALSE);