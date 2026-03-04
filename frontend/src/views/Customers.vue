<template>
  <div class="customers">
    <h1>顾客管理</h1>
    <div class="container">
      <!-- 统计信息 -->
      <div class="stats">
        <div class="stat-card">
          <div class="stat-label">总顾客数</div>
          <div class="stat-value">{{ stats.totalCustomers || 0 }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">活跃顾客</div>
          <div class="stat-value">{{ stats.activeCustomers || 0 }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">本月新增</div>
          <div class="stat-value">{{ stats.recentCustomers || 0 }}</div>
        </div>
      </div>

      <!-- 搜索和添加按钮 -->
      <div class="actions">
        <input 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索顾客姓名、电话、宠物名..."
          class="search-input"
          @keyup.enter="searchCustomers"
        />
        <button @click="searchCustomers" class="btn btn-outline">搜索</button>
        <button @click="addCustomer" class="btn btn-primary">添加顾客</button>
      </div>

      <!-- 顾客列表 -->
      <div class="customer-list">
        <table class="customer-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>顾客姓名</th>
              <th>VIP</th>
              <th>余额</th>
              <th>电话</th>
              <th>宠物姓名</th>
              <th>宠物类型</th>
              <th>宠物品种</th>
              <th>宠物年龄</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="customer in customers" :key="customer.id">
              <td>{{ customer.id }}</td>
              <td>{{ customer.customerName }}</td>
              <td>
                <span v-if="customer.isVip" class="vip-badge">VIP</span>
                <span v-else>-</span>
              </td>
              <td class="balance-cell">
                <span class="balance-amount">¥{{ (customer.balance || 0).toFixed(2) }}</span>
              </td>
              <td>{{ customer.phone }}</td>
              <td>{{ customer.petName }}</td>
              <td>{{ customer.petType }}</td>
              <td>{{ customer.petBreed }}</td>
              <td>{{ customer.petAge }}</td>
              <td>{{ formatDate(customer.createTime) }}</td>
              <td>
                <button @click="editCustomer(customer)" class="btn btn-sm btn-secondary">编辑</button>
                <button @click="viewPhotos(customer)" class="btn btn-sm btn-info">照片</button>
                <button @click="deleteCustomer(customer.id)" class="btn btn-sm btn-danger">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 无数据提示 -->
      <div v-if="customers.length === 0" class="no-data">
        <p>暂无顾客数据</p>
      </div>
    </div>

    <!-- 添加/编辑顾客模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h2>{{ isEdit ? '编辑顾客' : '添加顾客' }}</h2>
          <button @click="closeModal" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveCustomer">
            <div class="form-group">
              <label>顾客姓名 *</label>
              <input type="text" v-model="customerForm.customerName" required />
            </div>
            <div class="form-group">
              <label>联系电话</label>
              <input type="text" v-model="customerForm.phone" />
            </div>
            <div class="form-group">
              <label>宠物姓名 *</label>
              <input type="text" v-model="customerForm.petName" required />
            </div>
            <div class="form-group">
              <label>宠物类型</label>
              <input type="text" v-model="customerForm.petType" />
            </div>
            <div class="form-group">
              <label>宠物品种</label>
              <input type="text" v-model="customerForm.petBreed" />
            </div>
            <div class="form-group">
              <label>宠物年龄</label>
              <input type="number" v-model="customerForm.petAge" />
            </div>
            <div class="form-group">
              <label>是否VIP会员</label>
              <input type="checkbox" v-model="customerForm.isVip" />
            </div>
            <div class="form-group">
              <label>账户余额</label>
              <input type="number" v-model.number="customerForm.balance" step="0.01" disabled />
            </div>
            <div class="form-group">
              <label>累计充值</label>
              <input type="number" v-model.number="customerForm.totalRecharge" step="0.01" disabled />
            </div>
            <div class="form-group">
              <label>备注</label>
              <textarea v-model="customerForm.notes"></textarea>
            </div>
            <div class="modal-actions">
              <button type="button" @click="closeModal" class="btn btn-outline">取消</button>
              <button type="submit" class="btn btn-primary">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 照片管理模态框 -->
    <div v-if="showPhotoModal" class="modal-overlay" @click.self="closePhotoModal">
      <div class="modal photo-modal">
        <div class="modal-header">
          <h2>{{ currentCustomer?.customerName }} - 照片管理</h2>
          <button @click="closePhotoModal" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
          <div class="upload-section">
            <input 
              type="file" 
              ref="photoInput" 
              accept="image/*" 
              @change="handlePhotoUpload" 
              style="display: none"
            />
            <button @click="$refs.photoInput.click()" class="btn btn-primary">
              上传照片
            </button>
          </div>
          <div class="photos-grid">
            <div v-for="photo in photos" :key="photo.id" class="photo-item">
              <div class="photo-preview">
                <img :src="getPhotoUrl(photo)" :alt="photo.fileName" />
              </div>
              <div class="photo-name">{{ photo.fileName }}</div>
              <button @click="deletePhoto(photo.id)" class="btn btn-sm btn-danger">删除</button>
            </div>
            <div v-if="photos.length === 0" class="no-photos">
              暂无照片
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as customerApi from '@/api/customer'

// 数据状态
const customers = ref([])
const searchKeyword = ref('')
const stats = ref({})
const showModal = ref(false)
const showPhotoModal = ref(false)
const isEdit = ref(false)
const currentCustomer = ref(null)
const photos = ref([])
const photoInput = ref(null)

const customerForm = ref({
  id: null,
  customerName: '',
  phone: '',
  petName: '',
  petType: '',
  petBreed: '',
  petAge: null,
  isVip: false,
  balance: 0,
  totalRecharge: 0,
  notes: ''
})

// 加载顾客数据
const loadCustomers = async () => {
  try {
    const data = await customerApi.getCustomerList()
    customers.value = data
  } catch (error) {
    console.error('加载顾客数据失败:', error)
  }
}

// 加载统计信息
const loadStats = async () => {
  try {
    const response = await fetch('/api/customers/stats')
    if (response.ok) {
      stats.value = await response.json()
    }
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

// 搜索顾客
const searchCustomers = async () => {
  try {
    if (!searchKeyword.value.trim()) {
      await loadCustomers()
      return
    }
    const data = await customerApi.searchCustomers(searchKeyword.value)
    customers.value = data
  } catch (error) {
    console.error('搜索顾客失败:', error)
  }
}

// 添加顾客
const addCustomer = () => {
  isEdit.value = false
  customerForm.value = {
    id: null,
    customerName: '',
    phone: '',
    petName: '',
    petType: '',
    petBreed: '',
    petAge: null,
    isVip: false,
    balance: 0,
    totalRecharge: 0,
    notes: ''
  }
  showModal.value = true
}

// 编辑顾客
const editCustomer = (customer) => {
  isEdit.value = true
  customerForm.value = { ...customer }
  showModal.value = true
}

// 保存顾客
const saveCustomer = async () => {
  try {
    if (isEdit.value) {
      await customerApi.updateCustomer(customerForm.value.id, customerForm.value)
    } else {
      await customerApi.createCustomer(customerForm.value)
    }
    closeModal()
    await loadCustomers()
    await loadStats()
  } catch (error) {
    console.error('保存顾客失败:', error)
  }
}

// 删除顾客
const deleteCustomer = async (id) => {
  if (confirm('确定要删除该顾客吗？')) {
    try {
      await customerApi.deleteCustomer(id)
      await loadCustomers()
      await loadStats()
    } catch (error) {
      console.error('删除顾客失败:', error)
    }
  }
}

// 查看照片
const viewPhotos = async (customer) => {
  currentCustomer.value = customer
  try {
    const data = await customerApi.getCustomerPhotos(customer.id)
    photos.value = data
  } catch (error) {
    photos.value = []
    console.error('加载照片失败:', error)
  }
  showPhotoModal.value = true
}

// 上传照片
const handlePhotoUpload = async (event) => {
  const file = event.target.files[0]
  if (!file || !currentCustomer.value) return

  try {
    await customerApi.uploadCustomerPhoto(currentCustomer.value.id, file)
    // 重新加载照片
    const data = await customerApi.getCustomerPhotos(currentCustomer.value.id)
    photos.value = data
  } catch (error) {
    console.error('上传照片失败:', error)
  }

  // 清空文件输入
  event.target.value = ''
}

// 删除照片
const deletePhoto = async (photoId) => {
  if (confirm('确定要删除这张照片吗？')) {
    try {
      await customerApi.deletePhoto(photoId)
      // 重新加载照片
      const data = await customerApi.getCustomerPhotos(currentCustomer.value.id)
      photos.value = data
    } catch (error) {
      console.error('删除照片失败:', error)
    }
  }
}

// 关闭模态框
const closeModal = () => {
  showModal.value = false
}

const closePhotoModal = () => {
  showPhotoModal.value = false
  photos.value = []
  currentCustomer.value = null
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 获取照片URL
const getPhotoUrl = (photo) => {
  return `/api/files/photos/thumb/${photo.fileName}`
}

// 组件挂载时加载数据
onMounted(() => {
  loadCustomers()
  loadStats()
})
</script>

<style scoped>
.customers {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 统计信息 */
.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  border-radius: 8px;
  color: white;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin-top: 8px;
}

.actions {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}

.search-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.btn-primary {
  background: #4CAF50;
  color: white;
}

.btn-secondary {
  background: #2196F3;
  color: white;
}

.btn-danger {
  background: #f44336;
  color: white;
}

.btn-info {
  background: #9C27B0;
  color: white;
}

.btn-outline {
  background: white;
  color: #333;
  border: 1px solid #ddd;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
  margin-right: 4px;
}

.customer-list {
  overflow-x: auto;
}

.customer-table {
  width: 100%;
  border-collapse: collapse;
}

.customer-table th,
.customer-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.customer-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.customer-table tr:hover {
  background-color: #f5f5f5;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #666;
}

/* 模态框 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.photo-modal {
  max-width: 700px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h2 {
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group textarea {
  min-height: 80px;
  resize: vertical;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 照片管理 */
.upload-section {
  margin-bottom: 20px;
}

.photos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 16px;
}

.photo-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.photo-preview {
  aspect-ratio: 1;
  background: #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.photo-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.photo-name {
  font-size: 12px;
  color: #666;
  text-align: center;
  word-break: break-all;
}

.no-photos {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px;
  color: #666;
}

.vip-badge {
  display: inline-block;
  padding: 2px 8px;
  background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);
  color: white;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.balance-cell {
  font-weight: bold;
}

.balance-amount {
  color: #409eff;
  font-size: 16px;
}
</style>
