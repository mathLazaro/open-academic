import { Injectable } from '@angular/core';
import { ReportRequest, ReportResponse } from '../model/report';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  constructor() {}

  async getReportData(request: ReportRequest): Promise<ReportResponse> {
    const response = await fetch('http://localhost:8080', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });
    if (!response.ok) {
      throw new Error('Error fethching report data: ' + response.status);
    }
    return response.json();
  }
}
