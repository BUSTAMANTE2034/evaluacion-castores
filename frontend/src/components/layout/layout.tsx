import { useState } from 'react'
import type { ReactNode } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/authContext'
import {
  Package,
  ArrowUpCircle,
  History,
  User,
} from 'lucide-react'

interface Props {
  children: ReactNode
}

const MainLayout = ({ children }: Props) => {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const [openModal, setOpenModal] = useState(false)
  const [closingSession, setClosingSession] = useState(false)
  const [toast, setToast] = useState<string | null>(null)

  const basePath = `/${usuario?.rol.toLowerCase()}`

  const menuItems = [
  {
    label: 'Inventario',
    path: `${basePath}/inventario`,
    icon: Package,
  },
  {
    label: 'Salidas',
    path: `${basePath}/salidas`,
    icon: ArrowUpCircle,
  },
  {
    label: 'Historial',
    path: `${basePath}/historial`,
    icon: History,
  },
].filter((item) => {
  // Si es ADMINISTRADOR - NO mostrar "Salidas"
  if (usuario?.rol === 'ADMINISTRADOR' && item.label === 'Salidas') {
    return false
  }

  // Si NO es ADMINISTRADOR - NO mostrar "Historial"
  if (usuario?.rol !== 'ADMINISTRADOR' && item.label === 'Historial') {
    return false
  }

  return true
})

  const handleLogout = async () => {
    setOpenModal(false)
    setClosingSession(true)

    // Espera visual 2 segundos
    setTimeout(() => {
      logout()
      setClosingSession(false)

    //   // Mostrar toast pequeño
    //   setToast('Cierre exitoso')

      setTimeout(() => {
        navigate('/login', { replace: true })
      }, 800)

    }, 2000)
  }

  return (
    <div className="min-h-screen flex bg-[#f3f4f6] relative">

      {/* Sidebar */}
      <aside className="w-15 bg-[#1f2937] flex flex-col items-center py-6 justify-between">
        <div className="flex flex-col gap-6 items-center">
          {menuItems.map(({ label, path, icon: Icon }) => {
            const active = location.pathname === path

            return (
              <button
                key={label}
                onClick={() => navigate(path)}
                className={` cursor-pointer
                  
                  group relative
                  p-3 rounded-xl
                  transition-all duration-200
                  ${active ? 'bg-[#4156c2]' : 'hover:bg-[#374151]'}
                `}
              >
                <Icon size={22} className="text-white" />

                <span className=" z-100
                  absolute left-16
                  whitespace-nowrap
                  bg-[#1f2937]
                  text-white text-xs
                  px-2 py-1 rounded-md
                  opacity-0 group-hover:opacity-100
                  transition
                ">
                  {label}
                </span>
              </button>
            )
          })}
        </div>

        <div className="relative">
          <button
            onClick={() => setOpenModal(!openModal)}
            className="p-3 rounded-xl hover:bg-[#374151] transition"
          >
            <User size={22} className="text-white" />
          </button>

          {openModal && (
            <div className="
              absolute bottom-12 left-10
              bg-white
              rounded-xl
              shadow-xl
              px-4 py-3
              w-40
              text-center
            ">
              <p className="text-sm text-gray-600 mb-2">
                {usuario?.nombre}
              </p>

              <button
                onClick={handleLogout}
                className="
                  w-full py-1.5
                  rounded-lg
                  bg-[#c72628]
                  text-white text-sm
                  hover:opacity-90
                  transition
                "
              >
                Cerrar sesión
              </button>
            </div>
          )}
        </div>
      </aside>

      {/* Contenido derecho */}
      <main className="flex-1 p-8">
        {children}
      </main>

      {/* Overlay cerrando sesión */}
      {closingSession && (
        <div className="
          absolute inset-0
          bg-black/50
          backdrop-blur-sm
          flex items-center justify-center
          z-50
        ">
          <h2 className="text-white text-2xl font-semibold animate-pulse">
            Cerrando sesión...
          </h2>
        </div>
      )}

      {/* Toast pequeño */}
      {toast && (
        <div className="
          absolute bottom-6 left-1/2 -translate-x-1/2
          bg-[#4156c2]
          text-white
          px-6 py-2
          rounded-xl
          shadow-lg
          text-sm
          z-50
        ">
          {toast}
        </div>
      )}
    </div>
  )
}

export default MainLayout