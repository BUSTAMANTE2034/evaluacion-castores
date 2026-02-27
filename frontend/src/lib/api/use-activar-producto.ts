import { useState } from 'react'
import type { Producto, ApiResponse } from '../models/producto'
import { apiFetch } from './client'

export const useActivarProducto = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const activarProducto = async (id: number) => {
    setLoading(true)
    setError(null)

    try {
      const response = await apiFetch<ApiResponse<Producto>>(
        `/productos/${id}/reactivar`,
        {
          method: 'PATCH',
        }
      )

      return response.data
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('Error al reactivar el producto')
      }
      throw err
    } finally {
      setLoading(false)
    }
  }

  return {
    activarProducto,
    loading,
    error,
  }
}