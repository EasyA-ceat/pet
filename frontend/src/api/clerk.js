
import request from '@/utils/request'

export function getClerkList() {
  return request({
    url: '/clerks',
    method: 'get'
  })
}

export function getClerk(id) {
  return request({
    url: `/clerks/${id}`,
    method: 'get'
  })
}

export function createClerk(data) {
  return request({
    url: '/clerks',
    method: 'post',
    data
  })
}

export function updateClerk(id, data) {
  return request({
    url: `/clerks/${id}`,
    method: 'put',
    data
  })
}

export function deleteClerk(id) {
  return request({
    url: `/clerks/${id}`,
    method: 'delete'
  })
}
