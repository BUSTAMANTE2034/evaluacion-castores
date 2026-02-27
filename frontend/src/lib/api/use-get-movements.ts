import { useCallback, useEffect, useState } from 'react'
import type {
  Movimiento,
  MovimientosPaginationResponse,
  TipoMovimiento,
} from '../models/movimiento'
import { apiFetch } from '../api/client'

export interface OptionsGetMovimientos {
  initialPage?: number
  initialPerPage?: number
  initialTipo?: TipoMovimiento | null
}

export const useGetMovimientos = ({
  initialPage = 1,
  initialPerPage = 20,
  initialTipo = null,
}: OptionsGetMovimientos = {}) => {
  // Estados principales
  const [movimientos, setMovimientos] = useState<Movimiento[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Filtros
  const [current_page, setCurrentPage] = useState<number>(initialPage)
  const [per_page, setPerPage] = useState<number>(initialPerPage)
  const [tipo, setTipo] = useState<TipoMovimiento | null>(initialTipo)

  // Paginación
  const [total, setTotal] = useState<number>(0)
  const [pages, setPages] = useState<number>(0)
  const [hasNext, setHasNext] = useState(false)
  const [hasPrev, setHasPrev] = useState(false)
  const [nextPage, setNextPage] = useState<number | null>(null)
  const [prevPage, setPrevPage] = useState<number | null>(null)

  // Función principal
  const getMovimientos = useCallback(async () => {
    setLoading(true)
    setError(null)

    const params = new URLSearchParams()
    params.append('page', String(current_page))
    params.append('per_page', String(per_page))
    if (tipo) params.append('tipo', tipo)

    const url = `/movimientos?${params.toString()}`

    try {
      const data = await apiFetch<MovimientosPaginationResponse>(url, {
        method: 'GET',
      })

      const lista = Array.isArray(data.movimientos)
        ? data.movimientos
        : []

      const pagination = data.pagination

      setMovimientos(lista)
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
  }, [current_page, per_page, tipo])

  //Auto ejecución
  useEffect(() => {
    getMovimientos()
  }, [getMovimientos])

  // Navegación
  const goNext = useCallback(() => {
    if (nextPage !== null) setCurrentPage(nextPage)
  }, [nextPage])

  const goPrev = useCallback(() => {
    if (prevPage !== null) setCurrentPage(prevPage)
  }, [prevPage])

  return {
    movimientos,
    loading,
    error,
per_page,
    // paginación
    current_page,
    total,
    pages,
    hasNext,
    hasPrev,
    nextPage,
    prevPage,
    goNext,
    goPrev,

    // filtro
    tipo,
    setTipo: (value: TipoMovimiento | null) => {
      setTipo(value)
      setCurrentPage(1)
    },

    // refetch manual
    getMovimientos,
  }
}