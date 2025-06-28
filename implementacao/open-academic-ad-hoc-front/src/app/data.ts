import { ReportResponse } from "./model/report";


export const data: ReportResponse = {
  first:
    'select w1_0.title,a2_0.name,w1_0.type,w1_0.fwci from tb_works w1_0 join tb_authorships a1_0 on w1_0.id=a1_0.work_id join tb_authors a2_0 on a2_0.id=a1_0.author_id where w1_0.type=?',
  second: [
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Masatoshi Makuuchi',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Juan Pekolj',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'René Vonlanthen',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Ksenija Slankamenac',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Eduardo de Santibáñes',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Rolf Graf',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Daniel Dindo',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Claudio Bassi',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Richard D. Schulick',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Jean‐Nicolas Vauthey',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'John L. Cameron',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Jeffrey Barkun',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Pierre A. Clavien',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Michelle Oliveira',
    },
    {
      score: 67.27,
      tipo: 'article',
      titulo: 'The Clavien-Dindo Classification of Surgical Complications',
      nome: 'Robert Padbury',
    },
    {
      score: 67.84,
      tipo: 'article',
      titulo:
        'Very high resolution interpolated climate surfaces for global land areas',
      nome: 'Susan E. Cameron',
    },
  ],
};
