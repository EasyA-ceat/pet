
import request from '@/utils/request'

export function getDailyReport() {
  return request({
    url: '/reports/daily',
    method: 'get'
  })
}

export function getMonthlyReport() {
  return request({
    url: '/reports/monthly',
    method: 'get'
  })
}

export function getAnnualReport() {
  return request({
    url: '/reports/annual',
    method: 'get'
  })
}

export function exportPdfReport() {
  return request({
    url: '/reports/export/pdf',
    method: 'get',
    responseType: 'blob'
  })
}

export function exportExcelReport() {
  return request({
    url: '/reports/export/excel',
    method: 'get',
    responseType: 'blob'
  })
}

export function getChartData(type) {
  return request({
    url: '/reports/chart',
    method: 'get',
    params: { type }
  })
}
