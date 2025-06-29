import { Component } from '@angular/core';
import { DashboardReportComponent } from './component/dashboard-report/dashboard-report.component';
import { DataConstructor, GroupBy, Join, ReportRequest, ReportResponse } from './model/report';
import { Structure } from './service/structure.service';
import { AggregationFunction, JoinType, TableType } from './model/enums';
import { CardTableComponent } from './component/card-table/card-table.component';
import { ReportService } from './service/report.service';
import { FormsModule } from '@angular/forms';
import { Field } from './model/field';

@Component({
  selector: 'app-root',
  imports: [DashboardReportComponent, CardTableComponent, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  response?: ReportResponse;

  root?: TableType;

  addedTables: TableType[] = [];

  dataMap = new Map<TableType, DataConstructor>();

  joinSet: Join[] = [];

  isGrouping = false;

  tableToAggregate?: TableType;

  columnToAggregate?: Field;

  aggregateFunction?: string;

  aliasToAggregate?: string;

  constructor(private structure: Structure, private service: ReportService) {}

  get tables() {
    return this.structure.getAllTables();
  }

  private prepareData(): ReportRequest {
    const request: ReportRequest = {
      root: this.root!,
      joinSet: this.joinSet,
      columnSet: Array.from(this.dataMap.values())
        .map((data) => data.columnSet)
        .flat(),
      whereSet: Array.from(this.dataMap.values())
        .map((data) => data.whereSet)
        .flat(),
    };

    if (this.isGrouping) {
      if (!this.tableToAggregate && !this.columnToAggregate && !this.aggregateFunction) {
        console.error('Aggregation parameters are not set');
        return request;
      }
      const groupByColumns = Array.from(this.dataMap.values())
        .map((data) => data.groupBy)
        .flat();

      if (groupByColumns.length > 0) {
        request.groupBy = {
          columnSet: groupByColumns,
          aggregation: {
            table: this.tableToAggregate!,
            field: this.columnToAggregate!,
            aggregation: this.structure.getAggregationFunction(this.aggregateFunction!),
            alias: this.aliasToAggregate
          },
        } as GroupBy;
      }
    }

    return request;
  }

  getAttributes(table: TableType): Field[] {
    return this.structure.getAllAtributes(table);
  }

  getAggFunctions(table?: TableType, field?: Field): AggregationFunction[] {
    if (table && field) {
      return this.structure.getAggFunctionsByColumn(table, field);
    }
    else {
      return [];
    }
  }

  isRoot(table: TableType): boolean {
    const idx = this.addedTables.indexOf(table);
    return idx === 0;
  }

  onGrouping($event: boolean) {
    this.isGrouping = $event;
  }

  onRootChange(target: Event) {
    this.root = target.target instanceof HTMLSelectElement ? (target.target.value as TableType) : undefined;
    this.addedTables = [this.root!];
    this.joinSet = [];
    this.dataMap.clear();
    this.dataMap.set(this.root!, {
      columnSet: [],
      whereSet: [],
    });
  }

  onJoinTable(fromTable: TableType, join: { table: TableType; joinType: string }) {
    if (!this.addedTables.includes(join.table)) {
      this.addedTables.push(join.table);
      const joinSet = {
        from: fromTable,
        to: join.table,
        type: join.joinType as JoinType,
      } as Join;
      this.joinSet.push(joinSet);
    }
  }

  onRemoveTable(table: TableType) {
    const idx = this.addedTables.indexOf(table);
    if (idx > -1) {
      this.addedTables.splice(idx, 1);
    }
    this.dataMap.delete(table);
    this.joinSet = this.joinSet.filter((join) => join.to !== table && join.from !== table);
  }

  onChangeDataConstructor($event: Map<TableType, DataConstructor>) {
    const changes = $event;
    changes.forEach((data, table) => {
      this.dataMap.set(table, data);
    });
  }

  onSearch() {
    if (!this.root) {
      console.error('Root table is not selected');
      return;
    }

    const body = this.prepareData();

    console.log('Search request body:', body);

    this.service.getReportData(body).then((response) => (this.response = response));
  }
}
