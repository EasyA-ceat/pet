import request from '@/utils/request'

// 获取系统设置
export function getSettings() {
  return request({
    url: '/settings',
    method: 'get'
  })
}

// 保存系统设置
export function saveSettings(data) {
  return request({
    url: '/settings',
    method: 'post',
    data
  })
}

// 手动备份数据库
export function backupDatabase() {
  return request({
    url: '/settings/backup',
    method: 'post'
  })
}

// 恢复数据库
export function restoreDatabase(backupFileName) {
  return request({
    url: '/settings/restore',
    method: 'post',
    params: { backupFileName }
  })
}

// 清除数据库
export function clearDatabase() {
  return request({
    url: '/settings/clear',
    method: 'post'
  })
}

// 获取备份文件列表
export function getBackupFiles() {
  return request({
    url: '/settings/backups',
    method: 'get'
  })
}
