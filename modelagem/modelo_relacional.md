Authors (id, name, works_count, cited_by_count)

Domains (id, title, description, works_count)

Fields (id, title, description, works_count, domain_id) <br>
*domain_id references Domains(id)*

Topics (id, title, description, works_count, field_id) <br>
*field_id references Fields(id)*

Topic_Keywords (topic_id, word)
*topic_id references Topics(id)*

Organizations (id, name, city, country, country_code, works_count, cited_by_count)

Roles (organization_id, role, works_count) <br>
*organization_id references Organizations(id)*

Works (id, title, is_open, referenced_works_count, cited_by_count, fwci, publish_date, type) <br>

Authorships (work_id, author_id, instituition_id) <br>
*work_id references Works(id)* <br>
*author_id references Authors(id)* <br>
*instituition_id references Organizations(id)* <br>

Work_Organizations (work_id, organization_id, role_type) <br>
*work_id references Works(id)* <br>
*organization_id references Organizations(id)*

Work_Topics (work_id, topic_id, score) <br>
*work_id references Works(id)* <br>
*topic_id references Topics(id)*

Instituition_Domains (instituition_id, domain_id) <br>
*instituition_id references Organizations(id)* <br>
*domain_id references Domains(id)*
