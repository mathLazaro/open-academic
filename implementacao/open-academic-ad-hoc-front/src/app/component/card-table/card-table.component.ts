import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Structure } from '../../service/structure.service';
import { getOperator, JoinType, Operator, TableType } from '../../model/enums';
import { Field } from '../../model/field';

import { FormsModule } from '@angular/forms';
import { Column, DataConstructor, Where } from '../../model/report';

@Component({
  selector: 'app-card-table',
  imports: [FormsModule],
  templateUrl: './card-table.component.html',
  styleUrl: './card-table.component.css',
})
export class CardTableComponent {
  @Input({ required: true })
  table!: TableType;

  @Input({ required: true })
  isRoot!: boolean;

  @Input({ required: true })
  isGrouping!: boolean;

  tableToJoin?: TableType;

  isBlurred = false;

  checkedColumns: { [key: string]: boolean } = {};

  checkedFilters: { [key: string]: boolean } = {};

  checkedColumnsGroupBy: { [key: string]: boolean } = {};

  selectedJoinType: JoinType = JoinType.INNER;

  @Output()
  onJoinTable = new EventEmitter<{ table: TableType; joinType: JoinType }>();

  @Output()
  onFinalizeChanges = new EventEmitter<Map<TableType, DataConstructor>>();

  @Output()
  onGrouping = new EventEmitter<boolean>();

  constructor(private structure: Structure) {}

  get isGroupByActive(): boolean {
    return this.getCheckedColumnsGroupBy().length > 0 || this.isGrouping;
  }

  get attributes(): Field[] {
    return this.structure.getAllAtributes(this.table);
  }

  get joinTypes(): JoinType[] {
    return this.structure.getAllJoinTypes();
  }

  get tables(): TableType[] {
    return this.structure.getAllTables();
  }

  getCheckedColumns(): string[] {
    return Object.keys(this.checkedColumns).filter((key) => !!this.checkedColumns[key]);
  }

  getCheckedFilters(): string[] {
    return Object.keys(this.checkedFilters).filter((key) => !!this.checkedFilters[key]);
  }

  getCheckedColumnsGroupBy(): string[] {
    return Object.keys(this.checkedColumnsGroupBy).filter((key) => !!this.checkedColumnsGroupBy[key]);
  }

  getNeighbors(table: TableType): TableType[] {
    return this.structure.getAllNeighbors(table);
  }

  operators(attribute: Field): Operator[] {
    return this.structure.getOperatorsByColumn(this.table, attribute);
  }

  prepareData() {
    const selectedColumns = this.getCheckedColumns();
    const selectedFilters = this.getCheckedFilters();
    const selectedColumnsGroupBy = this.getCheckedColumnsGroupBy();

    const columns = selectedColumns.map((column) => this.extractColumnRow(column));
    const filters = selectedFilters.map((filter) => this.extractFilterRow(filter));
    const groupBy = selectedColumnsGroupBy.map((column) => this.extractColumnRow(column));

    return {
      columnSet: columns,
      whereSet: filters,
    } as DataConstructor;
  }

  extractColumnRow(field: string): Column {
    const id = `select-${field}`;

    const tr = document.getElementById(id);
    const alias = tr!.getElementsByTagName('input')[1]?.value || '';

    return {
      field: field as Field,
      alias: alias,
      table: this.table,
    } as Column;
  }

  extractFilterRow(field: string): Where {
    const id = `filter-${field}`;

    const tr = document.getElementById(id);

    const operator = getOperator(tr!.getElementsByTagName('select')[0].value) as Operator;
    const valueElement = tr!.querySelector('input[name="value-input"]') as HTMLInputElement;

    return {
      table: this.table as TableType,
      field: field as Field,
      operator: operator,
      value: valueElement.value || '',
    };
  }

  extractJoinType(): JoinType {
    return this.selectedJoinType as JoinType;
  }

  onAdd() {
    this.isBlurred = true;
    const data = this.prepareData();
    const table = this.table;
    const map = new Map<TableType, DataConstructor>();
    map.set(table, data);
    this.onFinalizeChanges.emit(map);
  }

  onTableToJoinChange($event: Event) {
    const selectElement = $event.target as HTMLSelectElement;
    const selectedValue = selectElement.value as TableType;
    this.tableToJoin = selectedValue;
  }

  onJoin() {
    if (this.tableToJoin) {
      this.onJoinTable.emit({ table: this.tableToJoin!, joinType: this.extractJoinType() });
    }
  }
  onModify() {
    this.isBlurred = false;
  }

  emitGrouping() {
    this.onGrouping.emit(this.getCheckedColumnsGroupBy().length > 0);
  }
}
