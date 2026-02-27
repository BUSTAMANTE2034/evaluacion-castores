import { useEffect, useState } from 'react'
import { MoreVertical, Plus } from 'lucide-react'

import { useGetProductos } from '../../../lib/api/use-obtener-productos'
import { useCrearProducto } from '../../../lib/api/use-crear-producto'
import { useEntradaProducto } from '../../../lib/api/use-entrada-producto'
import { useBajaProducto } from '../../../lib/api/use-baja-producto'
import { useActivarProducto } from '../../../lib/api/use-activar-producto'
import Modal from '../../ui/modal'
import { useAuth } from '../../context/authContext'

const InventarioView = () => {
  const { usuario } = useAuth()

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
  })

  const { crearProducto, loading: creating } = useCrearProducto()
  const { entradaProducto, loading: updating } = useEntradaProducto()
  const { bajaProducto, loading: deleting } = useBajaProducto()
  const { activarProducto, loading: activating } = useActivarProducto()

  const [openCreate, setOpenCreate] = useState(false)
  const [openEntrada, setOpenEntrada] = useState<number | null>(null)
  const [openBaja, setOpenBaja] = useState<number | null>(null)
  const [menuOpen, setMenuOpen] = useState<number | null>(null)

  const [nombre, setNombre] = useState('')
  const [precio, setPrecio] = useState('')
  const [cantidadEntrada, setCantidadEntrada] = useState('')

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

  // CREAR
  const handleCrear = async () => {
    try {
      await crearProducto({
        nombre,
        precio: Number(precio),
      })

      showNotification('Producto creado correctamente', 'success')
      setOpenCreate(false)
      setNombre('')
      setPrecio('')
      getProductos()
    } catch (err: any) {
      showNotification(err.message || 'Error al crear producto', 'error')
    }
  }

  // ENTRADA
  const handleEntrada = async (id: number) => {
    try {
      await entradaProducto(id, {
        cantidad: Number(cantidadEntrada),
      })

      showNotification('Inventario actualizado', 'success')
      setOpenEntrada(null)
      setCantidadEntrada('')
      getProductos()
    } catch (err: any) {
      showNotification(err.message || 'Error al actualizar', 'error')
    }
  }

  // BAJA
  const handleBaja = async (id: number) => {
    try {
      await bajaProducto(id)

      showNotification('Producto dado de baja', 'success')
      setOpenBaja(null)
      getProductos()
    } catch (err: any) {
      showNotification(err.message || 'Error al dar de baja', 'error')
    }
  }

  // ACTIVAR
  const handleActivar = async (id: number) => {
    try {
      await activarProducto(id)

      showNotification('Producto activado', 'success')
      getProductos()
    } catch (err: any) {
      showNotification(err.message || 'Error al activar', 'error')
    }
  }

  return (
    <div className="w-full relative">
      <h1 className="text-2xl py-4 font-semibold text-[#1f2937]">
        Inventario - Entrada de productos
      </h1>

      {/* NOTIFICACIONES */}
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
              <th className="p-3 text-left">Cantidad</th>
              <th className="p-3 text-left">Estatus</th>
              <th className="p-3"></th>
            </tr>
          </thead>

          <tbody>
            {productos.map((p: any) => (
              <tr key={p.idProducto} className="border-b hover:bg-gray-50">
                <td className="p-3">{p.nombre}</td>
                <td className="p-3">${p.precio}</td>
                <td className="p-3">{p.cantidad}</td>
                <td className="p-3">
                  <span
                    className={`px-2 py-1 rounded text-xs ${
                      p.estatus
                        ? 'bg-green-100 text-green-700'
                        : 'bg-red-100 text-red-700'
                    }`}
                  >
                    {p.estatus ? 'Activo' : 'Inactivo'}
                  </span>
                </td>
                {usuario?.rol === 'ADMINISTRADOR' && (
                  <td className="p-3 relative cursor-pointer">
                    <button
                      onClick={() => setMenuOpen(p.idProducto)}
                      className="cursor-pointer"
                    >
                      <MoreVertical size={18} />
                    </button>

                    {menuOpen === p.idProducto && (
                      <div className=" absolute right-4 bg-white shadow-lg rounded-lg text-sm z-10">
                        {p.estatus && (
                          <>
                            <button
                              onClick={() => {
                                setOpenEntrada(p.idProducto)
                                setMenuOpen(null)
                              }}
                              className="cursor-pointer block px-4 py-2 hover:bg-gray-100 w-full text-left"
                            >
                              Agregar
                            </button>

                            <button
                              onClick={() => {
                                setOpenBaja(p.idProducto)
                                setMenuOpen(null)
                              }}
                              className="cursor-pointer block px-4 py-2 hover:bg-gray-100 w-full text-left"
                            >
                              Dar de baja
                            </button>
                          </>
                        )}

                        {!p.estatus && (
                          <button
                            onClick={() => {
                              handleActivar(p.idProducto)
                              setMenuOpen(null)
                            }}
                            className="block px-4 py-2 hover:bg-gray-100 w-full text-left cursor-pointer"
                          >
                            Activar
                          </button>
                        )}
                      </div>
                    )}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>

        {/* FOOTER */}
        <div className="flex justify-between items-center p-4 bg-gray-50 text-sm">
          {usuario?.rol != 'ADMINISTRADOR' ? (
            <>
              <div></div>
            </>
          ) : (
            <button
              onClick={() => setOpenCreate(true)}
              className="cursor-pointer flex items-center gap-2 bg-[#4156c2] text-white px-4 py-2 rounded-lg hover:opacity-90"
            >
              <Plus size={18} /> Agregar
            </button>
          )}

          <div className="flex items-center gap-4">
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
      </div>

      {/* MODALES */}

      {openCreate && (
        <Modal title="Nuevo Producto" onClose={() => setOpenCreate(false)}>
          <input
            placeholder="Nombre"
            className="input"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
          />
          <input
            placeholder="Precio"
            type="number"
            className="input"
            value={precio}
            onChange={(e) => setPrecio(e.target.value)}
          />

          <button onClick={handleCrear} className="btn-primary">
            {creating ? 'Creando...' : 'Crear'}
          </button>
        </Modal>
      )}

      {openEntrada && (
        <Modal title="Agregar Inventario" onClose={() => setOpenEntrada(null)}>
          <div>
            <p className="text-gray-800 font-medium">
              Producto:{' '}
              <span className="text-black  font-normal">
                {productos.find((p) => p.idProducto === openEntrada)?.nombre}
              </span>
            </p>
            <p className="text-gray-800 font-medium">
              Cantidad disponible:{' '}
              <span className="text-black  font-normal">
                {productos.find((p) => p.idProducto === openEntrada)?.cantidad}
              </span>
            </p>
          </div>
          <input
            type="number"
            placeholder="Cantidad"
            className="input"
            value={cantidadEntrada}
            onChange={(e) => setCantidadEntrada(e.target.value)}
          />

          <button
            onClick={() => handleEntrada(openEntrada)}
            className="btn-primary"
          >
            {updating ? 'Guardando...' : 'Confirmar'}
          </button>
        </Modal>
      )}

      {openBaja && (
        <Modal title="Confirmar baja" onClose={() => setOpenBaja(null)}>
          <p className="mb-4 text-sm">
            ¿Seguro que deseas dar de baja este producto?
          </p>

          <button
            onClick={() => handleBaja(openBaja)}
            className="bg-[#c72628] text-white px-4 py-2 rounded"
          >
            {deleting ? 'Procesando...' : 'Confirmar'}
          </button>
        </Modal>
      )}
    </div>
  )
}

export default InventarioView
