-- Deleta todas as organizações que não foi possível fazer a agregação via ror id
DELETE FROM tb_work_organizations
WHERE organization_id NOT LIKE '%ror.org%';

-- Deleta todas as entidades não encontradas na API para adicionar as chaves estrangeiras
DELETE FROM tb_work_organizations
WHERE organization_id NOT IN (
    SELECT id FROM tb_organizations
);

DELETE FROM tb_authorships
WHERE institution_id NOT IN (
    SELECT id FROM tb_organizations
);

DELETE FROM tb_authorships
WHERE author_id NOT IN (
    SELECT id FROM tb_authors
);