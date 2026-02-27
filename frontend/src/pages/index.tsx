import { StrictMode, useEffect, useState } from 'react'
import { createRoot } from 'react-dom/client'
import '../assets/styles/index.css'
import { BrowserRouter } from 'react-router-dom'
import AppRoutes from '../routes/index'
import { AuthProvider } from '../components/context/authContext'

const Root = () => {
  const [isLoading, setIsLoading] = useState(true)
  useEffect(() => {
    const t = setTimeout(() => setIsLoading(false), 500)
    return () => clearTimeout(t)
  }, [])
  return (
    <>
        <AuthProvider>
         <AppRoutes />
        </AuthProvider>
      {isLoading && (
        <div className="fixed inset-0 flex items-center justify-center bg-white z-50">
          <span className='text-2xl text-center'>Cargando...</span>
        </div>
      )}
    </>
  )
}

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <StrictMode>
      <Root />
    </StrictMode>
  </BrowserRouter>
)
