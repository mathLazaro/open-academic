import { Component } from '@angular/core';
import { DashboardReportComponent } from './component/dashboard-report/dashboard-report.component';
import { data } from './data';
import { DataConstructor, Join, ReportResponse } from './model/report';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { Structure } from './service/structure.service';
import { JoinType, TableType } from './model/enums';
import { CardTableComponent } from './component/card-table/card-table.component';

@Component({
  selector: 'app-root',
  imports: [DashboardReportComponent, CardTableComponent, MatFormFieldModule, MatSelectModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  response: ReportResponse = data;

  root?: TableType;

  addedTables: TableType[] = [];

  dataMap = new Map<TableType, DataConstructor>();

  joinSet: Join[] = [];

  constructor(private structure: Structure) {}

  get tables() {
    return this.structure.getAllTables();
  }

  isRoot(table: TableType): boolean {
    const idx = this.addedTables.indexOf(table);
    return idx === 0;
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
      if (this.dataMap.has(table)) {
        const existingData = this.dataMap.get(table)!;
        existingData.columnSet.push(...data.columnSet); // TODO handle duplicates
        existingData.whereSet.push(...data.whereSet);
      } else {
        this.dataMap.set(table, data);
      }
    });
    
  }
}
