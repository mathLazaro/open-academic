import { AggregationFunction, JoinType, TableType } from './enums';
import { Field } from './field';

export interface ReportResponse {
  first: string;
  second: Array<Record<string, any>>;
}

export interface Join {
  from: TableType;
  to: TableType;
  type: JoinType;
}

export interface Column {
  table: TableType;
  field: Field;
  alias?: string;
}

export interface Where {
  table: TableType;
  field: Field;
  operator: string;
  value: string | number | boolean | Date;
}

export interface ReportRequest {
  root: string;
  joinSet: Array<Join>;
  columnSet: Array<Column>;
  whereSet: Array<Where>;
  groupBy?: GroupBy;
}

export interface Aggregation {
  table: TableType;
  field: Field;
  aggregation: AggregationFunction;
  alias?: string;
}

export interface GroupBy {
  columnSet: Array<Column>;
  aggregation: Aggregation;
}

export interface DataConstructor {
  columnSet: Column[];
  whereSet: Where[];
  groupBy?: Column[];
}
