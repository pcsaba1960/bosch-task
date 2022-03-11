import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-center',
  templateUrl: './center.component.html',
  styleUrls: ['./center.component.scss']
})
export class CenterComponent implements OnInit {
  public password: string = '';
  public color: string = ''
  colors: string[] = ['red', 'green', 'blue', 'white', 'grey']

  constructor() {}

  ngOnInit(): void {
    this.change('') ;
  }

  public change(init: string): void {
    let s : string = '';
    if (init.length == 0) {
      s = new Array(12).map(() => String.fromCharCode(Math.random()*86+40)).join("");
    }
    else {
      s = new String().replace('*', this.password);
    }
    this.password = s;
    this.color = this.colors[ Math.random()*(this.colors.length-1)] 
  }

}
