
import request from '@/utils/request'

export function getCustomerList() {
  return request({
    url: '/customers',
    method: 'get'
  })
}

export function getCustomer(id) {
  return request({
    url: `/customers/${id}`,
    method: 'get'
  })
}

export function createCustomer(data) {
  return request({
    url: '/customers',
    method: 'post',
    data
  })
}

export function updateCustomer(id, data) {
  return request({
    url: `/customers/${id}`,
    method: 'put',
    data
  })
}

export function deleteCustomer(id) {
  return request({
    url: `/customers/${id}`,
    method: 'delete'
  })
}

export function searchCustomers(keyword) {
  return request({
    url: '/customers/search',
    method: 'get',
    params: { keyword }
  })
}

export function uploadCustomerPhoto(customerId, file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: `/customers/${customerId}/photos`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function getCustomerPhotos(customerId) {
  return request({
    url: `/customers/${customerId}/photos`,
    method: 'get'
  })
}

export function deletePhoto(photoId) {
  return request({
    url: `/customers/photos/${photoId}`,
    method: 'delete'
  })
}
