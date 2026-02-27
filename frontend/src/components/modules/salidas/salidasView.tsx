import { useEffect, useState } from 'react'
import { MoreVertical } from 'lucide-react'

import { useGetProductos } from '../../../lib/api/use-obtener-productos'
import { useSalidaProducto } from '../../../lib/api/use-salida-producto'

import Modal from '../../ui/modal'

const SalidasView = () => {
  const {
    productos,
    total,
    pages,
    current_page,
    per_page,
    hasNext,
    hasPrev,
    goNext,
    goPrev,
    getProductos,
  } = useGetProductos({
    initialPage: 1,
    initialPerPage: 20,
    initialEstatus: true, // SOLO ACTIVOS
  })

  const { salidaProducto, loading } = useSalidaProducto()

  const [menuOpen, setMenuOpen] = useState<number | null>(null)
  const [openSalida, setOpenSalida] = useState<number | null>(null)
  const [cantidad, setCantidad] = useState('')

  const [notification, setNotification] = useState<{
    message: string
    type: 'success' | 'error'
  } | null>(null)

  const showNotification = (message: string, type: 'success' | 'error') => {
    setNotification({ message, type })
    setTimeout(() => setNotification(null), 3000)
  }

  useEffect(() => {
    getProductos()
  }, [])

  const handleSalida = async (id: number) => {
    try {
      await salidaProducto(id, {
        cantidad: Number(cantidad),
      })

      showNotification('Cantidad sacada correctamente', 'success')
      setOpenSalida(null)
      setCantidad('')
      getProductos()
    } catch (err: any) {
      showNotification(err.message || 'Error al realizar salida', 'error')
    }
  }

  return (
    <div className="w-full relative">
                <h1 className="text-2xl py-4 font-semibold text-[#1f2937]">Salida de productos</h1>
    
      {/* NOTIFICACIÓN */}
      {notification && (
        <div
          className={`fixed top-6 right-6 px-6 py-3 rounded-xl shadow-xl text-white z-100 ${
            notification.type === 'success' ? 'bg-[#4156c2]' : 'bg-[#c72628]'
          }`}
        >
          {notification.message}
        </div>
      )}

      {/* TABLA */}
      <div className="bg-white rounded-xl shadow-md overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-[#1f2937] text-white">
            <tr>
              <th className="p-3 text-left">Nombre</th>
              <th className="p-3 text-left">Precio</th>
              <th className="p-3 text-left">Cantidad Disponible</th>
              <th className="p-3"></th>
            </tr>
          </thead>

          <tbody>
            {productos.map((p: any) => (
              <tr key={p.idProducto} className="border-b hover:bg-gray-50">
                <td className="p-3">{p.nombre}</td>
                <td className="p-3">${p.precio}</td>
                <td className="p-3">{p.cantidad}</td>

                <td className="p-3 relative cursor-pointer">
                  <button onClick={() => setMenuOpen(p.idProducto)} className='cursor-pointer'>
                    <MoreVertical size={18} />
                  </button>

                  {menuOpen === p.idProducto && (
                    <div className="absolute right-10 top-2 bg-white shadow-lg rounded-lg text-sm z-10">
                      <button
                        onClick={() => {
                          setOpenSalida(p.idProducto)
                          setMenuOpen(null)
                        }}
                        className="cursor-pointer block px-4 py-2 hover:bg-gray-100 w-full text-left"
                      >
                        Salida
                      </button>
                    </div>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {/* FOOTER PAGINADO */}
        <div className="flex justify-end items-center p-4 bg-gray-50 text-sm gap-4">
          <span>Total: {total}</span>
          <span>
            Página: {current_page} / {pages}
          </span>
          <span>Por página: {per_page}</span>

          <button
            disabled={!hasPrev}
            onClick={goPrev}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-40"
          >
            ←
          </button>

          <button
            disabled={!hasNext}
            onClick={goNext}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-40"
          >
            -
          </button>
        </div>
      </div>

      {/* MODAL SALIDA */}
      {openSalida && (
        <Modal title="Registrar Salida" onClose={() => setOpenSalida(null)}>
          <div>
            <p className='text-gray-800 font-medium'>
              Producto:{' '}
              <span className='text-black  font-normal'>
                {productos.find((p) => p.idProducto === openSalida)?.nombre}
              </span>
            </p>
            <p className='text-gray-800 font-medium'>
              Cantidad disponible:{' '}
              <span className='text-black  font-normal'>
                {productos.find((p) => p.idProducto === openSalida)?.cantidad}
              </span>
            </p>
          </div>

          <input
            type="number"
            placeholder="Cantidad a retirar"
            className="input"
            value={cantidad}
            onChange={(e) => setCantidad(e.target.value)}
          />

          <button
            onClick={() => handleSalida(openSalida)}
            className="btn-primary"
          >
            {loading ? 'Procesando...' : 'Confirmar Salida'}
          </button>
        </Modal>
      )}
    </div>
  )
}

export default SalidasView
