export interface User {
  id: number;
  firstName: string;
  lastName: string;
  password: string;
  email: string;
  phone: string;
  token?: string;
  roles?: string[];
}
