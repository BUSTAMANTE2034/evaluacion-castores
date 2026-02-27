import { useState } from 'react'
import type {
  Producto,
  ProductoRequest,
} from '../models/producto'
import { apiFetch } from './client'

export const useCrearProducto = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const crearProducto = async (body: ProductoRequest) => {
    setLoading(true)
    setError(null)

    try {
      const data = await apiFetch<Producto>('/productos', {
        method: 'POST',
        body,
      })

      return data
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('Error al crear producto')
      }
      throw err
    } finally {
      setLoading(false)
    }
  }

  return {
    crearProducto,
    loading,
    error,
  }
}