import { Component, Input } from '@angular/core';
import { ReportResponse } from '../../model/report';
import { ExportService } from '../../service/export.service';

@Component({
  selector: 'app-dashboard-report',
  imports: [],
  templateUrl: './dashboard-report.component.html',
  styleUrl: './dashboard-report.component.css',
})
export class DashboardReportComponent {
  constructor(private exportService: ExportService) {}

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

  exportCsv() {
    this.exportService.exportCsv(this.data, this.columns);
  }

  exportPdf() {
    this.exportService.exportPdf(this.columns, this.data);
  }
}
