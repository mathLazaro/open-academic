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

-- ÍNDICES

CREATE INDEX idx_authorships_work_id ON tb_authorships(work_id);

CREATE INDEX idx_authorships_author_id ON tb_authorships(author_id);

CREATE INDEX idx_authorships_institution_id ON tb_authorships(institution_id);

CREATE INDEX idx_works_id_fwci ON tb_works(id, fwci DESC);

CREATE INDEX idx_works_type_fwci ON tb_works(type, fwci);

CREATE INDEX idx_org_domains_domain_id ON tb_organization_domains(domain_id);

CREATE INDEX idx_work_org_work_id ON tb_work_organizations(work_id);

CREATE INDEX idx_work_org_org_id ON tb_work_organizations(organization_id);

CREATE INDEX idx_work_org_role_type ON tb_work_organizations(role_type);

CREATE INDEX idx_org_domains_org_id ON tb_organization_domains(organization_id);

CREATE INDEX idx_roles_organization_id ON tb_roles(organization_id);

-- Quais autores tiveram as maiores notas por intituição?
EXPLAIN ANALYZE
SELECT
    a.id AS author_id,
    a.name AS author_name,
    i.id AS institution_id,
    i.name AS institution_name,
    w.fwci AS work_fwci
FROM tb_authorships au
JOIN tb_authors a ON a.id = au.author_id
JOIN tb_organizations i ON i.id = au.institution_id
JOIN tb_works w ON w.id = au.work_id
WHERE au.institution_id IS NOT NULL
ORDER BY w.fwci DESC;

-- Quais instituições estão associadas com maior fwci?
EXPLAIN ANALYZE
SELECT 
  o.id AS organization_id,
  o.name AS organization_name,
  AVG(w.fwci) AS avg_fwci
FROM tb_work_organizations wo
JOIN tb_organizations o ON o.id = wo.organization_id
JOIN tb_works w ON w.id = wo.work_id
GROUP BY o.id, o.name
ORDER BY avg_fwci DESC;

-- Quais autores mais publicaram (por número de obras)
EXPLAIN ANALYZE
SELECT 
  a.id AS author_id,
  a.name AS author_name,
  COUNT(DISTINCT au.work_id) AS num_works
FROM tb_authors a
JOIN tb_authorships au ON a.id = au.author_id
GROUP BY a.id, a.name
ORDER BY num_works DESC;

-- Quantas obras do tipo artigo tem FWCI maior que 1?
EXPLAIN ANALYZE
SELECT COUNT(*) AS articles_fwci_maior_1
FROM tb_works
WHERE type = 'article' AND fwci > 1;

-- Qual a média da atuação por cada domínio?
EXPLAIN ANALYZE
SELECT
    d.id AS domain_id,
    d.title AS domain_title,
    AVG(r.works_count) AS avg_works_per_organization
FROM tb_organization_domains od
JOIN tb_domains d ON d.id = od.domain_id
JOIN tb_roles r ON r.organization_id = od.organization_id
GROUP BY d.id, d.title
ORDER BY avg_works_per_organization DESC;

-- Qual a média da nota por role de cada instituição?
EXPLAIN ANALYZE
SELECT
    o.id AS organization_id,
    o.name AS organization_name,
    wo.role_type,
    AVG(w.fwci) AS avg_fwci
FROM tb_work_organizations wo
JOIN tb_organizations o ON o.id = wo.organization_id
JOIN tb_works w ON w.id = wo.work_id
GROUP BY o.id, o.name, wo.role_type
ORDER BY avg_fwci DESC;

-- Qual a soma de obras por organização e domínio?
EXPLAIN ANALYZE
SELECT
    o.id AS organization_id,
    o.name AS organization_name,
    d.id AS domain_id,
    d.title AS domain_title,
    SUM(r.works_count) AS total_works
FROM tb_organization_domains od
JOIN tb_organizations o ON o.id = od.organization_id
JOIN tb_domains d ON d.id = od.domain_id
JOIN tb_roles r ON r.organization_id = od.organization_id
GROUP BY o.id, o.name, d.id, d.title
ORDER BY total_works DESC;
