import { animate, style, transition, trigger } from '@angular/animations';
import { Component, HostListener, OnInit } from '@angular/core';

/*For working drop menu to add angular animation*/
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  animations: [trigger('slider', [transition('void=>*', [style({ transform: 'translateX(-100%)' }),
  animate('0.35s')]),
  transition('*=>void', [animate('0.35s'), style({ transform: 'translateX(-100%)' })
  ]
  )
  ])]
})
export class HeaderComponent implements OnInit {
  sliderState = false;
  public innerWidth: any;
  /*reloading and in the start this funtion run and load the data*/
  ngOnInit() {
    this.innerWidth = window.innerWidth;
  }
  /*create event to read screen size*/
  @HostListener('window:resize', ['$event'])
  onResize() {
    if (this.innerWidth != window.innerWidth) {
      this.innerWidth = window.innerWidth;
      if (this.innerWidth > 800 && this.sliderState) {
        this.sliderState = false;
      }
    }
  }
}
