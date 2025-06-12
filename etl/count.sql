-- Count das tabelas

SELECT 'tb_authors' AS nome_tabela, COUNT(*) AS contagem FROM tb_authors

UNION ALL

SELECT 'tb_domains', COUNT(*) FROM tb_domains

UNION ALL

SELECT 'tb_fields', COUNT(*) FROM tb_fields

UNION ALL

SELECT 'tb_topics', COUNT(*) FROM tb_topics

UNION ALL

SELECT 'tb_topic_keywords', COUNT(*) FROM tb_topic_keywords

UNION ALL

SELECT 'tb_organizations', COUNT(*) FROM tb_organizations

UNION ALL

SELECT 'tb_roles', COUNT(*) FROM tb_roles

UNION ALL

SELECT 'tb_works', COUNT(*) FROM tb_works

UNION ALL

SELECT 'tb_authorships', COUNT(*) FROM tb_authorships

UNION ALL

SELECT 'tb_work_organizations', COUNT(*) FROM tb_work_organizations

UNION ALL

SELECT 'tb_work_topics', COUNT(*) FROM tb_work_topics

UNION ALL

SELECT 'tb_organization_domains', COUNT(*) FROM tb_organization_domains

UNION ALL

SELECT 'tb_topic_keywords', COUNT(*) FROM tb_topic_keywords;