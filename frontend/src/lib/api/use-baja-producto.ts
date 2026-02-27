import { useState } from 'react'
import type { Producto, ApiResponse } from '../models/producto'
import { apiFetch } from './client'

export const useBajaProducto = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const bajaProducto = async (id: number) => {
    setLoading(true)
    setError(null)

    try {
      const response = await apiFetch<ApiResponse<Producto>>(
        `/productos/${id}/baja`,
        {
          method: 'PATCH',
        }
      )

      return response.data
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('Error al dar de baja el producto')
      }
      throw err
    } finally {
      setLoading(false)
    }
  }

  return {
    bajaProducto,
    loading,
    error,
  }
}