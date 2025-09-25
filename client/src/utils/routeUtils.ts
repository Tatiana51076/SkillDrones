import type { RouteConfig, UserRole } from '../types/routeTypes';
import { routeConfig } from '../config/routesConfig';

export const getAccessibleRoutes = (userRole: UserRole): RouteConfig[] => {
  return routeConfig.filter(route => 
    route.allowedRoles.includes(userRole)
  );
};

// Функция для отрисовки ссылок в navmenu
export const getNavRoutes = (userRole: UserRole): RouteConfig[] => {
  return getAccessibleRoutes(userRole)
    .filter(route => !route.hideFromNav && route.title &&
      route.path !== '/account');
};

// Функция для получения публичных маршрутов
export const getPublicRoutes = (): RouteConfig[] => {
  return routeConfig.filter(route => route.isPublic);
};

// Функция для получения защищенных маршрутов
export const getProtectedRoutes = (userRole: UserRole): RouteConfig[] => {
  return routeConfig.filter(route => 
    !route.isPublic && route.allowedRoles.includes(userRole)
  );
};