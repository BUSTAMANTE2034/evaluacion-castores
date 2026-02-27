export type TipoMovimiento = 'ENTRADA' | 'SALIDA'

export interface ProductoSimple {
  idProducto: number
  nombre: string
  precio: number          
  cantidad: number
  estatus: boolean
  fechaCreacion: string   
  fechaActualizacion: string
}

export interface UsuarioSimple {
  idUsuario: number
  nombre: string
  correo: string
  estatus: boolean
  fechaCreacion: string
}


export interface Movimiento {
  idMovimiento: number
  tipoMovimiento: TipoMovimiento
  cantidad: number
  fechaCreacion: string
  producto: ProductoSimple
  usuario: UsuarioSimple
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

export interface MovimientosPaginationResponse {
  message: string
  movimientos: Movimiento[]
  pagination: PaginationInfo
}



export interface MovimientosPaginationParams {
  page?: number
  per_page?: number
  tipo?: TipoMovimiento
}