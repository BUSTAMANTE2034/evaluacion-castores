export const formatFecha = (isoString: string|null) => {
  if (!isoString)return
  const date = new Date(isoString)

  const day = date.getDate().toString().padStart(2, '0')
  const monthIndex = date.getMonth() // 0-11
  const year = date.getFullYear()

  const meses = [
    'ene', 'feb', 'mar', 'abr', 'may', 'jun',
    'jul', 'ago', 'sep', 'oct', 'nov', 'dic'
  ]

  let hours = date.getHours()
  const minutes = date.getMinutes().toString().padStart(2, '0')
  const ampm = hours >= 12 ? 'p.m.' : 'a.m.'

  hours = hours % 12
  if (hours === 0) hours = 12 // 0 -> 12

  return `${day}-${meses[monthIndex]}-${year} (${hours}:${minutes}${ampm})`
}
