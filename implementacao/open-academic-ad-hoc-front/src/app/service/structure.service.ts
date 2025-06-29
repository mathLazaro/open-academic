import { Injectable } from '@angular/core';
import { Aggregation, JoinType, Operator, TableType } from '../model/enums';
import {
  AuthorField,
  authorFieldType,
  DomainField,
  domainFieldType,
  Field,
  FieldField,
  fieldFieldType,
  OrganizationField,
  organizationFieldType,
  RoleField,
  roleFieldType,
  TopicField,
  topicFieldType,
  WorkField,
  workFieldType,
  WorkOrganizationField,
  workOrganizationFieldType,
  WorkTopicField,
  workTopicFieldType,
} from '../model/field';

class Node {
  public table!: TableType;
  public neighbors: Array<Node> = [];

  constructor(table: TableType) {
    this.table = table;
  }
}

@Injectable({
  providedIn: 'root',
})
export class Structure {
  private nodeMap: Map<TableType, Node> = new Map();

  private static instance: Structure;

  constructor() {
    if (Structure.instance) {
      return Structure.instance;
    }
    Structure.instance = this;
    this.createGraph();
  }

  private createGraph(): void {
    const author = new Node(TableType.AUTHOR);
    const domain = new Node(TableType.DOMAIN);
    const field = new Node(TableType.FIELD);
    const organization = new Node(TableType.ORGANIZATION);
    const role = new Node(TableType.ROLE);
    const topic = new Node(TableType.TOPIC);
    const work = new Node(TableType.WORK);
    const authorship = new Node(TableType.AUTHORSHIP);
    const workOrganization = new Node(TableType.WORK_ORGANIZATION);
    const workTopic = new Node(TableType.WORK_TOPIC);

    // Add all nodes to the map
    this.nodeMap.set(TableType.AUTHOR, author);
    this.nodeMap.set(TableType.DOMAIN, domain);
    this.nodeMap.set(TableType.FIELD, field);
    this.nodeMap.set(TableType.ORGANIZATION, organization);
    this.nodeMap.set(TableType.ROLE, role);
    this.nodeMap.set(TableType.TOPIC, topic);
    this.nodeMap.set(TableType.WORK, work);
    this.nodeMap.set(TableType.AUTHORSHIP, authorship);
    this.nodeMap.set(TableType.WORK_ORGANIZATION, workOrganization);
    this.nodeMap.set(TableType.WORK_TOPIC, workTopic);

    author.neighbors.push(authorship);
    authorship.neighbors.push(author, work, organization);
    work.neighbors.push(workOrganization, workTopic, authorship);
    organization.neighbors.push(workOrganization, authorship, role, domain);
    role.neighbors.push(organization);
    domain.neighbors.push(field, organization);
    field.neighbors.push(domain, topic);
    topic.neighbors.push(field, workTopic);
    workOrganization.neighbors.push(work, organization);
    workTopic.neighbors.push(work, topic);
  }

  public getAllTables(): Array<TableType> {
    return [
      TableType.AUTHOR,
      TableType.DOMAIN,
      TableType.FIELD,
      TableType.ORGANIZATION,
      TableType.ROLE,
      TableType.TOPIC,
      TableType.WORK,
      TableType.AUTHORSHIP,
      TableType.WORK_ORGANIZATION,
      TableType.WORK_TOPIC,
    ];
  }

  public getAllAtributes(table: TableType): Field[] {
    switch (table) {
      case TableType.AUTHOR:
        return Object.keys(AuthorField) as AuthorField[];
      case TableType.DOMAIN:
        return Object.keys(DomainField) as DomainField[];
      case TableType.FIELD:
        return Object.keys(FieldField) as FieldField[];
      case TableType.ORGANIZATION:
        return Object.keys(OrganizationField) as OrganizationField[];
      case TableType.ROLE:
        return Object.keys(RoleField) as RoleField[];
      case TableType.TOPIC:
        return Object.keys(TopicField) as TopicField[];
      case TableType.WORK:
        return Object.keys(WorkField) as WorkField[];
      case TableType.WORK_ORGANIZATION:
        return Object.keys(WorkOrganizationField) as WorkOrganizationField[];
      case TableType.WORK_TOPIC:
        return Object.keys(WorkTopicField) as WorkTopicField[];
      default:
        return [];
    }
  }

  public getOperatorsByColumn(table: TableType, field: Field): Operator[] {
    switch (table) {
      case TableType.AUTHOR:
        return (authorFieldType[field as AuthorField] as Operator[]) || [];
      case TableType.DOMAIN:
        return (domainFieldType[field as DomainField] as Operator[]) || [];
      case TableType.FIELD:
        return (fieldFieldType[field as FieldField] as Operator[]) || [];
      case TableType.ORGANIZATION:
        return (organizationFieldType[field as OrganizationField] as Operator[]) || [];
      case TableType.ROLE:
        return (roleFieldType[field as RoleField] as Operator[]) || [];
      case TableType.TOPIC:
        return (topicFieldType[field as TopicField] as Operator[]) || [];
      case TableType.WORK:
        return (workFieldType[field as WorkField] as Operator[]) || [];
      case TableType.WORK_ORGANIZATION:
        return (workOrganizationFieldType[field as WorkOrganizationField] as Operator[]) || [];
      case TableType.WORK_TOPIC:
        return (workTopicFieldType[field as WorkTopicField] as Operator[]) || [];
      default:
        return [];
    }
  }

  public getAggFunctions(): string[] {
    return Object.values(Aggregation).filter(v => typeof v === 'string');
  }

  public getAllJoinTypes(): JoinType[] {
    return [JoinType.INNER, JoinType.LEFT, JoinType.RIGHT];
  }

  public getAllNeighbors(table: TableType): Array<TableType> {
    const node = this.getNodeByTable(table);
    return node!.neighbors.map((node) => node.table);
  }

  private getNodeByTable(table: TableType): Node | undefined {
    return this.nodeMap.get(table);
  }

  public isNeighbor(from: TableType, to: TableType): boolean {
    const fromNode = this.getNodeByTable(from);
    const toNode = this.getNodeByTable(to);
    if (!fromNode || !toNode) {
      return false;
    }
    return fromNode.neighbors.includes(toNode);
  }
}
