import React, { useState } from 'react';
import { Layout, Menu, theme } from 'antd';
import {
  HomeOutlined,
  MedicineBoxOutlined,
  UserOutlined,
  ScheduleOutlined,
  PlusCircleOutlined,
  ShopOutlined,
  BellOutlined,
  TeamOutlined,
  FileTextOutlined
} from '@ant-design/icons';
import HomePage from './pages/HomePage';
import PetsPage from './pages/PetsPage';
import OwnersPage from './pages/OwnersPage';
import DoctorsPage from './pages/DoctorsPage';
import ConsultationsPage from './pages/ConsultationsPage';
import PrescriptionsPage from './pages/PrescriptionsPage';
import MedicinesPage from './pages/MedicinesPage';
import BoardingsPage from './pages/BoardingsPage';
import BillsPage from './pages/BillsPage';
import RemindersPage from './pages/RemindersPage';

const { Header, Sider, Content } = Layout;

const menuItems = [
  { key: '/', icon: <HomeOutlined />, label: '首页', component: HomePage },
  { key: '/pets', icon: <MedicineBoxOutlined />, label: '宠物管理', component: PetsPage },
  { key: '/owners', icon: <UserOutlined />, label: '主人管理', component: OwnersPage },
  { key: '/doctors', icon: <TeamOutlined />, label: '医生排班', component: DoctorsPage },
  { key: '/consultations', icon: <FileTextOutlined />, label: '就诊管理', component: ConsultationsPage },
  { key: '/prescriptions', icon: <PlusCircleOutlined />, label: '处方管理', component: PrescriptionsPage },
  { key: '/medicines', icon: <ShopOutlined />, label: '药品库存', component: MedicinesPage },
  { key: '/boardings', icon: <ScheduleOutlined />, label: '寄养管理', component: BoardingsPage },
  { key: '/bills', icon: <FileTextOutlined />, label: '账单管理', component: BillsPage },
  { key: '/reminders', icon: <BellOutlined />, label: '回访提醒', component: RemindersPage },
];

function App() {
  const [selectedKey, setSelectedKey] = useState('/');
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  const handleMenuClick = ({ key }) => {
    setSelectedKey(key);
  };

  const getCurrentPage = () => {
    const item = menuItems.find(item => item.key === selectedKey);
    if (item && item.component) {
      const Component = item.component;
      return <Component />;
    }
    return <HomePage />;
  };

  return (
    <Layout style={{ height: '100vh' }}>
      <Sider width={200} theme="dark">
        <div style={{ 
          height: 64, 
          display: 'flex', 
          alignItems: 'center', 
          justifyContent: 'center',
          color: '#fff',
          fontSize: '16px',
          fontWeight: 'bold',
          borderBottom: '1px solid #303030'
        }}>
          宠物医院管理系统
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[selectedKey]}
          onClick={handleMenuClick}
          items={menuItems}
          style={{ borderRight: 0 }}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: '0 24px', background: colorBgContainer, display: 'flex', alignItems: 'center' }}>
          <span className="header-title">
            {menuItems.find(item => item.key === selectedKey)?.label || '首页'}
          </span>
        </Header>
        <Content style={{ margin: '0', padding: '24px', background: '#f0f2f5', minHeight: 280 }}>
          {getCurrentPage()}
        </Content>
      </Layout>
    </Layout>
  );
}

export default App;
