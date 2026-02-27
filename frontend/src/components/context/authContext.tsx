import type { Usuario } from '../../lib/models/usuario'
import {
  createContext,
  useContext,
  useEffect,
  useState,
} from 'react'
import type { ReactNode } from 'react'

export interface AuthProviderProps {
  children: ReactNode
}

interface AuthContextType {
  usuario: Usuario | null
  token: string | null
  isLoading: boolean
  isAuthenticated: boolean
  login: (usuario: Usuario, token: string) => void
  logout: () => void
  setToken: (token: string | null) => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

const USUARIO_KEY = 'usuario'
const TOKEN_KEY = 'token'

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [usuario, setUsuario] = useState<Usuario | null>(null)
  const [token, setTokenState] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  // 🔹 Cargar desde localStorage al iniciar
  useEffect(() => {
    const storedUsuario = localStorage.getItem(USUARIO_KEY)
    const storedToken = localStorage.getItem(TOKEN_KEY)

    if (storedUsuario) {
      try {
        setUsuario(JSON.parse(storedUsuario))
      } catch {
        localStorage.removeItem(USUARIO_KEY)
      }
    }

    if (storedToken) {
      setTokenState(storedToken)
    }

    setIsLoading(false)
  }, [])

  //Login
  const login = (usuarioData: Usuario, token: string) => {
    setUsuario(usuarioData)
    setTokenState(token)

    localStorage.setItem(USUARIO_KEY, JSON.stringify(usuarioData))
    localStorage.setItem(TOKEN_KEY, token)
  }

  // Logout
  const logout = () => {
    setUsuario(null)
    setTokenState(null)

    localStorage.removeItem(USUARIO_KEY)
    localStorage.removeItem(TOKEN_KEY)
  }

  // Setter manual de token
  const setToken = (newToken: string | null) => {
    setTokenState(newToken)

    if (newToken) {
      localStorage.setItem(TOKEN_KEY, newToken)
    } else {
      localStorage.removeItem(TOKEN_KEY)
    }
  }

  return (
    <AuthContext.Provider
      value={{
        usuario,
        token,
        isLoading,
        isAuthenticated: !!usuario && !!token,
        login,
        logout,
        setToken,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

// Hook
export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth debe usarse dentro de AuthProvider')
  }
  return ctx
}