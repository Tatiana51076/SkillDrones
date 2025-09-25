export const authUtils = {
  clearAuthData: (): void => {
    localStorage.removeItem('had_successful_auth');
    localStorage.removeItem('last_auth_time');
  },
  
  setAuthSuccess: (): void => {
    localStorage.setItem('last_auth_time', Date.now().toString());
  }
};