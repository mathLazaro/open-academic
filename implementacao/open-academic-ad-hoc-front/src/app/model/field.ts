import { Operator } from './enums';

export enum WorkField {
  ID = 'ID',
  TITLE = 'TITLE',
  IS_OPEN = 'IS_OPEN',
  REFERENCED_WORKS_COUNT = 'REFERENCED_WORKS_COUNT',
  CITED_BY_COUNT = 'CITED_BY_COUNT',
  FWCI = 'FWCI',
  PUBLISH_DATE = 'PUBLISH_DATE',
  TYPE = 'TYPE',
}

export enum AuthorField {
  ID = 'ID',
  NAME = 'NAME',
  WORKS_COUNT = 'WORKS_COUNT',
  CITED_BY_COUNT = 'CITED_BY_COUNT',
}

export enum DomainField {
  ID = 'ID',
  TITLE = 'TITLE',
  DESCRIPTION = 'DESCRIPTION',
  WORKS_COUNT = 'WORKS_COUNT',
}

export enum FieldField {
  ID = 'ID',
  TITLE = 'TITLE',
  DESCRIPTION = 'DESCRIPTION',
  WORKS_COUNT = 'WORKS_COUNT',
}

export enum OrganizationField {
  ID = 'ID',
  NAME = 'NAME',
  CITY = 'CITY',
  COUNTRY = 'COUNTRY',
  COUNTRY_CODE = 'COUNTRY_CODE',
  WORKS_COUNT = 'WORKS_COUNT',
  CITED_BY_COUNT = 'CITED_BY_COUNT',
}

export enum RoleField {
  ROLE = 'ROLE',
  WORKS_COUNT = 'WORKS_COUNT',
}

export enum TopicField {
  ID = 'ID',
  TITLE = 'TITLE',
  DESCRIPTION = 'DESCRIPTION',
  WORKS_COUNT = 'WORKS_COUNT',
}

export enum WorkOrganizationField {
  ROLE_TYPE = 'ROLE_TYPE',
}

export enum WorkTopicField {
  SCORE = 'SCORE',
}

export type Field =
  | WorkField
  | AuthorField
  | DomainField
  | FieldField
  | OrganizationField
  | RoleField
  | TopicField
  | WorkOrganizationField
  | WorkTopicField;

export const authorFieldType = {
  [AuthorField.ID]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [AuthorField.NAME]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
  [AuthorField.WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
  [AuthorField.CITED_BY_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};

export const workFieldType = {
  [WorkField.ID]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [WorkField.TITLE]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
  [WorkField.IS_OPEN]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [WorkField.REFERENCED_WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
  [WorkField.CITED_BY_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
  [WorkField.FWCI]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
  [WorkField.PUBLISH_DATE]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
  [WorkField.TYPE]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
};

export const domainFieldType = {
  [DomainField.ID]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [DomainField.TITLE]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
  [DomainField.DESCRIPTION]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
  [DomainField.WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};

export const fieldFieldType = {
  [FieldField.ID]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [FieldField.TITLE]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
  [FieldField.DESCRIPTION]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
  [FieldField.WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};

export const organizationFieldType = {
  [OrganizationField.ID]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [OrganizationField.NAME]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
  [OrganizationField.CITY]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
  [OrganizationField.COUNTRY]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
  [OrganizationField.COUNTRY_CODE]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [OrganizationField.WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
  [OrganizationField.CITED_BY_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};

export const roleFieldType = {
  [RoleField.ROLE]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
  [RoleField.WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};

export const topicFieldType = {
  [TopicField.ID]: [Operator.EQUALS, Operator.NOT_EQUALS],
  [TopicField.TITLE]: [Operator.EQUALS, Operator.NOT_EQUALS, Operator.LIKE],
  [TopicField.DESCRIPTION]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
  [TopicField.WORKS_COUNT]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};

export const workOrganizationFieldType = {
  [WorkOrganizationField.ROLE_TYPE]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.LIKE,
  ],
};

export const workTopicFieldType = {
  [WorkTopicField.SCORE]: [
    Operator.EQUALS,
    Operator.NOT_EQUALS,
    Operator.GREATER_THAN,
    Operator.LESS_THAN,
    Operator.GREATER_THAN_EQUALS,
    Operator.LESS_THAN_EQUALS,
  ],
};
