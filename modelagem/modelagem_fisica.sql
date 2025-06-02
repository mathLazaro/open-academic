CREATE TYPE organization_work_role AS ENUM ('FUNDER', 'PUBLISHER');

CREATE TYPE work_type AS ENUM ('article', 'book', 'book-chapter', 'dissertation', 'review');

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
    domain_id VARCHAR NOT NULL REFERENCES tb_domains(id)
);

CREATE TABLE tb_topics (
    id VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,
    works_count INT DEFAULT 0,
    field_id VARCHAR NOT NULL REFERENCES tb_fields(id)
);

CREATE TABLE tb_topic_keywords (
    topic_id VARCHAR NOT NULL REFERENCES tb_topics(id),
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
    organization_id VARCHAR NOT NULL REFERENCES tb_organizations(id),
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
    work_id VARCHAR NOT NULL REFERENCES tb_works(id),
    author_id VARCHAR NOT NULL REFERENCES tb_authors(id),
    instituition_id VARCHAR REFERENCES tb_organizations(id),
    PRIMARY KEY (work_id, author_id, instituition_id)
);

CREATE TABLE tb_work_organizations (
    work_id VARCHAR NOT NULL REFERENCES tb_works(id),
    organization_id VARCHAR NOT NULL REFERENCES tb_organizations(id),
    role_type organization_work_role NOT NULL,
    PRIMARY KEY (work_id, organization_id, role_type)
);

CREATE TABLE tb_work_topics (
    topic_id VARCHAR NOT NULL REFERENCES tb_topics(id),
    work_id VARCHAR NOT NULL REFERENCES tb_works(id),
    score REAL DEFAULT 0,
    PRIMARY KEY (topic_id, work_id)
);

CREATE TABLE tb_instituition_domains (
    instituition_id VARCHAR NOT NULL REFERENCES tb_organizations(id),
    domain_id VARCHAR NOT NULL REFERENCES tb_domains(id),
    PRIMARY KEY (instituition_id, domain_id)
);
