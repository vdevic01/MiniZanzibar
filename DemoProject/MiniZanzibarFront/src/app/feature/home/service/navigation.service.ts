import { Injectable } from '@angular/core';
import {NavItem} from "../model/nav-item";

@Injectable({
  providedIn: 'root'
})
export class NavigationService {
  constructor() {}

  getNavItems(): NavItem[] {
    return [
      { routeLink: '/home/my-files/', label: 'My Files', icon: 'fa fa-solid fa-folder-open' },
      { routeLink: '/home/files-with-permissions/', label: 'Files with permissions', icon: 'fa fa-user-group' }
    ];
  }
}
