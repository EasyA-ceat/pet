
import request from '@/utils/request'

export function getRolesAndPermissions() {
  return request({
    url: '/api/roles',
    method: 'get'
  })
}

