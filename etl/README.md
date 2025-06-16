# ETL

Este repositório contém o pipeline de ETL desenvolvido para extrair, transformar e carregar dados da API [OpenAlex](https://openalex.org/) para um banco de dados relacional PostgreSQL. O objetivo é centralizar e normalizar informações sobre trabalhos acadêmicos, autores, organizações, tópicos e áreas do conhecimento.

## 🛠️ Tecnologias Utilizadas

- Python
- PostgreSQL
- psycopg2
- requests

## 📐 Estrutura

- `dao.py`: Responsável pela comunicação com o banco de dados.
- `utils.py`: Contém funções auxiliares, incluindo chamadas à API.
- `change_id_to_ror.py`: Script para substituição de id para ror id.
- `etl_*.py`: Scripts para execução de extração, transformação e carga de cada tabela.
- `cleaning_data.sql`: Limpeza dos dados inseridos.
- `requirements.txt`: Pacotes requiridos no projeto.

## 🧩 Estratégia Adotada

- **Tabelas definitivas sem chaves estrangeiras** na etapa inicial.
- Inserções feitas diretamente, com consistência assegurada posteriormente por scripts.
- **Normalização e limpeza de dados** aplicada durante a transformação.
- Remoção de registros incompletos ou corrompidos.

## 🏢 Integração de Organizações

Foi realizada a padronização de instituições, financiadoras e editoras:

- Substituição dos IDs da API OpenAlex por **ROR IDs**.
- Recuperação posterior dos dados via endpoint `institutions`.
- Armazenamento unificado em `tb_organizations` com seus respectivos papéis definidos em `tb_roles`.

## ⚙️ Performance e Paralelismo

- Identificado gargalo entre iterações, não nas requisições.
- Evitado o compartilhamento de conexões entre threads.
- Implementado **paralelismo com isolamento de conexões**, garantindo desempenho e integridade transacional.

## ✅ Resultado

O processo resultou em uma base de dados relacional normalizada, consistente e preparada para análises, estudos ou integrações com sistemas maiores. A modelagem permite flexibilidade na manipulação de entidades e suas relações com controle sobre integridade e qualidade dos dados.
