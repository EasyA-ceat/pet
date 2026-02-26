
import request from '@/utils/request'

export function getTransactionList() {
  return request({
    url: '/transactions',
    method: 'get'
  })
}

export function getTransaction(id) {
  return request({
    url: `/transactions/${id}`,
    method: 'get'
  })
}

export function createTransaction(data) {
  return request({
    url: '/transactions',
    method: 'post',
    data
  })
}

export function updateTransaction(id, data) {
  return request({
    url: `/transactions/${id}`,
    method: 'put',
    data
  })
}

export function deleteTransaction(id) {
  return request({
    url: `/transactions/${id}`,
    method: 'delete'
  })
}

export function searchTransactions(keyword) {
  return request({
    url: '/transactions/search',
    method: 'get',
    params: { keyword }
  })
}

export function calculateTotalCommission() {
  return request({
    url: '/transactions/commission',
    method: 'get'
  })
}
