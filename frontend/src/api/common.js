import request from '@/utils/request'

export function getServiceTypes() {
  return request({
    url: '/common/service-types',
    method: 'get'
  })
}

export function getPaymentMethods() {
  return request({
    url: '/common/payment-methods',
    method: 'get'
  })
}
