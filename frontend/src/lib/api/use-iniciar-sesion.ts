import { useState } from 'react'
import type { LoginResponse, PostLogin } from '../models/login'
import type { Usuario } from '../models/usuario'
import { useAuth } from '../../components/context/authContext'
import { apiFetch } from './client'

export const useLogin = () => {
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()

  const authUser = async (credentials: PostLogin) => {
    setLoading(true)

    try {
      const data = await apiFetch<LoginResponse>('/auth/login', {
        method: 'POST',
        body: credentials,
      })

      // Construimos el usuario
      const usuario: Usuario = {
        nombre: data.nombre,
        correo: data.correo,
        rol: data.rol,
        estatus: data.estatus,
      }

      // Guardamos usuario + token en contexto
      login(usuario, data.token)

      return usuario
    } finally {
      setLoading(false)
    }
  }

  return { authUser, loading }
}