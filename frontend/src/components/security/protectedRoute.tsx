import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/authContext'
import Loader from '../ui/loader'

type ProtectedRouteProps = {
  allowedRoles?: string[]
}

const ProtectedRoute = ({ allowedRoles }: ProtectedRouteProps) => {
  const { isAuthenticated, isLoading,usuario } = useAuth()

  if (isLoading) {
    return (
      <div className='flex items-center w-full h-screen'>
        <Loader label='Cargando...' className='text-xl' size={80}/>
      </div>
    )
  }

  // usuario no logueado  Login
  if (!isAuthenticated || !usuario) {
    return <Navigate to="/login" replace />
  }

  if ( window.location.pathname === "/login") {
    return <Outlet />;
  }


  // Validación de roles
  if (allowedRoles && !allowedRoles.includes(usuario.rol)) {
    return <Navigate to={`/${usuario.rol}`} replace />
  }

  return <Outlet />
}

export default ProtectedRoute
