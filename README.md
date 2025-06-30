# 🗃️ Modelagem de Banco de Dados para Sistema Acadêmico
Projeto desenvolvido para a disciplina **Banco de Dados II** da Universidade Federal de Itajubá, implementando um sistema de relatórios *ad hoc* com integração à API OpenAlex

## ⚙️ Tarefas envolvidas

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

## 🛠️ Tecnologias Utilizadas

### 🔙 Back-end
- **Java** com **Spring Boot**
- **Hibernate** como ORM (Object-Relational Mapping)
- **CriteriaBuilder** para construção de consultas SQL dinâmicas com base nos parâmetros do front-end
- **PostgreSQL** como banco de dados relacional

### 🔝 Front-end
- **Angular**: Framework SPA utilizado para a construção da interface com o usuário. Permite seleção de tabelas, filtros, operadores e exibição dos relatórios ad hoc.

### 🔄 ETL
- **Python**: Usado no processo de ETL (Extração, Transformação e Carga)
- **psycopg2**: Biblioteca Python para conexão e inserção de dados no PostgreSQL

### 📡 API de Dados
- **OpenAlex**: API pública usada como fonte de dados. Fornece informações ricas sobre artigos científicos, autores, instituições, áreas do conhecimento, entre outros.

---

## 📁 Estrutura de Pastas do Projeto

### `implementação/`
Contém o código-fonte completo da aplicação:

- `open-academic-ad-hoc-front/`: Projeto Angular (front-end). O usuário interage com a aplicação e monta os filtros para os relatórios personalizados.
- `open-academic-ad-hoc/`: Projeto Java com Spring Boot (back-end). Recebe as requisições do front, monta consultas dinâmicas usando `CriteriaBuilder` e retorna os resultados.

### `modelagem/`
Contém os artefatos de modelagem do banco de dados:
- Modelo Entidade-Relacionamento (MER)
- Modelo Relacional
- Dicionário de Dados
- Estrutura lógica das tabelas com base nos dados da API

### `etl/`
Scripts responsáveis pela etapa de ETL:
- Extração dos dados da API OpenAlex
- Limpeza e transformação
- Inserção no banco de dados relacional com uso de `psycopg2`

---
## ▶ Como Executar o Projeto

### Pré-requisitos
- Node.js (para o front-end Angular)
- Java 17+ e Maven (para o back-end Spring Boot)
- PostgreSQL (instalado e rodando localmente)
- Python 3 e a biblioteca `psycopg2` (para o ETL)

---

### 1. Executar o ETL
```bash
cd etl/
python etl.py

### 2. Back-end
```bash
cd implementação/open-academic-ad-hoc/
./mvnw spring-boot:run
```

### 3. Front-end
```bash
cd implementação/open-academic-ad-hoc-front/
npm install
ng serve
```

---
