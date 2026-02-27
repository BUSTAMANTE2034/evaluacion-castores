import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useLogin } from '../../../lib/api/use-iniciar-sesion'

const LoginView = () => {
  const { authUser, loading } = useLogin()
  const navigate = useNavigate()

  const [correo, setCorreo] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [toast, setToast] = useState<string | null>(null)
  const [toastType, setToastType] = useState<'error' | 'success'>('error')

  const showToast = (message: string, type: 'error' | 'success' = 'error') => {
    setToastType(type)
    setToast(message)
    setTimeout(() => setToast(null), 3000)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!correo || !contrasena) {
      showToast('Todos los campos son obligatorios', 'error')
      return
    }

    try {
      const usuario = await authUser({ correo, contrasena })

      // bienvenida
      showToast(`Bienvenido, ${usuario.nombre}`, 'success')

      //  antes de redirigir
      setTimeout(() => {
        navigate(`/${usuario.rol.toLowerCase()}/inventario`, {
          replace: true,
        })
      }, 1200)

    } catch (err) {
      if (err instanceof Error) {
        showToast(err.message, 'error')
      } else {
        showToast('Error al iniciar sesión', 'error')
      }
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-linear-to-br from-[#5390d5] to-[#d4a5a6]">

      <div className="
        md:w-[25vw] 
        md:h-[60vh]
        max-w-2xl 
        min-w-[35%]
        bg-white/95 
        backdrop-blur-md 
        rounded-2xl 
        shadow-2xl 
        flex flex-col 
        justify-center 
        px-5 
        py-5
        gap-4
        relative
        transition-all
        duration-300
        hover:scale-[1.01]
      ">

        <div className="mb-8 text-center">
          <h1 className="text-4xl font-bold text-[#17202a] tracking-wide">
            Bienvenido
          </h1>
          <p className="text-gray-700 mt-2 text-sm">
            Sistema de Gestión de Inventario
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">

          <div>
            <label className="block text-sm font-bold text-gray-600 mb-1">
              Correo electrónico
            </label>
            <input
              type="email"
              value={correo}
              onChange={(e) => setCorreo(e.target.value)}
              className="
                w-full px-4 py-3 
                rounded-xl 
                focus:outline-none 
                focus:ring-2
                focus:border-none
                focus:ring-[#4156c2]
                transition
              "
              placeholder="usuario@empresa.com"
            />
          </div>

          <div>
            <label className="block text-sm font-bold text-gray-600 mb-1">
              Contraseña
            </label>
            <input
              type="password"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              className="
               w-full px-4 py-3 
                rounded-xl 
                focus:outline-none 
                focus:ring-2
                focus:border-none
                focus:ring-[#4156c2]
                transition
              "
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="
            cursor-pointer
              w-full py-3
              rounded-xl
              bg-[#17202a]
              text-white
              font-semibold
              tracking-wide
              hover:bg-[#2c4d6c]
              transition-all
              duration-300
              disabled:opacity-50
            "
          >
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>

        {/* Mini Toast */}
        {toast && (
          <div
            className={`
              absolute 
              bottom-2
              left-1/2 
              -translate-x-1/2 
              px-6 
              py-2 
              rounded-xl 
              shadow-lg 
              text-sm
              animate-pulse
              ${toastType === 'success' ? 'bg-blue-500' : 'bg-[#c72628] text-white'}
            `}
          >
            {toast}
          </div>
        )}
      </div>
    </div>
  )
}

export default LoginView