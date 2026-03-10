import request from '@/utils/request'

export function getRolesAndPermissions() {
  return request({
    url: '/roles',
    method: 'get'
  })
}

export function updateRolePermissions(roleName, permissions) {
  return request({
    url: `/roles/${roleName}/permissions`,
    method: 'put',
    data: permissions
  })
}

