// types/route.ts
import { ROLES } from '../constants/rolesConst';
import type { ComponentType } from 'react';

export type UserRole = typeof ROLES[keyof typeof ROLES];

export interface RouteConfig {
  path: string;
  component: ComponentType;
  allowedRoles: UserRole[];
  title?: string;
  hideFromNav?: boolean;
  exact?: boolean;
  icon?: string;
  isPublic?: boolean;
}