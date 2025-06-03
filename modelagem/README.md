# Modelagem de Banco de Dados para Sistema Acadêmico
Projeto desenvolvido para a disciplina **Banco de Dados II** (SPAD02) da Universidade Federal de Itajubá, implementando um sistema de relatórios *ad hoc* com integração à API OpenAlex

## Tarefas envolvidas

### Modelagem Conceitual e Lógica
- Análise da API OpenAlex  
- Definição do Modelo Entidade-Relacionamento  

Principais Entidades:  
  
| Entidade          | Descrição                                 | Campos Chave |
|-------------------|-------------------------------------------|--------------|
| `tb_works`        | Trabalhos acadêmicos                      | `id`         |
| `tb_authors`      | Autores dos trabalhos                     | `id`         |
| `tb_organizations`| Instituições, editoras e financiadoras    | `id`         |
| `tb_topics`       | Tópicos de pesquisa                       | `id`         |

### Implementação Física do Banco de Dados
Utilizando o postgreSQL  
- Script SQL para criar tabelas, constraints e índices  

## Descrição dos arquivos
