

export interface Producto {
  idProducto: number
  nombre: string
  precio: number            
  cantidad: number
  estatus: boolean
  creadoPorNombre: string | null
  actualizadoPorNombre: string | null
  fechaCreacion: string 
  fechaActualizacion: string
}


// Crear / Actualizar producto
export interface ProductoRequest {
  nombre: string
  precio: number
}

// Entrada / Salida inventario
export interface CantidadRequest {
  cantidad: number
}

// Parámetros para listado paginado
export interface ProductosPaginationParams {
  page?: number
  per_page?: number
  estatus?: boolean
}


export type ProductoResponse = Producto

export interface ApiResponse<T> {
  status: number
  mensaje: string
  timestamp: string
  data: T
}


export interface PaginationInfo {
  total: number
  pages: number
  current_page: number
  per_page: number
  has_next: boolean
  has_prev: boolean
  next_page: number | null
  prev_page: number | null
}

// Response del GET /productos
export interface ProductosPaginationResponse {
  message: string
  productos: Producto[]
  pagination: PaginationInfo
}