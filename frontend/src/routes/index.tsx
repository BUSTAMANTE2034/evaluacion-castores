import R404 from '../pages/404'
import LoginView from '../pages/auth'
import { Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { administradorRoutes, almacenistaRoutes } from './routes'
import ProtectedRoute from '../components/security/protectedRoute'

const AppRoutes = () => {
  return (
   <Routes>
  <Route path="/" element={<Navigate to="/login" replace />} />
  <Route path="/login" element={<LoginView />} />

  <Route element={<ProtectedRoute allowedRoles={['ADMINISTRADOR']} />}>
    <Route path="/administrador" element={<Outlet />}>
      <Route index element={<Navigate replace to="inventario" />} />
      {administradorRoutes.map(({ path, element }) => (
        <Route key={path} path={path} element={element} />
      ))}
    </Route>
  </Route>

  <Route element={<ProtectedRoute allowedRoles={['ALMACENISTA']} />}>
    <Route path="/almacenista" element={<Outlet/>}>
      <Route index element={<Navigate replace to="inventario" />} />
      {almacenistaRoutes.map(({ path, element }) => (
        <Route key={path} path={path} element={element} />
      ))}
    </Route>
  </Route>

  {/* SIEMPRE AL FINAL */}
  <Route path="*" element={<R404 />} />
</Routes>
  )
}

export default AppRoutes
