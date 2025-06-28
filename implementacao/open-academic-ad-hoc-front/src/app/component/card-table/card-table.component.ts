import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Structure } from '../../service/structure.service';
import { getOperator, JoinType, Operator, TableType } from '../../model/enums';
import { Field } from '../../model/field';

import { FormsModule } from '@angular/forms';
import { Column, DataConstructor } from '../../model/report';

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

  tableToJoin?: TableType;

  isBlurred = false;

  checkedColumns: { [key: string]: boolean } = {};

  checkedFilters: { [key: string]: boolean } = {};

  getCheckedColumns(): string[] {
    return Object.keys(this.checkedColumns).filter((key) => !!this.checkedColumns[key]);
  }

  getCheckedFilters(): string[] {
    return Object.keys(this.checkedFilters).filter((key) => !!this.checkedFilters[key]);
  }

  @Output()
  onJoinTable = new EventEmitter<{ table: TableType; joinType: JoinType }>();

  @Output()
  onFinalizeChanges = new EventEmitter<Map<TableType, DataConstructor>>();

  constructor(private structure: Structure) {}

  get attributes(): Field[] {
    return this.structure.getAllAtributes(this.table);
  }

  get joinTypes(): JoinType[] {
    return this.structure.getAllJoinTypes();
  }

  get tables(): TableType[] {
    return this.structure.getAllTables();
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

    const columns = selectedColumns.map((column) => this.extractColumnRow(column));
    const filters = selectedFilters.map((filter) => this.extractFilterRow(filter));

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

  extractFilterRow(field: string): { field: Field; operator: Operator; value: string } {
    const id = `filter-${field}`;

    const tr = document.getElementById(id);

    const operator = getOperator(tr!.getElementsByTagName('select')[0].value) as Operator;
    const valueElement = tr!.querySelector('input[name="value-input"]') as HTMLInputElement; // TODO corrigir

    return {
      field: field as Field,
      operator: operator,
      value: valueElement.value || '',
    };
  }

  extractJoinType(): JoinType {
    const selectElement = document.getElementById('join-type') as HTMLSelectElement;
    return selectElement.value as JoinType;
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
}
