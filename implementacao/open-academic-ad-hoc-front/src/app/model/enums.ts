export enum TableType {
  AUTHOR = 'AUTHOR',
  DOMAIN = 'DOMAIN',
  FIELD = 'FIELD',
  ORGANIZATION = 'ORGANIZATION',
  ROLE = 'ROLE',
  TOPIC = 'TOPIC',
  WORK = 'WORK',
  AUTHORSHIP = 'AUTHORSHIP',
  WORK_ORGANIZATION = 'WORK_ORGANIZATION',
  WORK_TOPIC = 'WORK_TOPIC',
}

export enum JoinType {
  INNER = 'INNER',
  LEFT = 'LEFT',
  RIGHT = 'RIGHT',
}

export enum ColumnType {
  STRING = 'STRING',
  NUMBER = 'NUMBER',
  DATE = 'DATE',
  BOOLEAN = 'BOOLEAN',
}

export enum Operator {
  EQUALS = '=',
  NOT_EQUALS = '!=',
  GREATER_THAN = '>',
  LESS_THAN = '<',
  GREATER_THAN_EQUALS = '>=',
  LESS_THAN_EQUALS = '<=',
  LIKE = 'LIKE',
}

export enum AggregationFunction {
  COUNT = 'COUNT',
  COUNT_DISTINCT = 'COUNT_DISTINCT',
  SUM = 'SUM',
  AVG = 'AVG',
  MAX = 'MAX',
  MIN = 'MIN',
}

export function getOperator(value: string): Operator | undefined {
  switch (value) {
    case '=':
      return Operator.EQUALS;
    case '!=':
      return Operator.NOT_EQUALS;
    case '>':
      return Operator.GREATER_THAN;
    case '<':
      return Operator.LESS_THAN;
    case '>=':
      return Operator.GREATER_THAN_EQUALS;
    case '<=':
      return Operator.LESS_THAN_EQUALS;
    case 'LIKE':
      return Operator.LIKE;
    default:
      return undefined;
  }
}
