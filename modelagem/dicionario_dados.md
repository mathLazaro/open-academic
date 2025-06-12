# Dicionário de Dados

## Authors (tb_authors)

Autores dos trabalhos acadêmicos

### Campos

| Campo            | Tipo    | Obrigatório? | Descrição                                                                       |
| ---------------- | ------- | ------------ | ------------------------------------------------------------------------------- |
| `id`             | VARCHAR | Sim          | **PK** - ID de Authors no OpenAlex (ex: `https://api.openalex.org/A5052975894`) |
| `name`           | VARCHAR | Sim          | Nome do autor                                                                   |
| `works_count`    | INT     | Não          | Quantidade de obras do autor (na API) (default = 0)                             |
| `cited_by_count` | INT     | Não          | Quantidade de vezes citado (na API) (default = 0)                               |

### Relacionamentos

-   É referenciado por **tb_authorships**

<hr>

## Domains (tb_domains)

Domínios de estudos dos trabalhos acadêmicos (ex.: Saúde)

### Campos

| Campo         | Tipo    | Obrigatório? | Descrição                                                                     |
| ------------- | ------- | ------------ | ----------------------------------------------------------------------------- |
| `id`          | VARCHAR | Sim          | **PK** - ID de Domains no OpenAlex (ex: `https://api.openalex.org/domains/1`) |
| `title`       | VARCHAR | Sim          | Título do domínio                                                             |
| `description` | TEXT    | Não          | Descrição do Domínio                                                          |
| `works_count` | INT     | Não          | Quantidade de obras por domínio (na API) (default = 0)                        |

### Relacionamentos

-   É referenciado por **tb_instituition_domains**
-   É referenciado por **tb_fields**

<hr>

## Fields (tb_fields)

Campos de estudos (ex.: Medicina, Biologia)

### Campos

| Campo         | Tipo    | Obrigatório? | Descrição                                                                    |
| ------------- | ------- | ------------ | ---------------------------------------------------------------------------- |
| `id`          | VARCHAR | Sim          | **PK** - ID de Fields no OpenAlex (ex: `https://api.openalex.org/fields/11`) |
| `title`       | VARCHAR | Sim          | Título do campo                                                              |
| `description` | TEXT    | Não          | Descrição do Campo                                                           |
| `works_count` | INT     | Não          | Quantidade de obras por campo (na API) (default = 0)                         |
| `domain_id`   | VARCHAR | Sim          | **FK** - Referencia `tb_domains.id`                                          |

### Relacionamentos

-   É referenciado por **tb_topics**

<hr>

## Keywords (tb_topic_keywords)

### Campos

| Campo      | Tipo    | Obrigatório? | Descrição                                  |
| ---------- | ------- | ------------ | ------------------------------------------ |
| `topic_id` | VARCHAR | Sim          | **PK** / **FK** - Referencia `tb_topic.id` |
| `word`     | VARCHAR | Sim          | **PK** Palavra chave                       |

<hr>

## Organizations (tb_organizations)

Armazena as informações das organizações que se relacionam com os trabalhos acadêmicos, podendo assumir os papéis de instituição de pesquisa, financiadora, e publicadora. Tais papeis são diferenciados na tabela **tb_roles**.

### Campos

| Campo            | Tipo    | Obrigatório? | Descrição                                                 |
| ---------------- | ------- | ------------ | --------------------------------------------------------- |
| `id`             | VARCHAR | Sim          | **PK** ID ROR da organização                              |
| `name`           | VARCHAR | Sim          | Nome da organização                                       |
| `city`           | VARCHAR | Sim          | Cidade                                                    |
| `country`        | VARCHAR | Sim          | País                                                      |
| `country_code`   | VARCHAR | Sim          | Código do país                                            |
| `works_count`    | INT     | Não          | Quantidade de obras da organização (na API) (default = 0) |
| `cited_by_count` | INT     | Não          | Quantidade de vezes citado (na API) (default = 0)         |

### Relacionamentos

-   É referenciado por **tb_roles**
-   É referenciado por **tb_authorship**
-   É referenciado por **tb_instituition_domains**
-   É referenciado por **tb_work_organizations**

<hr>

## Roles (tb_roles)

Tabela de especialização da organização

### Campos

