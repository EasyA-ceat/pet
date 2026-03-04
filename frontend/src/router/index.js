
import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      component: Layout,
      children: [
        {
          path: '',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue')
        },
        {
          path: 'customers',
          name: 'Customers',
          component: () => import('@/views/Customers.vue')
        },
        {
          path: 'appointments',
          name: 'Appointments',
          component: () => import('@/views/Appointments.vue')
        },
        {
          path: 'transactions',
          name: 'Transactions',
          component: () => import('@/views/Transactions.vue')
        },
        {
          path: 'clerks',
          name: 'Clerks',
          component: () => import('@/views/Clerks.vue')
        },
        {
          path: 'recharge',
          name: 'Recharge',
          component: () => import('@/views/RechargeManagement.vue')
        },
        {
          path: 'reports',
          name: 'Reports',
          component: () => import('@/views/Reports.vue')
        },
        {
          path: 'roles',
          name: 'Roles',
          component: () => import('@/views/Roles.vue')
        },
        {
          path: 'settings',
          name: 'Settings',
          component: () => import('@/views/Settings.vue')
        },
        {
          path: 'about',
          name: 'About',
          component: () => import('@/views/About.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
