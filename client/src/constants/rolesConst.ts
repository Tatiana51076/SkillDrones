export const ROLES = {
  ADMIN: 'admin',
  MANAGER: 'manager',
  USER: 'user',
};

export const ROLE_HIERARCHY = {
  [ROLES.ADMIN]: [ROLES.ADMIN, ROLES.MANAGER, ROLES.USER],
  [ROLES.MANAGER]: [ROLES.MANAGER, ROLES.USER],
  [ROLES.USER]: [ROLES.USER],
};