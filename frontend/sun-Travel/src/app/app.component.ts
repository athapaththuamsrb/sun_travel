import { Component, OnInit } from '@angular/core';

declare var particlesJS: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
// export class AppComponent implements OnInit {
//   title = 'sun-Travel';
//   ngOnInit() {
//     particlesJS.load('particlesCSS', '/assets/particles.json', function () { console.log('callback - particles.js config loaded'); });
//   }
// }
export class AppComponent implements OnInit {
  title = 'sun-Travel';
  ngOnInit(): void {
    particlesJS.load('particles-js', '/assets/particles.json', function () { console.log('callback - particles.js config loaded'); });
  }
}

