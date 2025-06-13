Authors (<u>id</u>, name, works_count, cited_by_count)

Domains (<u>id</u>, title, description, works_count)

Fields (<u>id</u>, title, description, works_count, domain_id) <br>
*domain_id references Domains(id)*

Topics (<u>id</u>, title, description, works_count, field_id) <br>
*field_id references Fields(id)*

Topic_Keywords (<u>topic_id, word</u>)
*topic_id references Topics(id)*

Organizations (<u>id</u>, name, city, country, country_code, works_count, cited_by_count)

Roles (<u>organization_id, role</u>, works_count) <br>
*organization_id references Organizations(id)*

Works (<u>id</u>, title, is_open, referenced_works_count, cited_by_count, fwci, publish_date, type) <br>

Authorships (<u>work_id, author_id, instituition_id</u>) <br>
*work_id references Works(id)* <br>
*author_id references Authors(id)* <br>
*instituition_id references Organizations(id)* <br>

Work_Organizations (<u>work_id, organization_id, role_type</u>) <br>
*work_id references Works(id)* <br>
*organization_id references Organizations(id)*

Work_Topics (<u>work_id, topic_id</u>, score) <br>
*work_id references Works(id)* <br>
*topic_id references Topics(id)*

Organization_Domains (<u>organization_id, domain_id</u>) <br>
*organization_id references Organizations(id)* <br>
*domain_id references Domains(id)*
