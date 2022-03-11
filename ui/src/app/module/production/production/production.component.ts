import { Component, OnInit } from '@angular/core';
import { ProductionDTO } from '../../../data/productiondto'
import { ProductDTO } from '../../../data/productdto'
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-production',
  templateUrl: './production.component.html',
  styleUrls: ['./production.component.scss']
})
export class ProductionComponent implements OnInit {
  public displayedColumns: string[] = ['id', 'pcbId', 'quantity', 'startDate,', 'endDate', 'delete'];
  public productions: ProductionDTO[] = [];
  public products: ProductDTO[] = [];
  public selected: number = -1;

  mysql = require('mysql');
  connection = this.mysql.createConnection({
    host     : 'localhost',
    user     : 'root',
    password : 'admin',
    database : 'cs_beugro'
  });

  constructor(formControl: FormControl) { }

  ngOnInit(): void {
    this.loadProductionsFromDB();
    this.loadProductsFromDB();
  }

  private loadProductionsFromDB() {

    this.connection.connect();

    let whereClause = this.selected != -1 ? ' WHRER pcbId = ' + this.selected : '';

    this.connection.query('SELECT * from production ' + whereClause, (error: any, results: { production: any; }[], fields: any) => {
        if (error)
          throw error;
        results.forEach(r => {
          let productiondto: ProductionDTO = new ProductionDTO();
          productiondto.id = r.production.id;
          productiondto.pcbId = r.production.pcbId;
          productiondto.quantity = r.production.quantity;
          productiondto.startDate = r.production.startDate;
          productiondto.endDate = r.production.endDate;
          this.productions.push(productiondto);
        })  
      });

    this.connection.end();
  }

  private loadProductsFromDB() {

    this.connection.connect();

     this.connection.query('SELECT * from products', (error: any, results: { products: any; }[], fields: any) => {
        if (error)
          throw error;
        results.forEach(r => {
          let productdto: ProductDTO = new ProductDTO();
          productdto.id = r.products.id;
          productdto.pcb = r.products.pcb;
          this.products.push(productdto);
        })  
      });

    this.connection.end();
  }

  public delete(id: number): void {
    if (confirm('Are you sure you want to delete this record?')) {
      this.connection.connect();
      this.connection.query('DELETE from production WHERE id = ' + id, (error: any, results: { products: any; }[], fields: any) => {
        if (error)
          throw error;
        this.loadProductionsFromDB()
      });
    }
  }
}
