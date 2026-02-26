
import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
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
          path: 'reports',
          name: 'Reports',
          component: () => import('@/views/Reports.vue')
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

export default router
