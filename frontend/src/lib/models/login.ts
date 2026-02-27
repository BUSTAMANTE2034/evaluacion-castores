export interface PostLogin {
  correo: string
  contrasena: string
}

export interface LoginResponse {
  token: string
  nombre: string
  correo: string
  rol: string
  estatus: boolean
}

export interface LogoutResponse {
  message: string
  status: boolean
}