create table staging.authorship_work_ids(
	work_id VARCHAR,
	instituition_id VARCHAR,
	author_id VARCHAR,
	processed boolean default false
);

create table staging.publisher_work_ids(
	work_id VARCHAR,
	publisher_id VARCHAR,
	processed boolean default false
);

create table staging.funder_work_ids(
	work_id VARCHAR,
	funder_id VARCHAR,
	processed boolean default false
); 

create table staging.topic_work_ids(
	topic_id VARCHAR,
	work_id VARCHAR,
	processed boolean default false
);

create table staging.map_organization_ror_id(
	id VARCHAR,
	ror_id VARCHAR
);

create index idx_map_organization_ror_id_id on staging.map_organization_ror_id using btree (id);

select * from tb_domains

select * from tb_topic_keywords

delete from tb_topics
