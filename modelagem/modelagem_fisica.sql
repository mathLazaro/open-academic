CREATE TYPE organization_work_role AS ENUM ('FUNDER', 'PUBLISHER');

CREATE TYPE work_type AS ENUM ('article', 'book', 'dissertation', 'review');

CREATE TYPE organization_role_type AS ENUM ('INSTITUTION', 'PUBLISHER', 'FUNDER');

CREATE TABLE tb_authors (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    works_count INT DEFAULT 0,
    cited_by_count INT DEFAULT 0
);

CREATE TABLE tb_domains (
    id VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    works_count INT DEFAULT 0
);

CREATE TABLE tb_fields (
    id VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    works_count INT DEFAULT 0,
    domain_id VARCHAR NOT NULL
);

CREATE TABLE tb_topics (
    id VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    works_count INT DEFAULT 0,
    field_id VARCHAR NOT NULL
);

CREATE TABLE tb_topic_keywords (
    topic_id VARCHAR NOT NULL,
    word VARCHAR NOT NULL,
    PRIMARY KEY (topic_id, word)
);

CREATE TABLE tb_organizations (
    id VARCHAR PRIMARY KEY,
    name VARCHAR,
    city VARCHAR,
    country VARCHAR,
    country_code VARCHAR,
    works_count INT DEFAULT 0,
    cited_by_count INT DEFAULT 0
);

CREATE TABLE tb_roles (
    organization_id VARCHAR NOT NULL,
    role organization_role_type NOT NULL,
    works_count INT DEFAULT 0,
    PRIMARY KEY (organization_id, role)
);

CREATE TABLE tb_works (
    id VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    is_open BOOLEAN DEFAULT FALSE,
    referenced_works_count INT DEFAULT 0,
    cited_by_count INT DEFAULT 0,
    fwci REAL DEFAULT 0,
    publish_date DATE NOT NULL,
    type work_type NOT NULL
);

CREATE TABLE tb_authorships (
    work_id VARCHAR NOT NULL,
    author_id VARCHAR NOT NULL,
    institution_id VARCHAR,
    PRIMARY KEY (work_id, author_id, institution_id)
);

CREATE TABLE tb_work_organizations (
    work_id VARCHAR NOT NULL,
    organization_id VARCHAR NOT NULL,
    role_type organization_work_role NOT NULL,
    PRIMARY KEY (work_id, organization_id, role_type)
);

CREATE TABLE tb_work_topics (
    topic_id VARCHAR NOT NULL,
    work_id VARCHAR NOT NULL,
    score REAL DEFAULT 0,
    PRIMARY KEY (topic_id, work_id)
);

CREATE TABLE tb_organization_domains (
    organization_id VARCHAR NOT NULL,
    domain_id VARCHAR NOT NULL,
    PRIMARY KEY (organization_id, domain_id)
);

-- CHAVE ESTRANGEIRAS (ADICIONADAS APÓS O ETL E LIMPEZA DOS DADOS)
ALTER TABLE tb_fields
    ADD CONSTRAINT fk_fields_domain FOREIGN KEY (domain_id) REFERENCES tb_domains(id);

ALTER TABLE tb_topics
    ADD CONSTRAINT fk_topics_field FOREIGN KEY (field_id) REFERENCES tb_fields(id);

ALTER TABLE tb_topic_keywords
    ADD CONSTRAINT fk_topic_keywords_topic FOREIGN KEY (topic_id) REFERENCES tb_topics(id);

ALTER TABLE tb_roles
    ADD CONSTRAINT fk_roles_organization FOREIGN KEY (organization_id) REFERENCES tb_organizations(id);

ALTER TABLE tb_authorships
    ADD CONSTRAINT fk_authorships_work FOREIGN KEY (work_id) REFERENCES tb_works(id),
    ADD CONSTRAINT fk_authorships_author FOREIGN KEY (author_id) REFERENCES tb_authors(id),
    ADD CONSTRAINT fk_authorships_institution FOREIGN KEY (institution_id) REFERENCES tb_organizations(id);

ALTER TABLE tb_work_organizations
    ADD CONSTRAINT fk_work_org_work FOREIGN KEY (work_id) REFERENCES tb_works(id),
    ADD CONSTRAINT fk_work_org_org FOREIGN KEY (organization_id) REFERENCES tb_organizations(id);

ALTER TABLE tb_work_topics
    ADD CONSTRAINT fk_work_topics_topic FOREIGN KEY (topic_id) REFERENCES tb_topics(id),
    ADD CONSTRAINT fk_work_topics_work FOREIGN KEY (work_id) REFERENCES tb_works(id);

ALTER TABLE tb_organization_domains
    ADD CONSTRAINT fk_inst_domains_inst FOREIGN KEY (organization_id) REFERENCES tb_organizations(id),
    ADD CONSTRAINT fk_inst_domains_domain FOREIGN KEY (domain_id) REFERENCES tb_domains(id);
