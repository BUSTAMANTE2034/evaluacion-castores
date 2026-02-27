const Modal = ({ title, children, onClose }: any) => (
  <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50">
    <div className="bg-white p-6 rounded-xl shadow-xl w-96">
      <h2 className="text-lg font-semibold mb-4">{title}</h2>
      <div className="flex flex-col gap-4">{children}</div>
      <button
        onClick={onClose}
        className="cursor-pointer  mt-4 text-sm text-gray-500 border border-gray-300 rounded-xl px-4 py-2 hover:bg-gray-100 transition"
      >
        Cancelar
      </button>
    </div>
  </div>
)
export default Modal