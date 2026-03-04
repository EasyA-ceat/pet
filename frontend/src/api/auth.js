
import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getCurrentUser() {
  return request({
    url: '/auth/me',
    method: 'get'
  })
}

export function logout() {
  // 前端清除token即可，后端是无状态的
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
}
