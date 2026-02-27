import { useState } from 'react'
import type {
  Producto,
  CantidadRequest,
  ApiResponse,
} from '../models/producto'
import { apiFetch } from './client'

export const useEntradaProducto = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const entradaProducto = async (
    id: number,
    body: CantidadRequest
  ) => {
    setLoading(true)
    setError(null)

    try {
      const response = await apiFetch<ApiResponse<Producto>>(
        `/productos/${id}/entrada`,
        {
          method: 'PATCH',
          body,
        }
      )

      return response.data
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('Error al aumentar inventario')
      }
      throw err
    } finally {
      setLoading(false)
    }
  }

  return {
    entradaProducto,
    loading,
    error,
  }
}