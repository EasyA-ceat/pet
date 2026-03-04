import request from '@/utils/request'

// 充值活动API
export function getRechargeActivityList() {
  return request({
    url: '/recharge-activities',
    method: 'get'
  })
}

export function getActiveRechargeActivities() {
  return request({
    url: '/recharge-activities/active',
    method: 'get'
  })
}

export function getRechargeActivity(id) {
  return request({
    url: `/recharge-activities/${id}`,
    method: 'get'
  })
}

export function createRechargeActivity(data) {
  return request({
    url: '/recharge-activities',
    method: 'post',
    data
  })
}

export function updateRechargeActivity(id, data) {
  return request({
    url: `/recharge-activities/${id}`,
    method: 'put',
    data
  })
}

export function deleteRechargeActivity(id) {
  return request({
    url: `/recharge-activities/${id}`,
    method: 'delete'
  })
}

// 充值记录API
export function getRechargeRecordList() {
  return request({
    url: '/recharge-records',
    method: 'get'
  })
}

export function getRechargeRecordsByCustomer(customerId) {
  return request({
    url: `/recharge-records/customer/${customerId}`,
    method: 'get'
  })
}

export function recharge(data) {
  return request({
    url: '/recharge-records/recharge',
    method: 'post',
    data
  })
}

// 员工服务抽成API
export function getClerkServiceCommissionList() {
  return request({
    url: '/clerk-service-commissions',
    method: 'get'
  })
}

export function getClerkServiceCommissionsByClerk(clerkId) {
  return request({
    url: `/clerk-service-commissions/clerk/${clerkId}`,
    method: 'get'
  })
}

export function createClerkServiceCommission(data) {
  return request({
    url: '/clerk-service-commissions',
    method: 'post',
    data
  })
}

export function updateClerkServiceCommission(id, data) {
  return request({
    url: `/clerk-service-commissions/${id}`,
    method: 'put',
    data
  })
}

export function deleteClerkServiceCommission(id) {
  return request({
    url: `/clerk-service-commissions/${id}`,
    method: 'delete'
  })
}
