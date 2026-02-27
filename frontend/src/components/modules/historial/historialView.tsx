import { useGetMovimientos } from '../../../lib/api/use-get-movements'
import { formatFecha } from '../../ui/functions'
import type { TipoMovimiento } from '../../../lib/models/movimiento'

const HistorialView = () => {

  const {
    movimientos,
    total,
    pages,
    current_page,
    per_page,
    hasNext,
    hasPrev,
    goNext,
    goPrev,
    tipo,
    setTipo,
  } = useGetMovimientos({
    initialPage: 1,
    initialPerPage: 20,
  })

  return (
    <div className="w-full">

      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-semibold text-[#1f2937]">
          Historial de Movimientos
        </h1>

        <select
          className="cursor-pointer"
          value={tipo ?? ''}
          onChange={(e) =>
            setTipo(
              e.target.value === ''
                ? null
                : (e.target.value as TipoMovimiento)
            )
          }
        >
          <option value="">Todos</option>
          <option value="ENTRADA">Entrada</option>
          <option value="SALIDA">Salida</option>
        </select>
      </div>

      {/* TABLA */}
      <div className="bg-white rounded-xl shadow-md overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-[#1f2937] text-white">
            <tr>
              <th className="p-3 text-left">Producto</th>
              <th className="p-3 text-left">Movimiento</th>
              <th className="p-3 text-left">Cantidad</th>
              <th className="p-3 text-left">Usuario</th>
              <th className="p-3 text-left">Fecha</th>
            </tr>
          </thead>

          <tbody>
            {movimientos.map((m: any) => (
              <tr key={m.idMovimiento} className="border-b hover:bg-gray-50">
                <td className="p-3">
                  {m.producto?.nombre}
                </td>

                <td className="p-3">
                  <span
                    className={`px-2 py-1 rounded text-xs ${
                      m.tipoMovimiento === 'ENTRADA'
                        ? 'bg-blue-100 text-blue-700'
                        : 'bg-red-100 text-red-700'
                    }`}
                  >
                    {m.tipoMovimiento}
                  </span>
                </td>

                <td className="p-3">{m.cantidad}</td>

                <td className="p-3">
                  {m.usuario?.nombre}
                </td>

                <td className="p-3">
                  {formatFecha(m.fechaCreacion)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {/* FOOTER PAGINADO */}
        <div className="flex justify-end items-center p-4 bg-gray-50 text-sm gap-4">
          <span>Total: {total}</span>
          <span>Página: {current_page} / {pages}</span>
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
  )
}

export default HistorialView