| Campo             | Tipo    | Obrigatório? | Descrição                                                                                                          |
| ----------------- | ------- | ------------ | ------------------------------------------------------------------------------------------------------------------ |
| `organization_id` | VARCHAR | Sim          | **PK**/**FK** ID ROR da organização (Referencia `tb_organization.id`)                                              |
| `role`            | ENUM    | Sim          | **PK** Tipo da organização (`INSTITUTION`, `PUBLISHER`, `FUNDER`)                                                         |
| `works_count`     | INT     | Não          | Quantidade de obras da organização por tipo (na API) (default = 0)                                                 |

### Constraints

    PRIMARY KEY (organization_id, role)

<hr>

## Topics (tb_topics)

Tópicos dos trabalhos.

### Campos

| Campo         | Tipo    | Obrigatório? | Descrição                                                                        |
| ------------- | ------- | ------------ | -------------------------------------------------------------------------------- |
| `id`          | VARCHAR | Sim          | **PK** - ID de Topics no OpenAlex (ex: `https://api.openalex.org/topics/T10333`) |
| `title`       | VARCHAR | Sim          | Título do tópico                                                                 |
| `description` | TEXT    | Não          | Descrição do tópico                                                              |
| `works_count` | INT     | Não          | Quantidade de obras por tópico (default = 0)                                     |
| `field_id`    | VARCHAR | Sim          | **FK** - Referencia `tb_fields.id`                                               |

### Relacionamentos

-   É referenciado por **tb_work_topics**
-   É referenciado por **tb_topic_keywords**

<hr>

## Works (tb_works)

Entidade que modela os trabalhos acadêmicos.

### Campos

| Campo                    | Tipo    | Obrigatório? | Descrição                                                                                                                     |
| ------------------------ | ------- | ------------ | ----------------------------------------------------------------------------------------------------------------------------- |
| `id`                     | VARCHAR | Sim          | **PK** - ID de Works no OpenAlex (ex: `https://api.openalex.org/W2128635872`)                                                 |
| `title`                  | VARCHAR | Sim          | Titulo da obra                                                                                                                |
| `is_open`                | BOOLEAN | Não          | Obra open source (default = false)                                                                                            |
| `referenced_works_count` | INT     | Não          | Quantidade de obras que cita (default = 0)                                                                                    |
| `cited_by_count`         | INT     | Não          | Quantidade de vezes que foi citada (default = 0)                                                                              |
| `fwci`                   | REAL    | Não          | Impacto das citações comparado com a média mundial no mesmo campo de conhecimento: bom(>1), médio(=1), ruim(<1) (default = 0) |
| `publish_date`           | DATE    | Sim          | Data de publicação da obra                                                                                                    |
| `type`                   | ENUM    | Sim          | Tipo da obra (`article`, `book`, `book-chapter`, `dissertation`, `review`)                                                    |

### Relacionamentos

-   É referenciado por **tb_authorships**
-   É referenciado por **tb_work_topics**
-   É referenciado por **tb_work_organizations**

<hr>

## Tabelas associativas

Tabelas que relacionam as entidades do banco.

### tb_authorships

Relaciona a autoria das obras com autores e instituições.

| Campo             | Tipo    | Obrigatório? | Descrição                                |
| ----------------- | ------- | ------------ | ---------------------------------------- |
| `work_id`         | VARCHAR | Sim          | **FK** - Referencia `tb_works.id`        |
| `author_id`       | VARCHAR | Sim          | **FK** - Referencia `tb_authors.id`      |
| `instituition_id` | VARCHAR | Não          | **FK** - Referencia `tb_organization.id` |

### Constraints

    PRIMARY KEY (work_id, author_id, instituition_id)

### tb_work_organizations

Relaciona as obras acadêmicas com as organizações financiadoras/editoras.

| Campo             | Tipo    | Obrigatório? | Descrição                                             |
| ----------------- | ------- | ------------ | ----------------------------------------------------- |
| `work_id`         | VARCHAR | Sim          | **FK** – Referência `tb_works.id`                     |
| `organization_id` | VARCHAR | Sim          | **FK** – Referência `tb_organizations.id`             |
| `role_type`       | ENUM    | Sim          | Papel da organização na obra: {`FUNDER`, `PUBLISHER`} |

### Constraints

    PRIMARY KEY (work_id, organization_id, role_type)

### tb_work_topics

Relaciona as obras acadêmicas com seus tópicos de pesquisa.

| Campo      | Tipo    | Obrigatório? | Descrição                                                                     |
| ---------- | ------- | ------------ | ----------------------------------------------------------------------------- |
| `topic_id` | VARCHAR | Sim          | **FK** - Referencia `tb_topics.id`                                            |
| `work_id`  | VARCHAR | Sim          | **FK** - Referencia `tb_works.id`                                             |
| `score`    | REAL    | Não          | Score da relação entre o tópico (gerado pela API através de IA) (default = 0) |

### tb_organization_domains

Relaciona os domínios de pesquisa com as organizações.

| Campo             | Tipo    | Obrigatório? | Descrição                                |
| ----------------- | ------- | ------------ | ---------------------------------------- |
| `organization_id` | VARCHAR | Sim          | **FK** - Referencia `tb_organization.id` |
| `domain_id`       | VARCHAR | Sim          | **FK** - Referencia `tb_domain.id`       |

