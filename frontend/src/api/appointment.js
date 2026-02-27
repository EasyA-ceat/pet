import request from '@/utils/request'

export function getAppointmentList() {
  return request({
    url: '/appointments',
    method: 'get'
  })
}

export function getAppointment(id) {
  return request({
    url: `/appointments/${id}`,
    method: 'get'
  })
}

export function getAppointmentsByCustomer(customerId) {
  return request({
    url: `/appointments/customer/${customerId}`,
    method: 'get'
  })
}

export function getAppointmentsByStatus(status) {
  return request({
    url: `/appointments/status/${status}`,
    method: 'get'
  })
}

export function getTodayAppointments() {
  return request({
    url: '/appointments/today',
    method: 'get'
  })
}

export function getUpcomingAppointments() {
  return request({
    url: '/appointments/upcoming',
    method: 'get'
  })
}

export function getAppointmentsByDateRange(start, end) {
  return request({
    url: '/appointments/date-range',
    method: 'get',
    params: { start, end }
  })
}

export function createAppointment(data) {
  return request({
    url: '/appointments',
    method: 'post',
    data
  })
}

export function updateAppointment(id, data) {
  return request({
    url: `/appointments/${id}`,
    method: 'put',
    data
  })
}

export function deleteAppointment(id) {
  return request({
    url: `/appointments/${id}`,
    method: 'delete'
  })
}

export function getAppointmentStats() {
  return request({
    url: '/appointments/stats',
    method: 'get'
  })
}
