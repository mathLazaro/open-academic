import { Component, Input } from '@angular/core';
import { ReportResponse } from '../../model/report';

@Component({
  selector: 'app-dashboard-report',
  imports: [],
  templateUrl: './dashboard-report.component.html',
  styleUrl: './dashboard-report.component.css',
})
export class DashboardReportComponent {
  @Input()
  public response?: ReportResponse;

  get data(): Array<Record<string, any>> {
    return this.response?.second || [];
  }

  get sql(): string {
    return this.response?.first || '';
  }

  get columns(): string[] {
    return Object.keys(this.data[0]) || [];
  }
}
