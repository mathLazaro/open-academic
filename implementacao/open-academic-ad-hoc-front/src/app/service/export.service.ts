import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ExportService {
  constructor() {}

  exportCsv(data: Array<Record<string, any>>, columns: string[]) {
      if (!data.length) return;
      const csvRows = [columns.join(',')];
      for (const row of data) {
        const values = columns.map((col) => {
          const val = row[col];
          if (val === null || val === undefined) return '';
          return '"' + String(val).replace(/"/g, '""') + '"';
        });
        csvRows.push(values.join(','));
      }
      const csvContent = csvRows.join('\n');
      const blob = new Blob([csvContent], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'relatorio.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    }
  
    exportPdf(columns: string[], data: Array<Record<string, any>>) {
      const doc = new jsPDF({ orientation: 'landscape' });
      const rows = data.map((row) => columns.map((col) => row[col]));
  
      autoTable(doc, {
        head: [columns],
        body: rows,
        styles: { fontSize: 10 },
      });
  
      doc.save('relatorio.pdf');
    }

}
