import { useCallback, useEffect, useState } from 'react'
import type {
  Producto,
  ProductosPaginationResponse,
} from '../models/producto'
import { apiFetch } from '../api/client'

export interface OptionsGetProductos {
  initialPage?: number
  initialPerPage?: number
  initialEstatus?: boolean | null
}

export const useGetProductos = ({
  initialPage = 1,
  initialPerPage = 10,
  initialEstatus = null,
}: OptionsGetProductos = {}) => {
  //Estados principales
  const [productos, setProductos] = useState<Producto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Filtros
  const [current_page, setCurrentPage] = useState<number>(initialPage)
  const [per_page, setPerPage] = useState<number>(initialPerPage)
  const [estatus, setEstatus] = useState<boolean | null>(initialEstatus)

  // Paginación
  const [total, setTotal] = useState<number>(0)
  const [pages, setPages] = useState<number>(0)
  const [hasNext, setHasNext] = useState(false)
  const [hasPrev, setHasPrev] = useState(false)
  const [nextPage, setNextPage] = useState<number | null>(null)
  const [prevPage, setPrevPage] = useState<number | null>(null)

  // Función principal
  const getProductos = useCallback(async () => {
    setLoading(true)
    setError(null)

    const params = new URLSearchParams()

    params.append('page', String(current_page))
    params.append('per_page', String(per_page))
    if (estatus !== null) params.append('estatus', String(estatus))

    const url = `/productos?${params.toString()}`

    try {
      const data = await apiFetch<ProductosPaginationResponse>(url, {
        method: 'GET',
      })

      const lista = Array.isArray(data.productos)
        ? data.productos
        : []

      const pagination = data.pagination

      setProductos(lista)
      setTotal(pagination.total)
      setPages(pagination.pages)
      setCurrentPage(pagination.current_page)
      setPerPage(pagination.per_page)
      setHasNext(pagination.has_next)
      setHasPrev(pagination.has_prev)
      setNextPage(pagination.next_page)
      setPrevPage(pagination.prev_page)
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('Error desconocido')
      }
    } finally {
      setLoading(false)
    }
  }, [current_page, per_page, estatus])

  // Auto ejecución
  useEffect(() => {
    getProductos()
  }, [getProductos])

  // Navegación
  const goNext = useCallback(() => {
    if (nextPage !== null) setCurrentPage(nextPage)
  }, [nextPage])

  const goPrev = useCallback(() => {
    if (prevPage !== null) setCurrentPage(prevPage)
  }, [prevPage])

  return {
    productos,
    loading,
    error,

    // paginación
    per_page,
    current_page,
    total,
    pages,
    hasNext,
    hasPrev,
    nextPage,
    prevPage,

    goNext,
    goPrev,

    // filtros
    estatus,
    setEstatus: (value: boolean | null) => {
      setEstatus(value)
      setCurrentPage(1)
    },

    // refetch manual
    getProductos,
  }
}