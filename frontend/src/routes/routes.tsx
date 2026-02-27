import HistorialView from "../pages/modules/historial"
import InventarioView from "../pages/modules/inventario"
import SalidaView from "../pages/modules/salida"


const administradorRoutes = [
  { path: 'inventario', element: <InventarioView/> },
  { path: 'historial', element: <HistorialView/> }

  
]
const almacenistaRoutes = [
  { path: 'inventario', element: <InventarioView/> },
  { path: 'salidas', element: <SalidaView/> }
]


export { administradorRoutes, almacenistaRoutes }
