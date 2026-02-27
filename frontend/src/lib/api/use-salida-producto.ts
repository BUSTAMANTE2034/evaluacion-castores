import { useState } from 'react'
import type {
  Producto,
  CantidadRequest,
  ApiResponse,
} from '../models/producto'
import { apiFetch } from './client'

export const useSalidaProducto = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const salidaProducto = async (
    id: number,
    body: CantidadRequest
  ) => {
    setLoading(true)
    setError(null)

    try {
      const response = await apiFetch<ApiResponse<Producto>>(
        `/productos/${id}/salida`,
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
        setError('Error al reducir inventario')
      }
      throw err
    } finally {
      setLoading(false)
    }
  }

  return {
    salidaProducto,
    loading,
    error,
  }
}